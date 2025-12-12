const express = require('express');
const cors = require('cors');
const { Pool } = require('pg');

const app = express();
const PORT = process.env.PORT || 8002;
const DATABASE_URL = process.env.DATABASE_URL || 'postgresql://postgres:postgres@localhost:5433/pedidos_db';

const pool = new Pool({ connectionString: DATABASE_URL });

app.use(cors());
app.use(express.json());

// Health check
app.get('/status', (req, res) => {
  res.json({ status: 'healthy', service: 'pedidos', timestamp: new Date().toISOString() });
});

// Listar pedidos
app.get('/pedidos', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM pedidos LIMIT 100');
    res.json(result.rows);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// Inicializar banco
async function initDB() {
  try {
    await pool.query(`
      CREATE TABLE IF NOT EXISTS pedidos (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        pedido_id VARCHAR(50) UNIQUE NOT NULL,
        data_pedido DATE NOT NULL,
        fornecedor_id VARCHAR(50) NOT NULL,
        valor_total_brl DECIMAL(15,2) NOT NULL,
        status VARCHAR(20) DEFAULT 'PENDENTE',
        tipo_pagamento VARCHAR(20),
        prazo_dias INTEGER DEFAULT 30,
        data_entrega_prevista DATE,
        usuario_criacao VARCHAR(100),
        observacoes TEXT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      );
    `);
    console.log('Banco inicializado');
  } catch (error) {
    console.error('Erro ao inicializar banco:', error);
  }
}

// Iniciar servidor
async function start() {
  await initDB();
  app.listen(PORT, '0.0.0.0', () => {
    console.log(`Pedidos service running on port ${PORT}`);
  });
}

start().catch(console.error);
