# language: pt
Funcionalidade: Gestão de Pagamentos - IMPORTAÚ
  Como gerente financeiro
  Quero gerenciar pagamentos a fornecedores
  Para correlacionar com pedidos e controlar fluxo de caixa (FUNC-04)
  
  Contexto:
    Dado que o microsserviço de pagamentos está disponível em "http://localhost:8003"

  Cenário: Listar pagamentos realizados
    Quando solicito a lista de pagamentos
    Então devo receber status 200

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/actuator/health"
    Então devo receber status 200
