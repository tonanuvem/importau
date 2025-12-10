# Microsserviço de Pedidos

Microsserviço para gerenciamento de pedidos e itens de pedidos, implementado em Node.js/Express seguindo arquitetura hexagonal e DDD.

## 🏗️ Arquitetura

- **Framework**: Express.js
- **Banco de Dados**: PostgreSQL 15
- **Testes**: Jest
- **Containerização**: Docker

## 📁 Estrutura do Projeto

```
pedidos/
├── server.js            # Aplicação principal
├── pedidos.test.js      # Testes unitários
├── Dockerfile
└── package.json
```

## 🚀 Como Executar

### Com Docker

```bash
docker build -t pedidos-service .
docker run -d -p 8002:8002 pedidos-service
```

### Executar Testes

```bash
npm test
```

## 📊 Endpoints da API

### Health Check
- `GET /status` - Verifica saúde do serviço

### Pedidos
- `GET /pedidos` - Lista todos os pedidos
- `GET /pedidos/:id` - Busca pedido por ID
- `POST /pedidos` - Cria novo pedido
- `PUT /pedidos/:id` - Atualiza pedido
- `DELETE /pedidos/:id` - Remove pedido
- `GET /pedidos/stats/resumo` - Estatísticas dos pedidos

### Itens de Pedidos
- `GET /pedidos/:pedidoId/itens` - Lista itens de um pedido
- `GET /itens/:id` - Busca item por ID
- `POST /itens` - Cria novo item de pedido
- `DELETE /itens/:id` - Remove item de pedido

## 📝 Modelo de Dados

### Pedido

```json
{
  "pedido_id": "PED001",
  "data_pedido": "2024-10-15",
  "fornecedor_id": "FORN001",
  "valor_total_brl": 15750.50,
  "status": "ENTREGUE",
  "tipo_pagamento": "BOLETO",
  "prazo_dias": 30,
  "data_entrega_prevista": "2024-11-14",
  "usuario_criacao": "matheus.silva",
  "observacoes": "Pedido urgente"
}
```

### Item de Pedido

```json
{
  "item_id": "ITEM001",
  "pedido_id": "PED001",
  "produto_id": "PROD001",
  "quantidade": 5,
  "preco_unitario": 2500.00,
  "desconto_percentual": 2.0,
  "valor_total_item": 12250.00,
  "data_inclusao": "2024-10-15",
  "observacoes": ""
}
```

### Status de Pedido
- `PENDENTE` - Pedido criado
- `EM_TRANSITO` - Pedido em transporte
- `ENTREGUE` - Pedido entregue
- `CANCELADO` - Pedido cancelado

## 🧪 Testes

O projeto possui testes unitários cobrindo:
- Criação de pedidos
- Listagem e filtros
- Validações de negócio
- Operações CRUD

## 🌐 Acesso

- **API**: http://54.89.15.237:8002
- **Swagger UI**: http://54.89.15.237:8002/docs
- **Health Check**: http://54.89.15.237:8002/status

## 🔧 Variáveis de Ambiente

```env
PORT=8002
HOST=0.0.0.0
DATABASE_URL=postgresql://postgres:postgres@localhost:5433/pedidos_db
```

## 📦 Portas

- **Aplicação**: 8002
- **PostgreSQL**: 5433

## ✅ Status

- ✅ Implementação completa de Pedidos
- ✅ Implementação completa de Itens de Pedidos
- ✅ Relacionamento entre Pedidos e Itens (Foreign Key)
- ✅ Testes unitários
- ✅ Docker build bem-sucedido
- ✅ Serviço rodando e saudável
- ✅ Dados migrados dos CSVs:
  - 30 pedidos carregados
  - 42 itens de pedidos carregados
- ✅ Swagger UI acessível
- ✅ CORS configurado
- ✅ Endpoints testados e funcionando

## 🔗 Relacionamentos

- Cada pedido pode ter múltiplos itens
- Itens são vinculados a pedidos via `pedido_id`
- Exclusão de pedido remove automaticamente seus itens (CASCADE)
