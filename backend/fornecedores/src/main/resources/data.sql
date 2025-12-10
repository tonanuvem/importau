-- Script de inicialização de dados para o microsserviço de Fornecedores
-- Baseado nos dados do CSV fornecedores.csv

INSERT INTO fornecedores (
    fornecedor_id, razao_social, cnpj, pais_origem, categoria, rating, 
    tempo_parceria_anos, condicoes_pagamento, moeda_negociacao, 
    contato_email, telefone, ativo, created_at, updated_at
) VALUES 
('FORN001', 'TechSupply Brasil Ltda', '12.345.678/0001-90', 'Brasil', 'Tecnologia', 'A', 5, '30-60 dias', 'BRL', 'contato@techsupply.com.br', '+55-11-3456-7890', true, NOW(), NOW()),
('FORN002', 'Global Parts International', '98.765.432/0001-11', 'Brasil', 'Componentes', 'A_PLUS', 8, '45 dias', 'BRL', 'sales@globalparts.com', '+55-11-9876-5432', true, NOW(), NOW()),
('FORN003', 'EletroMax Distribuidora', '11.222.333/0001-44', 'Brasil', 'Eletrônicos', 'B_PLUS', 3, '15-30 dias', 'BRL', 'vendas@eletromax.com.br', '+55-21-2345-6789', true, NOW(), NOW()),
('FORN004', 'IndustriaPro Solutions', '22.333.444/0001-55', 'Brasil', 'Industrial', 'A', 6, '60 dias', 'BRL', 'comercial@industriapro.com', '+55-11-3344-5566', true, NOW(), NOW()),
('FORN005', 'MegaStock Logística', '33.444.555/0001-66', 'Brasil', 'Logística', 'B', 4, '30 dias', 'BRL', 'logistica@megastock.com.br', '+55-19-9988-7766', true, NOW(), NOW()),
('FORN006', 'USA Tech Imports LLC', null, 'Estados Unidos', 'Tecnologia', 'A_PLUS', 10, '90 dias', 'USD', 'import@usatech.com', '+1-555-123-4567', true, NOW(), NOW()),
('FORN007', 'QuímicaBrasil Industrial', '44.555.666/0001-77', 'Brasil', 'Químicos', 'A', 7, '45 dias', 'BRL', 'quimica@quimicabr.com', '+55-11-5544-3322', true, NOW(), NOW()),
('FORN008', 'FastParts Distribuição', '55.666.777/0001-88', 'Brasil', 'Autopeças', 'B_PLUS', 2, '15 dias', 'BRL', 'fastparts@fastparts.com.br', '+55-11-7788-9900', true, NOW(), NOW()),
('FORN009', 'NacionalTex Têxtil', '66.777.888/0001-99', 'Brasil', 'Têxtil', 'A_MINUS', 4, '30-45 dias', 'BRL', 'textil@nacionaltex.com', '+55-11-8899-0011', true, NOW(), NOW()),
('FORN010', 'China Manufacturing Co', null, 'China', 'Manufatura', 'B', 3, '90 dias', 'USD', 'export@chinamfg.cn', '+86-21-5555-8888', true, NOW(), NOW())
ON CONFLICT (fornecedor_id) DO NOTHING;
