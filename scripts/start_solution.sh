#!/bin/bash

echo "=== Iniciando Solução IMPORTAÚ Open Finance ==="

cd infra/docker-compose/singlenode

echo "Parando containers existentes..."
docker-compose down

echo "Construindo e iniciando serviços..."
docker-compose up --build -d

echo "Aguardando serviços ficarem prontos..."
sleep 30

echo "Verificando status dos serviços..."
docker-compose ps

PUBLIC_IP=$(curl -s checkip.amazonaws.com)

echo ""
echo "=== Testando conectividade dos serviços ==="
curl -f http://localhost:8001/status && echo "✓ Produtos OK" || echo "✗ Produtos FALHOU"
curl -f http://localhost:8002/status && echo "✓ Pedidos OK" || echo "✗ Pedidos FALHOU"
curl -f http://localhost:8003/actuator/health && echo "✓ Pagamentos OK" || echo "✗ Pagamentos FALHOU"
curl -f http://localhost:8004/actuator/health && echo "✓ Fornecedores OK" || echo "✗ Fornecedores FALHOU"
curl -f http://localhost:8005/status && echo "✓ Empréstimos OK" || echo "✗ Empréstimos FALHOU"
curl -f http://localhost:8006/status && echo "✓ Câmbio OK" || echo "✗ Câmbio FALHOU"

echo ""
echo "=== Solução iniciada com sucesso! ==="
echo ""
echo "Swagger UI disponível em:"
echo "  Produtos:      http://$PUBLIC_IP:8001/docs"
echo "  Pedidos:       http://$PUBLIC_IP:8002/api-docs"
echo "  Pagamentos:    http://$PUBLIC_IP:8003/swagger-ui.html"
echo "  Fornecedores:  http://$PUBLIC_IP:8004/swagger-ui.html"
echo "  Empréstimos:   http://$PUBLIC_IP:8005/docs"
echo "  Câmbio:        http://$PUBLIC_IP:8006/docs"

