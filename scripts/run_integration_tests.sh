#!/bin/bash

# Script para executar testes de integração BDD
echo "=== Executando Testes de Integração IMPORTAÚ ==="

# Navega para o diretório de testes
cd /home/ubuntu/environment/aidev/openfinance/importau/testes_integracao

# Verifica se os microsserviços estão rodando
echo "Verificando se microsserviços estão disponíveis..."
curl -f http://localhost:8001/status || {
    echo "ERRO: Microsserviço de produtos não está disponível"
    echo "Execute primeiro: ./start_solution.sh"
    exit 1
}

# Executa os testes
echo "Executando testes BDD com Maven..."
mvn clean test

# Verifica resultado
if [ $? -eq 0 ]; then
    echo "=== Testes de integração executados com SUCESSO! ==="
else
    echo "=== FALHA nos testes de integração ==="
    exit 1
fi
