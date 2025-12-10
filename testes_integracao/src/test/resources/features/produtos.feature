# language: pt
Funcionalidade: Gestão de Produtos - IMPORTAÚ
  Como gerente de marketing
  Quero gerenciar o catálogo de produtos importados
  Para controlar volume de importação por categoria (FUNC-01)

  Contexto:
    Dado que o microsserviço de produtos está disponível em "http://localhost:8001"

  Cenário: Listar produtos com sucesso
    Quando solicito a lista de produtos
    Então devo receber status 200
    E devo receber uma lista com pelo menos 1 produto

  Esquema do Cenário: Buscar produtos por categoria (FUNC-01)
    Dado que existem produtos cadastrados
    Quando busco produtos da categoria "<categoria>"
    Então devo receber status 200
    E todos os produtos devem ser da categoria "<categoria>"

    Exemplos:
      | categoria    |
      | Tecnologia   |
      | Eletrônicos  |
      | Autopeças    |
      | Têxtil       |

  Cenário: Buscar produto existente por código
    Dado que existe um produto com codigo "PROD001"
    Quando busco o produto pelo codigo
    Então devo receber status 200
    E o produto deve ter a categoria "Tecnologia"

  Cenário: Atualizar estoque de produto
    Dado que existe um produto com codigo "PROD001"
    Quando atualizo o estoque_atual para 200
    Então devo receber status 200
    E o produto deve ter estoque_atual igual a 200

  Cenário: Validar produtos com estoque baixo
    Quando solicito produtos com estoque abaixo do mínimo
    Então devo receber status 200
    E todos os produtos devem ter estoque_atual menor que estoque_minimo

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/status"
    Então devo receber status 200
    E a resposta deve conter "status" igual a "healthy"
