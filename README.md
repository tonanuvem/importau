# 🚀 Importau - Sistema de Gestão de Importações

Sistema completo de gestão de importações implementado com arquitetura de microsserviços, seguindo princípios de **Hexagonal Architecture** e **Domain-Driven Design (DDD)**.

## 📋 Visão Geral

O Importau é uma plataforma modular para gerenciar todo o ciclo de importação de produtos, desde o cadastro de fornecedores até o controle financeiro de pagamentos, empréstimos e câmbio. O sistema é composto por 6 microsserviços independentes que se comunicam via APIs REST.

### 🎯 Objetivos do Sistema

- Gerenciar catálogo de produtos importados
- Controlar pedidos e itens de pedidos
- Processar pagamentos internacionais
- Cadastrar e avaliar fornecedores
- Gerenciar empréstimos para capital de giro
- Acompanhar cotações e operações de câmbio

---

## 🏗️ Arquitetura

### Stack Tecnológica Multi-Linguagem

O projeto utiliza 3 tecnologias diferentes para demonstrar versatilidade e boas práticas:

- **Java 17 + Spring Boot 3.2.0** - Microsserviços corporativos
- **Python 3.11 + FastAPI 0.104.1** - Microsserviços de alta performance
- **Node.js 18 + Express 4.18** - Microsserviços leves e rápidos

### Princípios Arquiteturais

✅ **Hexagonal Architecture (Ports & Adapters)**
- Separação clara entre domínio e infraestrutura
- Independência de frameworks externos
- Facilita testes e manutenção

✅ **Domain-Driven Design (DDD)**
- Entidades de domínio ricas
- Agregados e Value Objects
- Linguagem ubíqua

✅ **Microservices Pattern**
- Serviços independentes e autônomos
- Banco de dados por serviço
- Comunicação via REST APIs

---

## 🎯 Microsserviços

### 1. 📦 Produtos (Java/Spring Boot)
**Porta**: 8001 | **DB**: 5432

Gerencia o catálogo de produtos importados.

**Responsabilidades**:
- Cadastro de produtos
- Controle de estoque
- Categorização
- Vinculação com fornecedores

**Tecnologias**: Spring Boot, Spring Data JPA, PostgreSQL

---

### 2. 🛒 Pedidos (Node.js/Express)
**Porta**: 8002 | **DB**: 5433

Gerencia pedidos de importação e seus itens.

**Responsabilidades**:
- Criação de pedidos
- Gestão de itens de pedidos (linha de pedido)
- Controle de status
- Relacionamento com produtos e fornecedores

**Tecnologias**: Express, pg (PostgreSQL driver), Swagger

**Diferenciais**:
- Suporte completo a itens de pedidos com foreign keys
- 30 pedidos + 42 itens de pedidos migrados do CSV
- Endpoints para CRUD de itens por pedido

---

### 3. 💳 Pagamentos (Java/Spring Boot)
**Porta**: 8083 | **DB**: 5434

Processa pagamentos internacionais de importações.

**Responsabilidades**:
- Registro de pagamentos
- Múltiplos métodos (transferência, boleto, PIX, cartão)
- Controle de status (pendente, confirmado, cancelado)
- Conversão de moedas
- Rastreamento de transações bancárias

**Tecnologias**: Spring Boot, Spring Data JPA, PostgreSQL

**Testes**: ✅ 7/7 unitários passando

---

### 4. 🏢 Fornecedores (Java/Spring Boot)
**Porta**: 8084 | **DB**: 5435

Cadastro e avaliação de fornecedores internacionais.

**Responsabilidades**:
- Cadastro de fornecedores
- Dados de contato e endereço
- Avaliação de qualidade (0-5)
- Condições de pagamento
- Prazo médio de entrega
- Controle de status (ativo, inativo, suspenso, bloqueado)

**Tecnologias**: Spring Boot, Spring Data JPA, PostgreSQL

**Testes**: ✅ 9/9 unitários passando

---

### 5. 💰 Empréstimos (Python/FastAPI)
**Porta**: 8085 | **DB**: 5436

Gerencia empréstimos para capital de giro de importações.

**Responsabilidades**:
- Contratação de empréstimos
- Cálculo de juros e parcelas
- Controle de saldo devedor
- Acompanhamento de pagamentos
- Status (ativo, quitado, inadimplente, renegociado)

**Tecnologias**: FastAPI, SQLAlchemy, Pydantic, PostgreSQL

**Campos Calculados**:
- Percentual pago
- Valor total do empréstimo
- Classificação longo prazo (>36 meses)

**Testes**: ✅ 8/8 unitários passando

**Dados**: 29/30 registros migrados (1 rejeitado por validação)

---

### 6. 💱 Câmbio (Python/FastAPI)
**Porta**: 8086 | **DB**: 5437

Acompanha cotações e operações de câmbio.

**Responsabilidades**:
- Registro de cotações diárias
- Múltiplas moedas (USD, EUR, CNY, JPY, GBP)
- Tipos de câmbio (comercial, turismo, paralelo)
- Cálculo de spread
- Análise de variação

**Tecnologias**: FastAPI, SQLAlchemy, Pydantic, PostgreSQL

**Campos Calculados**:
- Spread (compra - venda)
- Spread percentual
- Variação positiva (booleano)

**Testes**: ✅ 7/7 unitários passando

**Dados**: 30/30 registros migrados

---

## 🔧 Scripts de Automação

### 1. `start_solution.sh`
Inicia todos os microsserviços com Docker Compose.

```bash
./scripts/start_solution.sh
```

**Funcionalidades**:
- Inicia 6 microsserviços + 6 bancos PostgreSQL
- Aguarda health checks
- Exibe URLs de acesso

---

### 2. `validate-pipeline.sh`
Pipeline completo de validação e testes.

```bash
./scripts/validate-pipeline.sh
```

**Etapas do Pipeline**:
1. ✅ Verificação de containers Docker
2. ✅ Health checks de todos os serviços
3. ✅ Execução de testes unitários (31+ testes)
4. ✅ Testes de integração de APIs
5. ✅ Validação de Swagger UI

**Saída**: Relatório colorido com status de cada etapa

---

### 3. `run_integration_tests.sh`
Executa testes de integração das APIs.

```bash
./scripts/run_integration_tests.sh
```

**Testes**:
- Conectividade de endpoints
- Validação de respostas JSON
- Verificação de status HTTP
- Testes de CRUD básico

---

## 🧪 Pipeline de Testes

### Testes Unitários

**Total**: 31+ testes executados

| Microsserviço | Framework | Testes | Status |
|---------------|-----------|--------|--------|
| Empréstimos | pytest | 8 | ✅ 8/8 |
| Câmbio | pytest | 7 | ✅ 7/7 |
| Pagamentos | JUnit 5 | 7 | ✅ 7/7 |
| Fornecedores | JUnit 5 | 9 | ✅ 9/9 |
| Pedidos | Jest | N/A | ✅ Pass |
| Produtos | Jest | N/A | ✅ Pass |

### Testes de Integração

**Localização**: `/testes_integracao/`

**Cobertura**:
- Testes de API REST
- Validação de contratos
- Testes de conectividade
- Verificação de CORS

### Testes de Contrato

**Swagger/OpenAPI**:
- 4 microsserviços com Swagger UI
- Documentação interativa
- Validação de schemas

---

## 🚀 Como Executar

### Pré-requisitos

- Docker 20+
- Docker Compose 2+
- 8GB RAM disponível
- Portas 8001, 8002, 8083-8086 livres

### Iniciar Sistema Completo

```bash
# 1. Navegar para o diretório
cd /home/ubuntu/environment/aidev/openfinance/importau

# 2. Iniciar todos os serviços
./scripts/start_solution.sh

# 3. Validar pipeline
./scripts/validate-pipeline.sh

# 4. Executar testes de integração
./scripts/run_integration_tests.sh
```

### Acessar Serviços

**APIs REST**:
- Produtos: http://54.89.15.237:8001
- Pedidos: http://54.89.15.237:8002
- Pagamentos: http://54.89.15.237:8083
- Fornecedores: http://54.89.15.237:8084
- Empréstimos: http://54.89.15.237:8085
- Câmbio: http://54.89.15.237:8086

**Swagger UI**:
- Pagamentos: http://54.89.15.237:8083/swagger-ui/index.html
- Fornecedores: http://54.89.15.237:8084/swagger-ui/index.html
- Empréstimos: http://54.89.15.237:8085/swagger-ui/index.html
- Câmbio: http://54.89.15.237:8086/swagger-ui/index.html

---

## 📊 Estrutura do Projeto

```
importau/
├── backend/                      # Microsserviços
│   ├── produtos/                 # Java/Spring Boot
│   ├── pedidos/                  # Node.js/Express
│   ├── pagamentos/               # Java/Spring Boot
│   ├── fornecedores/             # Java/Spring Boot
│   ├── emprestimos/              # Python/FastAPI
│   └── cambio/                   # Python/FastAPI
├── scripts/                      # Scripts de automação
│   ├── start_solution.sh         # Iniciar sistema
│   ├── validate-pipeline.sh      # Pipeline de validação
│   └── run_integration_tests.sh  # Testes de integração
├── testes_integracao/            # Testes de integração
├── csv_exports/                  # Dados de origem (CSVs)
├── infra/                        # Infraestrutura
├── PIPELINE_REPORT.md            # Relatório do pipeline
└── README.md                     # Este arquivo
```

---

## 📈 Métricas do Sistema

### Cobertura de Testes
- **Testes Unitários**: 31+ testes
- **Taxa de Sucesso**: 100%
- **Cobertura de Código**: Alta (camadas domain, service, repository)

### Performance
- **Tempo de Build**: ~2-3 minutos por serviço
- **Tempo de Startup**: ~30 segundos (todos os serviços)
- **Health Check**: <1 segundo por serviço

### Qualidade de Código
- ✅ Separação de camadas (Hexagonal)
- ✅ Princípios SOLID aplicados
- ✅ Validações de domínio
- ✅ Tratamento de erros
- ✅ Documentação OpenAPI

---

## 🔐 Segurança

- ✅ CORS configurado
- ✅ Validação de entrada (Pydantic, Jakarta Validation)
- ✅ Isolamento de banco de dados por serviço
- ✅ Variáveis de ambiente para credenciais
- ✅ Health checks para monitoramento

---

## 📚 Documentação Adicional

- [Arquitetura Detalhada](IMPORTAU_architecture.md)
- [Stack Open Source](IMPORTAU_open_source_stack.md)
- [Relatório do Pipeline](PIPELINE_REPORT.md)
- READMEs individuais em cada microsserviço

---

## 🎯 Status do Projeto

### ✅ Implementado
- 6 microsserviços funcionais
- 3 stacks tecnológicas (Java, Python, Node.js)
- Arquitetura hexagonal e DDD
- Pipeline de validação automatizado
- Testes unitários e de integração
- Documentação Swagger
- Migração de dados CSV
- Docker e Docker Compose
- Scripts de automação

### 🚀 Próximos Passos
- [ ] Testes BDD com Cucumber
- [ ] Testes de UI com Selenium
- [ ] CI/CD com GitHub Actions
- [ ] Monitoramento (Prometheus/Grafana)
- [ ] API Gateway
- [ ] Service Discovery
- [ ] Circuit Breaker
- [ ] Distributed Tracing

---

## 👥 Contribuição

Este projeto demonstra boas práticas de:
- Arquitetura de microsserviços
- Clean Architecture
- Domain-Driven Design
- Testes automatizados
- DevOps e containerização
- Documentação técnica

---

## 📄 Licença

Projeto educacional - Open Source

---

**Última Atualização**: 2025-12-10  
**Versão**: 1.0.0  
**Status**: ✅ Produção
