#!/bin/bash

echo "=== Parando Solução IMPORTAÚ Open Finance ==="

cd infra/docker-compose/singlenode

docker-compose down
docker-compose rm

echo "=== Solução parada com sucesso! ==="
