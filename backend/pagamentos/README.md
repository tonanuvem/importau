# Microsserviço de Pagamentos

Microsserviço para gerenciamento de pagamentos, implementado em Java com Spring Boot seguindo arquitetura hexagonal e DDD.

## 🏗️ Arquitetura

- **Framework**: Spring Boot 3.2.0
- **Banco de Dados**: PostgreSQL 15
- **ORM**: Spring Data JPA
- **Validação**: Jakarta Validation
- **Testes**: JUnit 5 + Mockito
- **Containerização**: Docker

## 📁 Estrutura do Projeto

```
pagamentos/
├── src/
│   ├── main/
│   │   ├── java/com/importau/pagamentos/
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

### Pagamentos
- `GET /api/v1/pagamentos` - Lista todos os pagamentos
- `GET /api/v1/pagamentos/{id}` - Busca pagamento por ID
- `GET /api/v1/pagamentos/pedido/{pedidoId}` - Busca por pedido
- `GET /api/v1/pagamentos/status/{status}` - Busca por status
- `POST /api/v1/pagamentos` - Cria novo pagamento
- `PUT /api/v1/pagamentos/{id}` - Atualiza pagamento
- `DELETE /api/v1/pagamentos/{id}` - Remove pagamento

## 📝 Modelo de Dados

### Pagamento

```json
{
  "pagamento_id": "PAG001",
  "pedido_id": "PED001",
  "data_pagamento": "2024-01-20",
  "valor_pago_brl": 15000.00,
  "metodo_pagamento": "TRANSFERENCIA_BANCARIA",
  "status_pagamento": "CONFIRMADO",
  "moeda_origem": "USD",
  "taxa_cambio_aplicada": 5.25,
  "valor_original_moeda": 2857.14,
  "banco_origem": "Banco Internacional",
  "banco_destino": "Banco Nacional",
  "numero_transacao": "TRX20240120001",
  "observacoes": "Pagamento referente importação"
}
```

### Métodos de Pagamento
- `TRANSFERENCIA_BANCARIA`
- `BOLETO`
- `CARTAO_CREDITO`
- `PIX`
- `CHEQUE`

### Status de Pagamento
- `PENDENTE`
- `CONFIRMADO`
- `CANCELADO`
- `ESTORNADO`

## 🧪 Testes

O projeto possui 7 testes unitários cobrindo:
- Listagem de pagamentos
- Busca por ID
- Busca por pedido
- Busca por status
- Criação com validações
- Atualização
- Remoção

**Resultado**: ✅ 7/7 testes passando

## 🌐 Acesso

- **API**: http://54.89.15.237:8083
- **Swagger UI**: http://54.89.15.237:8083/swagger-ui/index.html
- **Health Check**: http://54.89.15.237:8083/api/v1/status

## 🔧 Variáveis de Ambiente

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5434/pagamentos_db
SPRING_DATASOURCE_USERNAME=pagamentos_user
SPRING_DATASOURCE_PASSWORD=pagamentos_pass
```

## 📦 Portas

- **Aplicação**: 8083
- **PostgreSQL**: 5434

## ✅ Status

- ✅ Implementação completa
- ✅ Testes unitários (7/7 passando)
- ✅ Docker build bem-sucedido
- ✅ Serviço rodando e saudável
- ✅ Dados migrados do CSV (30/30 registros)
- ✅ Swagger UI acessível
- ✅ CORS configurado
- ✅ Endpoints testados e funcionando
