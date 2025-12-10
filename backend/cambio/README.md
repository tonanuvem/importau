# Microsserviço de Câmbio

Microsserviço para gerenciamento de cotações de câmbio, implementado em Python com FastAPI seguindo arquitetura hexagonal e DDD.

## 🏗️ Arquitetura

- **Framework**: FastAPI 0.104.1
- **Banco de Dados**: PostgreSQL 15
- **ORM**: SQLAlchemy 2.0.23
- **Validação**: Pydantic 2.5.0
- **Testes**: Pytest 7.4.3
- **Containerização**: Docker

## 📁 Estrutura do Projeto

```
cambio/
├── app/
│   ├── domain/          # Entidades de domínio
│   ├── repository/      # Camada de persistência
│   ├── service/         # Lógica de negócio
│   ├── controller/      # Endpoints REST
│   └── main.py          # Aplicação FastAPI
├── tests/               # Testes unitários
├── Dockerfile
├── docker-compose.yml
└── requirements.txt
```

## 🚀 Como Executar

### Com Docker Compose

```bash
docker-compose up -d
```

### Executar Testes

```bash
docker run --rm -e PYTHONPATH=/app cambio-service:1.0.0 pytest tests/ -v
```

### Migrar Dados do CSV

```bash
python3 migrate_data.py
```

## 📊 Endpoints da API

### Health Check
- `GET /api/v1/status` - Verifica saúde do serviço

### Cotações de Câmbio
- `GET /api/v1/cambio` - Lista todas as cotações
- `GET /api/v1/cambio/{id}` - Busca cotação por ID
- `GET /api/v1/cambio/moeda/{moeda}` - Busca cotações por moeda
- `GET /api/v1/cambio/moeda/{moeda}/ultima` - Última cotação de uma moeda
- `GET /api/v1/cambio/data/{data}` - Busca cotações por data
- `POST /api/v1/cambio` - Cria nova cotação
- `PUT /api/v1/cambio/{id}` - Atualiza cotação
- `DELETE /api/v1/cambio/{id}` - Remove cotação

## 📝 Modelo de Dados

### Cambio

```python
{
  "cambio_id": 1,
  "data_cotacao": "2024-10-15",
  "moeda": "USD",
  "taxa_compra": 5.2350,
  "taxa_venda": 5.2580,
  "taxa_ptax": 5.2465,
  "variacao_dia_percentual": 0.15,
  "fonte": "SISBACEN",
  "tipo_cambio": "COMERCIAL",
  "hora_atualizacao": "16:30:00"
}
```

### Tipos de Câmbio
- `COMERCIAL` - Câmbio comercial
- `TURISMO` - Câmbio turismo
- `PARALELO` - Câmbio paralelo

## 🔍 Campos Calculados

- **spread**: Diferença entre taxa de venda e compra
- **spread_percentual**: Spread em percentual
- **variacao_positiva**: Indica se variação do dia é positiva

## 🧪 Testes

O projeto possui 7 testes unitários cobrindo:
- Listagem de cotações
- Busca por moeda
- Criação com validações
- Validação de taxas
- Cálculos de spread
- Identificação de variação

**Resultado**: ✅ 7/7 testes passando

## 🌐 Acesso

- **API**: http://54.89.15.237:8086
- **Swagger UI**: http://54.89.15.237:8086/swagger-ui/index.html
- **Health Check**: http://54.89.15.237:8086/api/v1/status

## 🔧 Variáveis de Ambiente

```env
DATABASE_URL=postgresql://cambio_user:cambio_pass@localhost:5437/cambio_db
APP_NAME=cambio-service
APP_VERSION=1.0.0
```

## 📦 Portas

- **Aplicação**: 8086
- **PostgreSQL**: 5437

## ✅ Status

- ✅ Implementação completa
- ✅ Testes unitários (7/7 passando)
- ✅ Docker build bem-sucedido
- ✅ Serviço rodando e saudável
- ✅ Dados migrados do CSV (30/30 registros)
- ✅ Swagger UI acessível
- ✅ CORS configurado
- ✅ Endpoints testados e funcionando
