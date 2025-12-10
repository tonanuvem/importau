#!/bin/bash

echo "=== Parando Solução IMPORTAÚ Open Finance ==="

cd /home/ubuntu/environment/aidev/openfinance/importau/infra/docker-compose/singlenode

docker-compose down

echo "=== Solução parada com sucesso! ==="
