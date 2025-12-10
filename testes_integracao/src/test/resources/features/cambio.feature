# language: pt
Funcionalidade: Gestão de Câmbio - IMPORTAÚ
  Como gerente financeiro
  Quero consultar taxas de câmbio atualizadas
  Para calcular custos de importação e tomar decisões estratégicas
  
  Contexto:
    Dado que o microsserviço de câmbio está disponível em "http://localhost:8006"

  Cenário: Listar operações de câmbio
    Quando solicito a lista de operações de câmbio
    Então devo receber status 200
    E devo receber uma lista com pelo menos 1 operação

  Esquema do Cenário: Consultar taxa de câmbio por moeda
    Dado que existem cotações cadastradas
    Quando consulto a taxa de câmbio para "<moeda>"
    Então devo receber status 200
    E a moeda deve ser "<moeda>"
    E deve conter taxa_compra e taxa_venda

    Exemplos:
      | moeda |
      | USD   |
      | EUR   |
      | GBP   |
      | JPY   |

  Esquema do Cenário: Filtrar câmbio por tipo
    Dado que existem cotações cadastradas
    Quando filtro operações pelo tipo_cambio "<tipo>"
    Então devo receber status 200
    E todas as operações devem ter tipo_cambio "<tipo>"

    Exemplos:
      | tipo       |
      | COMERCIAL  |
      | TURISMO    |

  Cenário: Consultar taxa PTAX do dia
    Dado que existe cotação para "USD" na data "2024-10-15"
    Quando consulto a taxa PTAX
    Então devo receber status 200
    E a taxa_ptax deve ser 5.2465
    E a fonte deve ser "SISBACEN"

  Esquema do Cenário: Calcular conversão de moeda
    Dado que tenho um valor de <valor_origem> em "<moeda_origem>"
    Quando solicito conversão para "<moeda_destino>"
    Então devo receber status 200
    E o valor convertido deve ser calculado corretamente

    Exemplos:
      | valor_origem | moeda_origem | moeda_destino |
      | 1000.00      | USD          | BRL           |
      | 5000.00      | EUR          | BRL           |
      | 10000.00     | BRL          | USD           |

  Cenário: Verificar variação diária do câmbio
    Quando consulto operações com variacao_dia_percentual maior que 0
    Então devo receber status 200
    E todas as operações devem ter variacao_dia_percentual positiva

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/api/v1/status"
    Então devo receber status 200
    E a resposta deve conter "status" igual a "healthy"
