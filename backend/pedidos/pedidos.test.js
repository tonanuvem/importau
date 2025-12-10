/**
 * Testes unitários para o microsserviço de pedidos
 * Implementa TDD com cobertura completa
 */
const request = require('supertest');
const { app } = require('./server');

describe('Microsserviço de Pedidos', () => {
  
  describe('GET /status', () => {
    it('deve retornar status healthy', async () => {
      const response = await request(app)
        .get('/status')
        .expect(200);

      expect(response.body.status).toBe('healthy');
      expect(response.body.service).toBe('pedidos');
      expect(response.body.timestamp).toBeDefined();
    });
  });

  describe('GET /pedidos', () => {
    it('deve retornar lista de pedidos', async () => {
      const response = await request(app)
        .get('/pedidos')
        .expect(200);

      expect(Array.isArray(response.body)).toBe(true);
    });

    it('deve filtrar pedidos por status', async () => {
      const response = await request(app)
        .get('/pedidos?status=PENDENTE')
        .expect(200);

      expect(Array.isArray(response.body)).toBe(true);
      // Se houver pedidos, todos devem ter status PENDENTE
      if (response.body.length > 0) {
        response.body.forEach(pedido => {
          expect(pedido.status).toBe('PENDENTE');
        });
      }
    });

    it('deve respeitar limite de paginação', async () => {
      const response = await request(app)
        .get('/pedidos?limit=5')
        .expect(200);

      expect(Array.isArray(response.body)).toBe(true);
      expect(response.body.length).toBeLessThanOrEqual(5);
    });
  });

  describe('POST /pedidos', () => {
    const novoPedido = {
      pedido_id: 'TEST001',
      data_pedido: '2024-12-10',
      fornecedor_id: 'FORN001',
      valor_total_brl: 1500.50,
      status: 'PENDENTE',
      tipo_pagamento: 'BOLETO',
      prazo_dias: 30,
      data_entrega_prevista: '2025-01-10',
      usuario_criacao: 'test.user',
      observacoes: 'Pedido de teste'
    };

    it('deve criar novo pedido com sucesso', async () => {
      const response = await request(app)
        .post('/pedidos')
        .send(novoPedido)
        .expect(201);

      expect(response.body.pedido_id).toBe(novoPedido.pedido_id);
      expect(response.body.valor_total_brl).toBe(novoPedido.valor_total_brl);
      expect(response.body.id).toBeDefined();
    });

    it('deve rejeitar pedido com código duplicado', async () => {
      // Primeiro pedido
      await request(app)
        .post('/pedidos')
        .send({ ...novoPedido, pedido_id: 'DUP001' })
        .expect(201);

      // Segundo pedido com mesmo código
      const response = await request(app)
        .post('/pedidos')
        .send({ ...novoPedido, pedido_id: 'DUP001' })
        .expect(400);

      expect(response.body.error).toContain('já existe');
    });

    it('deve rejeitar pedido com valor inválido', async () => {
      const response = await request(app)
        .post('/pedidos')
        .send({ ...novoPedido, pedido_id: 'VAL001', valor_total_brl: 0 })
        .expect(400);

      expect(response.body.error).toContain('maior que zero');
    });

    it('deve rejeitar pedido sem campos obrigatórios', async () => {
      const response = await request(app)
        .post('/pedidos')
        .send({ pedido_id: 'INC001' })
        .expect(500);

      expect(response.body.error).toBeDefined();
    });
  });

  describe('GET /pedidos/:id', () => {
    let pedidoId;

    beforeAll(async () => {
      const response = await request(app)
        .post('/pedidos')
        .send({
          pedido_id: 'GET001',
          data_pedido: '2024-12-10',
          fornecedor_id: 'FORN001',
          valor_total_brl: 2000.00
        });
      pedidoId = response.body.id;
    });

    it('deve retornar pedido por ID', async () => {
      const response = await request(app)
        .get(`/pedidos/${pedidoId}`)
        .expect(200);

      expect(response.body.id).toBe(pedidoId);
      expect(response.body.pedido_id).toBe('GET001');
    });

    it('deve retornar 404 para pedido inexistente', async () => {
      const response = await request(app)
        .get('/pedidos/00000000-0000-0000-0000-000000000000')
        .expect(404);

      expect(response.body.error).toBe('Pedido não encontrado');
    });
  });

  describe('PUT /pedidos/:id', () => {
    let pedidoId;

    beforeAll(async () => {
      const response = await request(app)
        .post('/pedidos')
        .send({
          pedido_id: 'UPD001',
          data_pedido: '2024-12-10',
          fornecedor_id: 'FORN001',
          valor_total_brl: 3000.00,
          status: 'PENDENTE'
        });
      pedidoId = response.body.id;
    });

    it('deve atualizar pedido existente', async () => {
      const updates = {
        status: 'EM_TRANSITO',
        observacoes: 'Pedido em trânsito'
      };

      const response = await request(app)
        .put(`/pedidos/${pedidoId}`)
        .send(updates)
        .expect(200);

      expect(response.body.status).toBe('EM_TRANSITO');
      expect(response.body.observacoes).toBe('Pedido em trânsito');
    });

    it('deve retornar 404 para pedido inexistente', async () => {
      const response = await request(app)
        .put('/pedidos/00000000-0000-0000-0000-000000000000')
        .send({ status: 'ENTREGUE' })
        .expect(404);

      expect(response.body.error).toBe('Pedido não encontrado');
    });
  });

  describe('DELETE /pedidos/:id', () => {
    let pedidoId;

    beforeAll(async () => {
      const response = await request(app)
        .post('/pedidos')
        .send({
          pedido_id: 'DEL001',
          data_pedido: '2024-12-10',
          fornecedor_id: 'FORN001',
          valor_total_brl: 4000.00
        });
      pedidoId = response.body.id;
    });

    it('deve excluir pedido existente', async () => {
      const response = await request(app)
        .delete(`/pedidos/${pedidoId}`)
        .expect(200);

      expect(response.body.message).toContain('excluído com sucesso');

      // Verifica se foi realmente excluído
      await request(app)
        .get(`/pedidos/${pedidoId}`)
        .expect(404);
    });

    it('deve retornar 404 para pedido inexistente', async () => {
      const response = await request(app)
        .delete('/pedidos/00000000-0000-0000-0000-000000000000')
        .expect(404);

      expect(response.body.error).toBe('Pedido não encontrado');
    });
  });

  describe('GET /pedidos/stats/resumo', () => {
    it('deve retornar estatísticas dos pedidos', async () => {
      const response = await request(app)
        .get('/pedidos/stats/resumo')
        .expect(200);

      expect(Array.isArray(response.body)).toBe(true);
      // Se houver dados, cada item deve ter status, quantidade e valor_total
      if (response.body.length > 0) {
        response.body.forEach(stat => {
          expect(stat.status).toBeDefined();
          expect(stat.quantidade).toBeDefined();
          expect(stat.valor_total).toBeDefined();
        });
      }
    });
  });

  describe('Validações de Performance', () => {
    it('deve responder em menos de 3 segundos', async () => {
      const startTime = Date.now();
      
      await request(app)
        .get('/pedidos')
        .expect(200);
      
      const responseTime = Date.now() - startTime;
      expect(responseTime).toBeLessThan(3000);
    });
  });

  describe('Swagger Documentation', () => {
    it('deve servir documentação Swagger', async () => {
      const response = await request(app)
        .get('/docs/')
        .expect(200);

      expect(response.text).toContain('swagger');
    });
  });
});
