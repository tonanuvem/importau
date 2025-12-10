# Pipeline de Validação - IMPORTAÚ

## Resumo das Melhorias Implementadas

### 1. Features de Teste Criadas
Foram criados arquivos `.feature` para todos os microserviços:
- ✓ `produtos.feature` (já existia)
- ✓ `pedidos.feature` (já existia)
- ✓ `pagamentos.feature` (novo)
- ✓ `fornecedores.feature` (novo)
- ✓ `emprestimos.feature` (novo)
- ✓ `cambio.feature` (novo)

### 2. Script de Validação Completo
Criado `scripts/validate-pipeline.sh` que executa:

#### Etapa 1: Verificação de Dockerfiles
- Valida existência de Dockerfile para cada microserviço

#### Etapa 2: Build e Deploy
- Para containers existentes
- Constrói imagens Docker
- Inicia todos os containers
- Aguarda 60s para inicialização

#### Etapa 3: Testes de Conectividade
Testa endpoints de health check:
- Produtos (8001): `/status`
- Pedidos (8002): `/status`
- Pagamentos (8003): `/actuator/health`
- Fornecedores (8004): `/actuator/health`
- Empréstimos (8005): `/status`
- Câmbio (8006): `/status`

#### Etapa 4: Testes Unitários
Executa testes dentro dos containers:
- **Python** (Produtos, Empréstimos, Câmbio): `pytest`
- **Node.js** (Pedidos): `npm test`
- **Java** (Pagamentos, Fornecedores): `mvn test`

#### Etapa 5: Testes de Integração
- Executa testes Cucumber com Maven
- Valida integração entre microserviços

#### Etapa 6: Relatório
Gera relatório em `pipeline-reports/AAAA_MM_DD_HH_MM/REPORT.md` com:
- Status de conectividade
- URLs do Swagger com IP público
- Logs de todos os testes
- Status final do pipeline

### 3. Estrutura de Microserviços

| Microserviço | Porta | Tecnologia | Banco | Swagger |
|--------------|-------|------------|-------|---------|
| Produtos | 8001 | Python/FastAPI | PostgreSQL (5432) | /docs |
| Pedidos | 8002 | Node.js/Express | PostgreSQL (5433) | /api-docs |
| Pagamentos | 8003 | Java/Spring Boot | PostgreSQL (5434) | /swagger-ui.html |
| Fornecedores | 8004 | Java/Spring Boot | PostgreSQL (5435) | /swagger-ui.html |
| Empréstimos | 8005 | Python/FastAPI | PostgreSQL (5436) | /docs |
| Câmbio | 8006 | Python/FastAPI | PostgreSQL (5437) | /docs |

### 4. Como Executar

```bash
# Executar pipeline completo
cd /home/ubuntu/environment/aidev/openfinance/importau
./scripts/validate-pipeline.sh
```

### 5. Arquivos Criados/Atualizados

```
importau/
├── .gitignore (criado)
├── PIPELINE_VALIDATION.md (este arquivo)
├── scripts/
│   ├── validate-pipeline.sh (atualizado)
│   ├── start_solution.sh (atualizado)
│   ├── stop_solution.sh (criado)
│   └── update-github.sh (atualizado)
├── infra/docker-compose/singlenode/
│   └── docker-compose.yml (atualizado com 6 microserviços)
└── testes_integracao/src/test/resources/features/
    ├── produtos.feature (existente)
    ├── pedidos.feature (existente)
    ├── pagamentos.feature (criado)
    ├── fornecedores.feature (criado)
    ├── emprestimos.feature (criado)
    └── cambio.feature (criado)
```

### 6. Compatibilidade

O pipeline está preparado para:
- ✓ Execução local via script bash
- ✓ Integração com GitHub Actions
- ✓ Integração com AWS CodeBuild
- ⚠ Selenium UI tests (próxima etapa)
