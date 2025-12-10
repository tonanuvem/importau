# Microsserviço de Fornecedores

Microsserviço para gerenciamento de fornecedores, implementado em Java com Spring Boot seguindo arquitetura hexagonal e DDD.

## 🏗️ Arquitetura

- **Framework**: Spring Boot 3.2.0
- **Banco de Dados**: PostgreSQL 15
- **ORM**: Spring Data JPA
- **Validação**: Jakarta Validation
- **Testes**: JUnit 5 + Mockito
- **Containerização**: Docker

## 📁 Estrutura do Projeto

```
fornecedores/
├── src/
│   ├── main/
│   │   ├── java/com/importau/fornecedores/
│   │   │   ├── domain/          # Entidades de domínio
│   │   │   ├── repository/      # Camada de persistência
│   │   │   ├── service/         # Lógica de negócio
│   │   │   └── controller/      # Endpoints REST
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Testes unitários
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

## 🚀 Como Executar

### Com Docker Compose

```bash
docker-compose up -d
```

### Executar Testes

```bash
mvn test
```

### Migrar Dados do CSV

```bash
java -jar migrate-data.jar
```

## 📊 Endpoints da API

### Health Check
- `GET /api/v1/status` - Verifica saúde do serviço

### Fornecedores
- `GET /api/v1/fornecedores` - Lista todos os fornecedores
- `GET /api/v1/fornecedores/{id}` - Busca fornecedor por ID
- `GET /api/v1/fornecedores/pais/{pais}` - Busca por país
- `GET /api/v1/fornecedores/status/{status}` - Busca por status
- `POST /api/v1/fornecedores` - Cria novo fornecedor
- `PUT /api/v1/fornecedores/{id}` - Atualiza fornecedor
- `DELETE /api/v1/fornecedores/{id}` - Remove fornecedor

## 📝 Modelo de Dados

### Fornecedor

```json
{
  "fornecedor_id": "FORN001",
  "nome_empresa": "Tech Supplies Inc",
  "pais_origem": "Estados Unidos",
  "contato_principal": "John Smith",
  "email": "john.smith@techsupplies.com",
  "telefone": "+1-555-0123",
  "endereco": "123 Tech Street, Silicon Valley, CA",
  "cnpj_equivalente": "12.345.678/0001-90",
  "tipo_produto_fornecido": "Eletrônicos",
  "condicoes_pagamento": "30/60/90 dias",
  "prazo_entrega_medio_dias": 45,
  "avaliacao_qualidade": 4.5,
  "status": "ATIVO",
  "data_cadastro": "2024-01-05",
  "observacoes": "Fornecedor premium"
}
```

### Status Possíveis
- `ATIVO` - Fornecedor ativo
- `INATIVO` - Fornecedor inativo
- `SUSPENSO` - Fornecedor temporariamente suspenso
- `BLOQUEADO` - Fornecedor bloqueado

## 🧪 Testes

O projeto possui 9 testes unitários cobrindo:
- Listagem de fornecedores
- Busca por ID
- Busca por país
- Busca por status
- Criação com validações
- Validação de email
- Validação de avaliação
- Atualização
- Remoção

**Resultado**: ✅ 9/9 testes passando

## 🌐 Acesso

- **API**: http://54.89.15.237:8084
- **Swagger UI**: http://54.89.15.237:8084/swagger-ui/index.html
- **Health Check**: http://54.89.15.237:8084/api/v1/status

## 🔧 Variáveis de Ambiente

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5435/fornecedores_db
SPRING_DATASOURCE_USERNAME=fornecedores_user
SPRING_DATASOURCE_PASSWORD=fornecedores_pass
```

## 📦 Portas

- **Aplicação**: 8084
- **PostgreSQL**: 5435

## ✅ Status

- ✅ Implementação completa
- ✅ Testes unitários (9/9 passando)
- ✅ Docker build bem-sucedido
- ✅ Serviço rodando e saudável
- ✅ Dados migrados do CSV (30/30 registros)
- ✅ Swagger UI acessível
- ✅ CORS configurado
- ✅ Endpoints testados e funcionando
