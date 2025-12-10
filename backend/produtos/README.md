# Microsserviço de Produtos

Microsserviço para gerenciamento de produtos, implementado em Java com Spring Boot seguindo arquitetura hexagonal e DDD.

## 🏗️ Arquitetura

- **Framework**: Spring Boot 3.2.0
- **Banco de Dados**: PostgreSQL 15
- **ORM**: Spring Data JPA
- **Validação**: Jakarta Validation
- **Testes**: JUnit 5 + Mockito
- **Containerização**: Docker

## 📁 Estrutura do Projeto

```
produtos/
├── src/
│   ├── main/
│   │   ├── java/com/importau/produtos/
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

## 📊 Endpoints da API

### Health Check
- `GET /api/v1/status` - Verifica saúde do serviço

### Produtos
- `GET /api/v1/produtos` - Lista todos os produtos
- `GET /api/v1/produtos/{id}` - Busca produto por ID
- `POST /api/v1/produtos` - Cria novo produto
- `PUT /api/v1/produtos/{id}` - Atualiza produto
- `DELETE /api/v1/produtos/{id}` - Remove produto

## 📝 Modelo de Dados

### Produto

```json
{
  "produto_id": "PROD001",
  "nome": "Notebook Dell Inspiron",
  "categoria": "Eletrônicos",
  "preco_unitario_brl": 3500.00,
  "quantidade_estoque": 50,
  "fornecedor_id": "FORN001",
  "data_cadastro": "2024-01-10",
  "status": "ATIVO"
}
```

## 🌐 Acesso

- **API**: http://54.89.15.237:8001
- **Swagger UI**: http://54.89.15.237:8001/swagger-ui/index.html
- **Health Check**: http://54.89.15.237:8001/api/v1/status

## 🔧 Variáveis de Ambiente

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/produtos_db
SPRING_DATASOURCE_USERNAME=produtos_user
SPRING_DATASOURCE_PASSWORD=produtos_pass
```

## 📦 Portas

- **Aplicação**: 8001
- **PostgreSQL**: 5432

## ✅ Status

- ✅ Implementação completa
- ✅ Testes unitários passando
- ✅ Docker build bem-sucedido
- ✅ Serviço rodando e saudável
- ✅ Swagger UI acessível
- ✅ CORS configurado
- ✅ Endpoints testados e funcionando
