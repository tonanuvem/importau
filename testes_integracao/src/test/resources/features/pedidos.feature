# language: pt
Funcionalidade: Gestão de Pedidos - IMPORTAÚ
  Como gerente financeiro
  Quero gerenciar pedidos de importação
  Para correlacionar com transações financeiras via Open Finance (FUNC-04)

  Contexto:
    Dado que o microsserviço de pedidos está disponível em "http://localhost:8002"

  Cenário: Listar pedidos com sucesso
    Quando solicito a lista de pedidos
    Então devo receber status 200
    E devo receber uma lista com pelo menos 1 pedido

  Cenário: Buscar pedido existente (FUNC-04)
    Dado que existe um pedido com pedido_id "PED030"
    Quando busco o pedido pelo pedido_id
    Então devo receber status 200
    E o pedido deve ter status "ENTREGUE"

  Cenário: Obter estatísticas dos pedidos
    Quando solicito as estatísticas dos pedidos
    Então devo receber status 200

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/status"
    Então devo receber status 200
