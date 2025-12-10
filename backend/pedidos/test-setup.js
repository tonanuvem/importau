// Configuração de teste para usar banco em memória
process.env.DATABASE_URL = 'postgresql://postgres:postgres@localhost:5433/pedidos_db';
process.env.NODE_ENV = 'test';
