# language: pt
@ui
Funcionalidade: Testes de Interface Swagger IMPORTAU
  Como desenvolvedor
  Eu quero validar que todas as interfaces Swagger funcionam corretamente
  Para que a documentação da API seja acessível e funcional

  Contexto:
    Dado que o navegador está configurado para testes headless
    E todos os microsserviços estão executando

  Esquema do Cenário: Validar Interface Swagger dos Microsserviços
    Quando navego para a interface Swagger do "<microsservico>" em "<url>"
    Então a interface Swagger deve carregar com sucesso
    E devo ver elementos de documentação da API
    E devo conseguir expandir operações da API
    Quando tiro uma captura de tela
    Então a captura deve ser salva com timestamp

    Exemplos:
      | microsservico | url                                                    |
      | produtos      | http://localhost:8001/docs                            |
      | pedidos       | http://localhost:8002/docs                            |
      | pagamentos    | http://localhost:8083/swagger-ui.html                 |
      | fornecedores  | http://localhost:8084/swagger-ui.html                 |
      | emprestimos   | http://localhost:8085/swagger-ui/index.html           |
      | cambio        | http://localhost:8086/swagger-ui/index.html           |

  Cenário: Testar operações da API através da interface Swagger
    Quando navego para a interface Swagger do "produtos" em "http://localhost:8001/docs"
    E expando a primeira operação da API
    E clico no botão "Try it out"
    E clico no botão "Execute"
    Então devo ver uma resposta de API bem-sucedida
    Quando tiro uma captura de tela
    Então a captura deve ser salva com timestamp
