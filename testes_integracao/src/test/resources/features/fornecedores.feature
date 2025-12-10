# language: pt
Funcionalidade: Gestão de Fornecedores - IMPORTAÚ
  Como gerente de compras
  Quero gerenciar cadastro de fornecedores
  Para controlar parcerias comerciais e condições de pagamento
  
  Contexto:
    Dado que o microsserviço de fornecedores está disponível em "http://localhost:8004"

  Cenário: Listar fornecedores cadastrados
    Quando solicito a lista de fornecedores
    Então devo receber status 200

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/actuator/health"
    Então devo receber status 200
