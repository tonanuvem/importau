# Microsserviço de Empréstimos

Microsserviço para gerenciamento de empréstimos financeiros, implementado em Python com FastAPI seguindo arquitetura hexagonal e DDD.

## 🏗️ Arquitetura

- **Framework**: FastAPI 0.104.1
- **Banco de Dados**: PostgreSQL 15
- **ORM**: SQLAlchemy 2.0.23
- **Validação**: Pydantic 2.5.0
- **Testes**: Pytest 7.4.3
- **Containerização**: Docker

## 📁 Estrutura do Projeto

```
emprestimos/
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
docker run --rm -e PYTHONPATH=/app emprestimos-service:1.0.0 pytest tests/ -v
```

### Migrar Dados do CSV

```bash
python3 migrate_data.py
```

## 📊 Endpoints da API

### Health Check
- `GET /api/v1/status` - Verifica saúde do serviço

### Empréstimos
- `GET /api/v1/emprestimos` - Lista todos os empréstimos
- `GET /api/v1/emprestimos/{id}` - Busca empréstimo por ID
- `GET /api/v1/emprestimos/status/{status}` - Busca por status
- `GET /api/v1/emprestimos/instituicao/{nome}` - Busca por instituição
- `POST /api/v1/emprestimos` - Cria novo empréstimo
- `PUT /api/v1/emprestimos/{id}` - Atualiza empréstimo
- `DELETE /api/v1/emprestimos/{id}` - Remove empréstimo

## 📝 Modelo de Dados

### Emprestimo

```python
{
  "emprestimo_id": "EMP001",
  "data_contratacao": "2024-01-15",
  "instituicao_financeira": "Banco Nacional",
  "valor_principal_brl": 500000.00,
  "taxa_juros_anual": 12.5,
  "prazo_meses": 36,
  "valor_parcela_mensal": 17850.25,
  "saldo_devedor": 425000.00,
  "status": "ATIVO",
  "finalidade": "Capital de Giro",
  "data_vencimento_proxima": "2024-11-15",
  "num_parcelas_pagas": 10,
  "usuario_responsavel": "celso.oliveira"
}
```

### Status Possíveis
- `ATIVO` - Empréstimo em andamento
- `QUITADO` - Empréstimo totalmente pago
- `INADIMPLENTE` - Empréstimo com pagamentos atrasados
- `RENEGOCIADO` - Empréstimo renegociado

## 🔍 Campos Calculados

- **percentual_pago**: Percentual de parcelas pagas
- **valor_total_emprestimo**: Valor total com juros
- **emprestimo_longo_prazo**: Indica se prazo > 36 meses

## 🧪 Testes

O projeto possui 8 testes unitários cobrindo:
- Listagem de empréstimos
- Busca por ID
- Criação com validações
- Validação de saldo devedor
- Validação de parcelas pagas
- Cálculos de campos derivados

**Resultado**: ✅ 8/8 testes passando

## 🌐 Acesso

- **API**: http://54.89.15.237:8085
- **Swagger UI**: http://54.89.15.237:8085/swagger-ui/index.html
- **Health Check**: http://54.89.15.237:8085/api/v1/status

## 🔧 Variáveis de Ambiente

```env
DATABASE_URL=postgresql://emprestimos_user:emprestimos_pass@localhost:5436/emprestimos_db
APP_NAME=emprestimos-service
APP_VERSION=1.0.0
```

## 📦 Portas

- **Aplicação**: 8085
- **PostgreSQL**: 5436

## ✅ Status

- ✅ Implementação completa
- ✅ Testes unitários (8/8 passando)
- ✅ Docker build bem-sucedido
- ✅ Serviço rodando e saudável
- ✅ Dados migrados do CSV (29/30 registros)
- ✅ Swagger UI acessível
- ✅ CORS configurado
- ✅ Endpoints testados e funcionando
