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
    E devo receber uma lista com pelo menos 1 pagamento

  Esquema do Cenário: Filtrar pagamentos por status
    Dado que existem pagamentos cadastrados
    Quando filtro pagamentos pelo status_pagamento "<status>"
    Então devo receber status 200
    E todos os pagamentos devem ter status_pagamento "<status>"

    Exemplos:
      | status    |
      | PAGO      |
      | PENDENTE  |
      | ATRASADO  |
      | CANCELADO |

  Esquema do Cenário: Filtrar pagamentos por método
    Dado que existem pagamentos cadastrados
    Quando filtro pagamentos pelo metodo_pagamento "<metodo>"
    Então devo receber status 200
    E todos os pagamentos devem ter metodo_pagamento "<metodo>"

    Exemplos:
      | metodo         |
      | BOLETO         |
      | TRANSFERENCIA  |
      | PIX            |
      | CARTAO_CREDITO |

  Cenário: Correlacionar pagamento com pedido (FUNC-04)
    Dado que existe um pagamento com pagamento_id "PAG001"
    Quando busco o pagamento pelo id
    Então devo receber status 200
    E o pagamento deve estar vinculado ao pedido_id "PED001"
    E o pagamento deve ter fornecedor_id "FORN001"
    E o pagamento deve ter valor_pago_brl 15750.50

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/actuator/health"
    Então devo receber status 200
