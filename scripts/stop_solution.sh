#!/bin/bash

echo "=== Parando Solução IMPORTAÚ Open Finance ==="

cd infra/docker-compose/singlenode

docker-compose down

echo "=== Solução parada com sucesso! ==="
