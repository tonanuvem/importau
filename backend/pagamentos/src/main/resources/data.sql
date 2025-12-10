-- Script de inicialização de dados para o microsserviço de Pagamentos
-- Baseado nos dados do CSV pagamentos.csv

INSERT INTO pagamentos (
    pagamento_id, pedido_id, fornecedor_id, data_pagamento, valor_pago_brl, 
    metodo_pagamento, status_pagamento, taxa_cambio_aplicada, moeda_origem, 
    valor_moeda_origem, num_parcelas, parcela_atual, data_vencimento, 
    usuario_aprovacao, created_at, updated_at
) VALUES 
('PAG001', 'PED001', 'FORN001', '2024-11-14', 15750.50, 'BOLETO', 'PAGO', 1.0000, 'BRL', 15750.50, 1, 1, '2024-11-14', 'celso.oliveira', NOW(), NOW()),
('PAG002', 'PED002', 'FORN002', '2024-11-30', 8920.00, 'TRANSFERENCIA', 'PENDENTE', 1.0000, 'BRL', 8920.00, 1, 1, '2024-11-30', 'celso.oliveira', NOW(), NOW()),
('PAG003', 'PED003', 'FORN003', '2024-11-01', 12300.75, 'CARTAO_CREDITO', 'PAGO', 1.0000, 'BRL', 12300.75, 3, 1, '2024-11-01', 'marcos.santos', NOW(), NOW()),
('PAG004', 'PED003', 'FORN003', '2024-12-01', 12300.75, 'CARTAO_CREDITO', 'AGENDADO', 1.0000, 'BRL', 12300.75, 3, 2, '2024-12-01', 'marcos.santos', NOW(), NOW()),
('PAG005', 'PED003', 'FORN003', '2025-01-01', 12300.75, 'CARTAO_CREDITO', 'AGENDADO', 1.0000, 'BRL', 12300.75, 3, 3, '2025-01-01', 'marcos.santos', NOW(), NOW()),
('PAG006', 'PED004', 'FORN001', '2024-12-17', 25600.00, 'BOLETO', 'PAGO', 1.0000, 'BRL', 25600.00, 1, 1, '2024-12-17', 'celso.oliveira', NOW(), NOW()),
('PAG007', 'PED005', 'FORN004', '2024-10-19', 5400.25, 'PIX', 'CANCELADO', 1.0000, 'BRL', 5400.25, 1, 1, '2024-10-19', 'celso.oliveira', NOW(), NOW()),
('PAG008', 'PED006', 'FORN005', '2024-11-19', 18900.00, 'BOLETO', 'PENDENTE', 1.0000, 'BRL', 18900.00, 1, 1, '2024-11-19', 'celso.oliveira', NOW(), NOW()),
('PAG009', 'PED007', 'FORN002', '2024-12-06', 9850.50, 'TRANSFERENCIA', 'PAGO', 1.0000, 'BRL', 9850.50, 1, 1, '2024-12-06', 'celso.oliveira', NOW(), NOW()),
('PAG010', 'PED008', 'FORN006', '2025-01-20', 32100.00, 'BOLETO', 'AGENDADO', 5.2500, 'USD', 6114.29, 1, 1, '2025-01-20', 'marcos.santos', NOW(), NOW()),
('PAG011', 'PED009', 'FORN003', '2024-10-30', 7200.00, 'PIX', 'PAGO', 1.0000, 'BRL', 7200.00, 1, 1, '2024-10-30', 'celso.oliveira', NOW(), NOW()),
('PAG012', 'PED010', 'FORN007', '2024-11-23', 14500.75, 'CARTAO_CREDITO', 'PENDENTE', 1.0000, 'BRL', 14500.75, 2, 1, '2024-11-23', 'marcos.santos', NOW(), NOW()),
('PAG013', 'PED010', 'FORN007', '2024-12-23', 14500.75, 'CARTAO_CREDITO', 'AGENDADO', 1.0000, 'BRL', 14500.75, 2, 2, '2024-12-23', 'marcos.santos', NOW(), NOW()),
('PAG014', 'PED011', 'FORN001', '2024-12-09', 19800.00, 'BOLETO', 'PAGO', 1.0000, 'BRL', 19800.00, 1, 1, '2024-12-09', 'celso.oliveira', NOW(), NOW()),
('PAG015', 'PED012', 'FORN008', '2024-11-10', 6700.50, 'TRANSFERENCIA', 'PENDENTE', 1.0000, 'BRL', 6700.50, 1, 1, '2024-11-10', 'celso.oliveira', NOW(), NOW()),
('PAG016', 'PED013', 'FORN004', '2024-12-26', 28900.00, 'BOLETO', 'AGENDADO', 1.0000, 'BRL', 28900.00, 1, 1, '2024-12-26', 'celso.oliveira', NOW(), NOW()),
('PAG017', 'PED014', 'FORN009', '2024-11-04', 11200.25, 'PIX', 'PAGO', 1.0000, 'BRL', 11200.25, 1, 1, '2024-11-04', 'celso.oliveira', NOW(), NOW()),
('PAG018', 'PED015', 'FORN005', '2024-11-28', 16500.00, 'CARTAO_CREDITO', 'PENDENTE', 1.0000, 'BRL', 16500.00, 3, 1, '2024-11-28', 'marcos.santos', NOW(), NOW()),
('PAG019', 'PED015', 'FORN005', '2024-12-28', 16500.00, 'CARTAO_CREDITO', 'AGENDADO', 1.0000, 'BRL', 16500.00, 3, 2, '2024-12-28', 'marcos.santos', NOW(), NOW()),
('PAG020', 'PED015', 'FORN005', '2025-01-28', 16500.00, 'CARTAO_CREDITO', 'AGENDADO', 1.0000, 'BRL', 16500.00, 3, 3, '2025-01-28', 'marcos.santos', NOW(), NOW()),
('PAG021', 'PED016', 'FORN002', '2024-12-14', 22400.75, 'BOLETO', 'PENDENTE', 1.0000, 'BRL', 22400.75, 1, 1, '2024-12-14', 'celso.oliveira', NOW(), NOW()),
('PAG022', 'PED017', 'FORN010', '2024-11-30', 8900.00, 'TRANSFERENCIA', 'PAGO', 5.3200, 'USD', 1672.93, 1, 1, '2024-11-30', 'marcos.santos', NOW(), NOW()),
('PAG023', 'PED018', 'FORN003', '2024-11-16', 13700.50, 'BOLETO', 'PENDENTE', 1.0000, 'BRL', 13700.50, 1, 1, '2024-11-16', 'celso.oliveira', NOW(), NOW()),
('PAG024', 'PED019', 'FORN006', '2025-02-01', 45200.00, 'BOLETO', 'AGENDADO', 5.2800, 'USD', 8560.61, 1, 1, '2025-02-01', 'marcos.santos', NOW(), NOW()),
('PAG025', 'PED020', 'FORN001', '2024-11-10', 9500.25, 'PIX', 'PAGO', 1.0000, 'BRL', 9500.25, 1, 1, '2024-11-10', 'celso.oliveira', NOW(), NOW()),
('PAG026', 'PED021', 'FORN007', '2024-12-04', 17800.00, 'CARTAO_CREDITO', 'PENDENTE', 1.0000, 'BRL', 17800.00, 2, 1, '2024-12-04', 'marcos.santos', NOW(), NOW()),
('PAG027', 'PED021', 'FORN007', '2025-01-04', 17800.00, 'CARTAO_CREDITO', 'AGENDADO', 1.0000, 'BRL', 17800.00, 2, 2, '2025-01-04', 'marcos.santos', NOW(), NOW()),
('PAG028', 'PED022', 'FORN008', '2024-12-20', 12100.75, 'TRANSFERENCIA', 'PENDENTE', 1.0000, 'BRL', 12100.75, 1, 1, '2024-12-20', 'celso.oliveira', NOW(), NOW()),
('PAG029', 'PED023', 'FORN004', '2025-01-05', 24600.00, 'BOLETO', 'AGENDADO', 1.0000, 'BRL', 24600.00, 1, 1, '2025-01-05', 'celso.oliveira', NOW(), NOW()),
('PAG030', 'PED024', 'FORN009', '2024-11-14', 7800.50, 'PIX', 'PENDENTE', 1.0000, 'BRL', 7800.50, 1, 1, '2024-11-14', 'celso.oliveira', NOW(), NOW())
ON CONFLICT (pagamento_id) DO NOTHING;
