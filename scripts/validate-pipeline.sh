#!/bin/bash

set -e

echo "=========================================="
echo "PIPELINE DE VALIDAÇÃO - IMPORTAÚ"
echo "=========================================="
echo ""

cd /home/ubuntu/environment/aidev/openfinance/importau

PUBLIC_IP=$(curl -s checkip.amazonaws.com)
TIMESTAMP=$(date '+%Y_%m_%d_%H_%M')
REPORT_DIR="/home/ubuntu/environment/aidev/openfinance/importau/pipeline-reports/$TIMESTAMP"
mkdir -p "$REPORT_DIR"

echo "1. VERIFICANDO DOCKERFILES"
echo "=========================================="
for service in produtos pedidos pagamentos fornecedores emprestimos cambio; do
    if [ -f "backend/$service/Dockerfile" ]; then
        echo "✓ Dockerfile encontrado: backend/$service/Dockerfile"
    else
        echo "✗ Dockerfile NÃO encontrado: backend/$service/Dockerfile"
        exit 1
    fi
done
echo ""

echo "2. CONSTRUINDO E INICIANDO CONTAINERS"
echo "=========================================="
cd infra/docker-compose/singlenode
docker-compose down -v
docker-compose up --build -d
echo "Aguardando serviços iniciarem (60s)..."
sleep 60
echo ""

echo "3. TESTANDO CONECTIVIDADE DOS MICROSERVIÇOS"
echo "=========================================="
FAILED=0

test_service() {
    local name=$1
    local port=$2
    local endpoint=$3
    
    echo -n "Testando $name (porta $port)... "
    if curl -f -s -m 10 "http://localhost:$port$endpoint" > /dev/null 2>&1; then
        echo "✓ OK"
        echo "$name: OK" >> "$REPORT_DIR/connectivity.log"
        return 0
    else
        echo "✗ FALHOU"
        echo "$name: FALHOU" >> "$REPORT_DIR/connectivity.log"
        FAILED=$((FAILED + 1))
        return 1
    fi
}

test_service "Produtos" 8001 "/status"
test_service "Pedidos" 8002 "/status"
test_service "Pagamentos" 8003 "/actuator/health"
test_service "Fornecedores" 8004 "/actuator/health"
test_service "Empréstimos" 8005 "/status"
test_service "Câmbio" 8006 "/status"

echo ""
echo "4. EXECUTANDO TESTES UNITÁRIOS"
echo "=========================================="

cd /home/ubuntu/environment/aidev/openfinance/importau

# Produtos (Python)
echo "Testando Produtos..."
docker exec produtos-service pytest /app/test_produtos.py -v > "$REPORT_DIR/test-produtos.log" 2>&1 || echo "✗ Produtos: testes falharam"

# Pedidos (Node.js)
echo "Testando Pedidos..."
docker exec pedidos-service npm test > "$REPORT_DIR/test-pedidos.log" 2>&1 || echo "✗ Pedidos: testes falharam"

# Pagamentos (Java)
echo "Testando Pagamentos..."
docker exec pagamentos-service mvn test > "$REPORT_DIR/test-pagamentos.log" 2>&1 || echo "✗ Pagamentos: testes falharam"

# Fornecedores (Java)
echo "Testando Fornecedores..."
docker exec fornecedores-service mvn test > "$REPORT_DIR/test-fornecedores.log" 2>&1 || echo "✗ Fornecedores: testes falharam"

# Empréstimos (Python)
echo "Testando Empréstimos..."
docker exec emprestimos-service pytest /app/tests/ -v > "$REPORT_DIR/test-emprestimos.log" 2>&1 || echo "✗ Empréstimos: testes falharam"

# Câmbio (Python)
echo "Testando Câmbio..."
docker exec cambio-service pytest /app/tests/ -v > "$REPORT_DIR/test-cambio.log" 2>&1 || echo "✗ Câmbio: testes falharam"

echo ""
echo "5. EXECUTANDO PIPELINE CI/CD (ACT)"
echo "=========================================="
cd /home/ubuntu/environment/aidev/openfinance/importau
act workflow_dispatch --container-architecture linux/amd64 > "$REPORT_DIR/pipeline-act.log" 2>&1
PIPELINE_RESULT=$?

if [ $PIPELINE_RESULT -eq 0 ]; then
    echo "✓ Pipeline CI/CD: SUCESSO"
else
    echo "✗ Pipeline CI/CD: FALHOU"
    FAILED=$((FAILED + 1))
fi

echo ""
echo "6. GERANDO RELATÓRIO FINAL"
echo "=========================================="
cat > "$REPORT_DIR/REPORT.md" << EOF
# Relatório de Validação do Pipeline - IMPORTAÚ
**Data/Hora:** $(date '+%Y-%m-%d %H:%M:%S')
**IP Público:** $PUBLIC_IP

## Conectividade dos Microserviços
\`\`\`
$(cat "$REPORT_DIR/connectivity.log")
\`\`\`

## URLs de Acesso (Swagger)
- Produtos: http://$PUBLIC_IP:8001/docs
- Pedidos: http://$PUBLIC_IP:8002/api-docs
- Pagamentos: http://$PUBLIC_IP:8003/swagger-ui.html
- Fornecedores: http://$PUBLIC_IP:8004/swagger-ui.html
- Empréstimos: http://$PUBLIC_IP:8005/docs
- Câmbio: http://$PUBLIC_IP:8006/docs

## Status dos Testes
- Testes Unitários: Ver logs individuais
- Pipeline CI/CD (ACT): $([ $PIPELINE_RESULT -eq 0 ] && echo "✓ SUCESSO" || echo "✗ FALHOU")

## Logs Completos
- Conectividade: connectivity.log
- Produtos: test-produtos.log
- Pedidos: test-pedidos.log
- Pagamentos: test-pagamentos.log
- Fornecedores: test-fornecedores.log
- Empréstimos: test-emprestimos.log
- Câmbio: test-cambio.log
- Pipeline CI/CD: pipeline-act.log
EOF

echo ""
echo "=========================================="
echo "PIPELINE CONCLUÍDO"
echo "=========================================="
echo "Relatório salvo em: $REPORT_DIR/REPORT.md"
echo ""

if [ $FAILED -gt 0 ]; then
    echo "✗ Pipeline FALHOU com $FAILED erro(s)"
    exit 1
else
    echo "✓ Pipeline CONCLUÍDO COM SUCESSO"
    exit 0
fi
