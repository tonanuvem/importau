# language: pt
@integration
Funcionalidade: Testes de Integração IMPORTAU
  Como desenvolvedor
  Eu quero testar a integração entre microsserviços
  Para garantir que a comunicação funciona corretamente

  Contexto:
    Dado que todos os microsserviços estão executando
    E as APIs estão acessíveis

  Esquema do Cenário: Validar APIs dos Microsserviços
    Quando faço uma requisição GET para "<endpoint>"
    Então devo receber status code <status>
    E a resposta deve conter dados válidos

    Exemplos:
      | microsservico | endpoint                                    | status |
      | produtos      | http://localhost:8001/produtos             | 200    |
      | pedidos       | http://localhost:8002/pedidos              | 200    |
      | pagamentos    | http://localhost:8083/api/v1/pagamentos    | 200    |
      | fornecedores  | http://localhost:8084/api/v1/fornecedores  | 200    |
      | emprestimos   | http://localhost:8085/emprestimos          | 200    |
      | cambio        | http://localhost:8086/cambio               | 200    |

  Esquema do Cenário: Testar Especificações OpenAPI
    Quando acesso a especificação OpenAPI em "<spec_url>"
    Então devo receber uma especificação válida
    E deve conter informações sobre "<microsservico>"

    Exemplos:
      | microsservico | spec_url                                      |
      | produtos      | http://localhost:8001/openapi.json           |
      | pedidos       | http://localhost:8002/openapi.json           |
      | emprestimos   | http://localhost:8085/api/openapi.json       |
      | cambio        | http://localhost:8086/api/openapi.json       |
