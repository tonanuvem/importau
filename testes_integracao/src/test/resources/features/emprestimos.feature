# language: pt
Funcionalidade: Gestão de Empréstimos - IMPORTAÚ
  Como gerente financeiro
  Quero gerenciar empréstimos e financiamentos
  Para controlar capital de giro e investimentos da empresa
  
  Contexto:
    Dado que o microsserviço de empréstimos está disponível em "http://localhost:8005"

  Cenário: Listar empréstimos ativos
    Quando solicito a lista de empréstimos
    Então devo receber status 200

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/api/v1/status"
    Então devo receber status 200
