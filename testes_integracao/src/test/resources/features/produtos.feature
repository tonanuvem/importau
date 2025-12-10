# language: pt
Funcionalidade: Gestão de Produtos
  Como usuário do sistema IMPORTAÚ
  Quero gerenciar produtos
  Para controlar o catálogo de importação

  Contexto:
    Dado que o microsserviço de produtos está disponível

  Cenário: Listar produtos com sucesso
    Quando solicito a lista de produtos
    Então devo receber status 200
    E devo receber uma lista de produtos

  Cenário: Criar produto com sucesso
    Dado que tenho os dados de um novo produto:
      | codigo   | nome           | categoria  | preco_unitario_brl |
      | TEST001  | Produto Teste  | Teste      | 100.0              |
    Quando crio o produto
    Então devo receber status 201
    E o produto deve ser criado com sucesso

  Cenário: Buscar produto por ID
    Dado que existe um produto com codigo "PROD001"
    Quando busco o produto pelo ID
    Então devo receber status 200
    E devo receber os dados do produto

  Cenário: Atualizar produto existente
    Dado que existe um produto com codigo "PROD001"
    Quando atualizo o nome para "Produto Atualizado"
    Então devo receber status 200
    E o produto deve ter o nome atualizado

  Cenário: Excluir produto existente
    Dado que existe um produto com codigo "PROD001"
    Quando excluo o produto
    Então devo receber status 200
    E o produto deve ser removido

  Esquema do Cenário: Filtrar produtos por categoria
    Dado que existem produtos das categorias <categoria1> e <categoria2>
    Quando filtro produtos pela categoria <filtro>
    Então devo receber apenas produtos da categoria <filtro>

    Exemplos:
      | categoria1  | categoria2  | filtro      |
      | Tecnologia  | Industrial  | Tecnologia  |
      | Eletrônicos | Autopeças   | Eletrônicos |
      | Têxtil      | Químicos    | Têxtil      |

  Cenário: Validar tempo de resposta
    Quando solicito a lista de produtos
    Então o tempo de resposta deve ser menor que 3 segundos

  Cenário: Verificar saúde do serviço
    Quando acesso o endpoint de saúde
    Então devo receber status 200
    E devo receber confirmação de que o serviço está saudável
