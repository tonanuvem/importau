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
    E devo receber uma lista com pelo menos 1 empréstimo

  Esquema do Cenário: Filtrar empréstimos por status
    Dado que existem empréstimos cadastrados
    Quando filtro empréstimos pelo status "<status>"
    Então devo receber status 200
    E todos os empréstimos devem ter status "<status>"

    Exemplos:
      | status     |
      | ATIVO      |
      | QUITADO    |
      | ATRASADO   |

  Esquema do Cenário: Filtrar empréstimos por finalidade
    Dado que existem empréstimos cadastrados
    Quando filtro empréstimos pela finalidade "<finalidade>"
    Então devo receber status 200
    E todos os empréstimos devem ter finalidade "<finalidade>"

    Exemplos:
      | finalidade                      |
      | Capital de Giro                 |
      | Aquisição de Equipamentos       |
      | Expansão de Negócio             |

  Cenário: Consultar empréstimo específico
    Dado que existe um empréstimo com emprestimo_id "EMP001"
    Quando busco o empréstimo pelo id
    Então devo receber status 200
    E o empréstimo deve ter instituicao_financeira "Banco Nacional"
    E o empréstimo deve ter valor_principal_brl 500000.00
    E o empréstimo deve ter taxa_juros_anual 12.5

  Esquema do Cenário: Criar novo empréstimo
    Dado que tenho os dados de um novo empréstimo:
      | campo                    | valor                  |
      | emprestimo_id            | <emprestimo_id>        |
      | instituicao_financeira   | <instituicao>          |
      | valor_principal_brl      | <valor>                |
      | taxa_juros_anual         | <taxa>                 |
      | prazo_meses              | <prazo>                |
      | finalidade               | <finalidade>           |
    Quando crio o empréstimo
    Então devo receber status 201
    E o empréstimo deve conter o emprestimo_id "<emprestimo_id>"

    Exemplos:
      | emprestimo_id | instituicao      | valor      | taxa | prazo | finalidade          |
      | TEST001       | Banco Teste      | 100000.00  | 10.0 | 12    | Capital de Giro     |
      | TEST002       | Banco Comercial  | 250000.00  | 12.5 | 24    | Expansão            |

  Cenário: Calcular saldo devedor total
    Quando solicito o saldo devedor total
    Então devo receber status 200
    E o saldo_devedor_total deve ser maior que 0

  Cenário: Listar empréstimos com vencimento próximo
    Quando solicito empréstimos com vencimento nos próximos 30 dias
    Então devo receber status 200
    E todos os empréstimos devem ter data_vencimento_proxima dentro de 30 dias

  Esquema do Cenário: Filtrar por instituição financeira
    Dado que existem empréstimos cadastrados
    Quando filtro empréstimos pela instituicao_financeira "<instituicao>"
    Então devo receber status 200
    E todos os empréstimos devem ser da instituicao "<instituicao>"

    Exemplos:
      | instituicao       |
      | Banco Nacional    |
      | Banco Comercial   |

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/api/v1/status"
    Então devo receber status 200
    E a resposta deve conter "status" igual a "healthy"
