#!/bin/bash

# Script de inicialização da solução IMPORTAÚ
echo "=== Iniciando Solução IMPORTAÚ Open Finance ==="

# Navega para o diretório do docker-compose
cd /home/ubuntu/environment/aidev/openfinance/importau/infra/docker-compose/singlenode

# Para containers existentes
echo "Parando containers existentes..."
docker-compose down

# Constrói e inicia os serviços
echo "Construindo e iniciando serviços..."
docker-compose up --build -d

# Aguarda serviços ficarem prontos
echo "Aguardando serviços ficarem prontos..."
sleep 30

# Verifica status dos serviços
echo "Verificando status dos serviços..."
docker-compose ps

# Testa conectividade
echo "Testando conectividade dos serviços..."
curl -f http://localhost:8001/status || echo "Produtos service não está respondendo"

echo "=== Solução iniciada com sucesso! ==="
echo "Swagger UI disponível em: http://localhost:8001/docs"
