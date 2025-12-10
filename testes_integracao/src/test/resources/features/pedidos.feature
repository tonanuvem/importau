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

  Esquema do Cenário: Filtrar pedidos por status
    Dado que existem pedidos cadastrados
    Quando filtro pedidos pelo status "<status>"
    Então devo receber status 200
    E todos os pedidos devem ter status "<status>"

    Exemplos:
      | status      |
      | PENDENTE    |
      | EM_TRANSITO |
      | ENTREGUE    |
      | CANCELADO   |

  Cenário: Buscar pedido existente (FUNC-04)
    Dado que existe um pedido com pedido_id "PED030"
    Quando busco o pedido pelo pedido_id
    Então devo receber status 200
    E o pedido deve ter fornecedor_id "FORN001"

  Cenário: Obter estatísticas dos pedidos
    Quando solicito as estatísticas dos pedidos
    Então devo receber status 200
    E a resposta deve conter contadores por status

  Esquema do Cenário: Filtrar pedidos por tipo de pagamento
    Dado que existem pedidos cadastrados
    Quando filtro pedidos pelo tipo_pagamento "<tipo>"
    Então devo receber status 200
    E todos os pedidos devem ter tipo_pagamento "<tipo>"

    Exemplos:
      | tipo           |
      | PIX            |
      | TED            |
      | BOLETO         |
      | CARTAO_CREDITO |

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/status"
    Então devo receber status 200
    E a resposta deve conter "status" igual a "healthy"
