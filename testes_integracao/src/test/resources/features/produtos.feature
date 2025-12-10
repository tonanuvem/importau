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

  Cenário: Buscar produto existente por código
    Dado que existe um produto com codigo "PROD001"
    Quando busco o produto pelo codigo
    Então devo receber status 200
    E o produto deve ter o nome "Processador Intel i7"
    E o produto deve ter a categoria "Tecnologia"

  Cenário: Validar produtos com estoque baixo
    Quando solicito produtos com estoque abaixo do mínimo
    Então devo receber status 200

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/status"
    Então devo receber status 200
