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
    E devo receber uma lista com pelo menos 1 fornecedor

  Esquema do Cenário: Filtrar fornecedores por categoria
    Dado que existem fornecedores cadastrados
    Quando filtro fornecedores pela categoria "<categoria>"
    Então devo receber status 200
    E todos os fornecedores devem ter categoria "<categoria>"

    Exemplos:
      | categoria    |
      | Tecnologia   |
      | Componentes  |
      | Autopeças    |
      | Eletrônicos  |

  Esquema do Cenário: Filtrar fornecedores por rating
    Dado que existem fornecedores cadastrados
    Quando filtro fornecedores pelo rating "<rating>"
    Então devo receber status 200
    E todos os fornecedores devem ter rating "<rating>"

    Exemplos:
      | rating |
      | A+     |
      | A      |
      | B      |

  Cenário: Consultar fornecedor específico
    Dado que existe um fornecedor com fornecedor_id "FORN001"
    Quando busco o fornecedor pelo id
    Então devo receber status 200
    E o fornecedor deve ter razao_social "TechSupply Brasil Ltda"
    E o fornecedor deve ter cnpj "12.345.678/0001-90"
    E o fornecedor deve ter pais_origem "Brasil"

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/actuator/health"
    Então devo receber status 200
