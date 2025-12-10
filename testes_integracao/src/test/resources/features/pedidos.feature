# language: pt
Funcionalidade: Gestão de Pedidos
  Como usuário do sistema IMPORTAÚ
  Quero gerenciar pedidos de importação
  Para controlar o fluxo de compras

  Contexto:
    Dado que o microsserviço de pedidos está disponível

  Cenário: Listar pedidos com sucesso
    Quando solicito a lista de pedidos
    Então devo receber status 200
    E devo receber uma lista de pedidos

  Cenário: Criar pedido com sucesso
    Dado que tenho os dados de um novo pedido:
      | pedido_id | data_pedido | fornecedor_id | valor_total_brl | status   |
      | PED999    | 2024-12-10  | FORN001       | 2500.00         | PENDENTE |
    Quando crio o pedido
    Então devo receber status 201
    E o pedido deve ser criado com sucesso

  Cenário: Buscar pedido por ID
    Dado que existe um pedido com codigo "PED001"
    Quando busco o pedido pelo ID
    Então devo receber status 200
    E devo receber os dados do pedido

  Cenário: Atualizar status do pedido
    Dado que existe um pedido com codigo "PED001"
    Quando atualizo o status para "EM_TRANSITO"
    Então devo receber status 200
    E o pedido deve ter o status atualizado

  Cenário: Excluir pedido existente
    Dado que existe um pedido com codigo "PED001"
    Quando excluo o pedido
    Então devo receber status 200
    E o pedido deve ser removido

  Esquema do Cenário: Filtrar pedidos por status
    Dado que existem pedidos com status <status1> e <status2>
    Quando filtro pedidos pelo status <filtro>
    Então devo receber apenas pedidos com status <filtro>

    Exemplos:
      | status1   | status2     | filtro      |
      | PENDENTE  | ENTREGUE    | PENDENTE    |
      | ENTREGUE  | EM_TRANSITO | ENTREGUE    |
      | CANCELADO | PENDENTE    | CANCELADO   |

  Cenário: Obter estatísticas dos pedidos
    Quando solicito as estatísticas dos pedidos
    Então devo receber status 200
    E devo receber resumo por status

  Cenário: Validar tempo de resposta
    Quando solicito a lista de pedidos
    Então o tempo de resposta deve ser menor que 3 segundos

  Cenário: Verificar saúde do serviço
    Quando acesso o endpoint de saúde dos pedidos
    Então devo receber status 200
    E devo receber confirmação de que o serviço está saudável
