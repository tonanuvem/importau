#!/bin/bash

echo "=== Iniciando IMPORTAÚ Open Finance no Docker Swarm ==="

# Verificar se o swarm está inicializado
if ! docker info | grep -q "Swarm: active"; then
    echo "Inicializando Docker Swarm..."
    docker swarm init
fi

# Navegar para o diretório do swarm
cd infra/docker-compose/swarm

# Construir e fazer push das imagens (se necessário)
echo "Construindo imagens dos serviços..."
docker build -t importau/produtos-service:latest ../../../backend/produtos
docker build -t importau/pedidos-service:latest ../../../backend/pedidos
docker build -t importau/pagamentos-service:latest ../../../backend/pagamentos
docker build -t importau/fornecedores-service:latest ../../../backend/fornecedores
docker build -t importau/emprestimos-service:latest ../../../backend/emprestimos
docker build -t importau/cambio-service:latest ../../../backend/cambio

# Remover stack existente (se houver)
echo "Removendo stack existente..."
docker stack rm importau 2>/dev/null || true

# Aguardar remoção completa
echo "Aguardando remoção completa..."
sleep 10

# Deploy da nova stack
echo "Fazendo deploy da stack IMPORTAÚ..."
docker stack deploy -c docker-compose.yml importau

# Aguardar serviços ficarem prontos
echo "Aguardando serviços ficarem prontos..."
sleep 30

# Verificar status dos serviços
echo ""
echo "=== Status dos Serviços ==="
docker service ls

echo ""
echo "=== Detalhes dos Serviços ==="
docker stack ps importau

PUBLIC_IP=$(curl -s checkip.amazonaws.com)

echo ""
echo "=== Solução IMPORTAÚ iniciada no Docker Swarm! ==="
echo ""
echo "Swagger UI disponível em:"
echo "  Produtos:      http://$PUBLIC_IP:8001/docs"
echo "  Pedidos:       http://$PUBLIC_IP:8002/api-docs"
echo "  Pagamentos:    http://$PUBLIC_IP:8083/swagger-ui.html"
echo "  Fornecedores:  http://$PUBLIC_IP:8084/swagger-ui.html"
echo "  Empréstimos:   http://$PUBLIC_IP:8085/docs"
echo "  Câmbio:        http://$PUBLIC_IP:8086/docs"

echo ""
echo "Comandos úteis:"
echo "  docker service ls                    # Listar serviços"
echo "  docker stack ps importau             # Status da stack"
echo "  docker service logs <service-name>   # Ver logs"
echo "  docker stack rm importau             # Remover stack"
