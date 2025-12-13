#!/bin/bash

echo "=== Iniciando Solução IMPORTAÚ Open Finance ==="

cd infra/docker-compose/singlenode

echo "Parando containers existentes..."
docker-compose down

echo "Construindo e iniciando serviços..."
docker-compose up --build -d

echo "Aguardando serviços ficarem prontos..."
echo "Aguardando 30 segundos para inicialização dos bancos..."
sleep 30

echo "Aguardando mais 60 segundos para inicialização dos serviços..."
sleep 60

echo "Verificando status dos serviços..."
docker-compose ps

PUBLIC_IP=$(curl -s checkip.amazonaws.com)

echo ""
echo "=== Testando conectividade dos serviços ==="

# Função para testar serviço com retry e diferentes abordagens
test_service_comprehensive() {
    local base_url=$1
    local name=$2
    local port=$3
    local max_attempts=3
    local attempt=1
    
    # Lista de endpoints para testar
    local endpoints=()
    case $name in
        "Produtos"|"Empréstimos"|"Câmbio")
            endpoints=("/status" "/health" "/")
            ;;
        "Pedidos")
            endpoints=("/status" "/health" "/api/health" "/")
            ;;
        "Pagamentos"|"Fornecedores")
            endpoints=("/actuator/health" "/health" "/swagger-ui.html" "/")
            ;;
    esac
    
    for endpoint in "${endpoints[@]}"; do
        echo "Testando $name com endpoint $endpoint..."
        attempt=1
        while [ $attempt -le $max_attempts ]; do
            local url="http://localhost:${port}${endpoint}"
            echo "  Tentativa $attempt/$max_attempts: $url"
            
            if curl -f -s --connect-timeout 15 --max-time 30 "$url" > /dev/null 2>&1; then
                echo "✓ $name OK via $endpoint"
                return 0
            fi
            
            # Se falhou, tentar via IP público também
            local public_url="http://${PUBLIC_IP}:${port}${endpoint}"
            if curl -f -s --connect-timeout 15 --max-time 30 "$public_url" > /dev/null 2>&1; then
                echo "✓ $name OK via IP público $endpoint"
                return 0
            fi
            
            echo "  Tentativa $attempt/$max_attempts falhou, aguardando..."
            sleep 10
            ((attempt++))
        done
    done
    
    echo "✗ $name FALHOU em todos os endpoints testados"
    echo "Verificando logs do $name:"
    service_name=$(echo $name | tr '[:upper:]' '[:lower:]')-service
    docker-compose logs --tail=20 "$service_name" 2>/dev/null || echo "Não foi possível obter logs para $service_name"
    
    # Verificar se o container está rodando
    echo "Status do container:"
    docker-compose ps "$service_name" 2>/dev/null || echo "Container não encontrado"
    
    return 1
}

# Testar todos os serviços
test_service_comprehensive "localhost" "Produtos" "8001"
test_service_comprehensive "localhost" "Pedidos" "8002"
test_service_comprehensive "localhost" "Pagamentos" "8003"
test_service_comprehensive "localhost" "Fornecedores" "8004"
test_service_comprehensive "localhost" "Empréstimos" "8005"
test_service_comprehensive "localhost" "Câmbio" "8006"

echo ""
echo "=== Resumo dos Serviços ==="
docker-compose ps

echo ""
echo "=== Solução iniciada! ==="
echo ""
echo "Swagger UI disponível em:"
echo "  Produtos:      http://$PUBLIC_IP:8001/docs"
echo "  Pedidos:       http://$PUBLIC_IP:8002/api-docs"
echo "  Pagamentos:    http://$PUBLIC_IP:8003/swagger-ui.html"
echo "  Fornecedores:  http://$PUBLIC_IP:8004/swagger-ui.html"
echo "  Empréstimos:   http://$PUBLIC_IP:8005/docs"
echo "  Câmbio:        http://$PUBLIC_IP:8006/docs"

echo ""
echo "Para verificar logs de um serviço específico:"
echo "  docker-compose logs -f <nome-do-serviço>"
echo ""
echo "Para parar todos os serviços:"
echo "  ./scripts/stop_solution.sh"
