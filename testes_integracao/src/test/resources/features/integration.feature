# language: pt
@integration
Funcionalidade: Testes de Integração IMPORTAU - Carga e Validação de Dados
  Como desenvolvedor
  Eu quero testar a integração entre microsserviços e validar a carga de dados
  Para garantir que todos os dados foram carregados corretamente nos bancos

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

  Esquema do Cenário: Validar Carga de Dados nos Bancos
    Quando consulto os dados de "<microsservico>" no endpoint "<endpoint>"
    Então devo receber pelo menos <min_registros> registros
    E os dados devem ter estrutura válida para "<microsservico>"

    Exemplos:
      | microsservico | endpoint                                    | min_registros |
      | produtos      | http://localhost:8001/produtos             | 5             |
      | pedidos       | http://localhost:8002/pedidos              | 10            |
      | pagamentos    | http://localhost:8083/api/v1/pagamentos    | 8             |
      | fornecedores  | http://localhost:8084/api/v1/fornecedores  | 5             |
      | emprestimos   | http://localhost:8085/emprestimos          | 15            |
      | cambio        | http://localhost:8086/cambio               | 20            |

  Esquema do Cenário: Validar Estrutura de Dados Específicos
    Quando busco um registro específico de "<microsservico>" no endpoint "<endpoint>"
    Então o registro deve conter os campos obrigatórios de "<microsservico>"
    E os tipos de dados devem estar corretos

    Exemplos:
      | microsservico | endpoint                                           |
      | produtos      | http://localhost:8001/produtos                    |
      | pedidos       | http://localhost:8002/pedidos                     |
      | pagamentos    | http://localhost:8083/api/v1/pagamentos           |
      | fornecedores  | http://localhost:8084/api/v1/fornecedores         |
      | emprestimos   | http://localhost:8085/emprestimos                 |
      | cambio        | http://localhost:8086/cambio                      |

  Esquema do Cenário: Testar Integridade Referencial
    Quando consulto dados relacionados entre "<microsservico1>" e "<microsservico2>"
    Então as referências devem ser consistentes
    E não deve haver dados órfãos

    Exemplos:
      | microsservico1 | microsservico2 |
      | pedidos        | fornecedores   |
      | pagamentos     | pedidos        |
      | pagamentos     | fornecedores   |

  Cenário: Validar Health Checks de Todos os Serviços
    Quando verifico o health check de todos os microsserviços
    Então todos devem retornar status "healthy"
    E devem ter timestamps válidos

  Cenário: Validar Conectividade com Bancos de Dados
    Quando testo a conectividade com os bancos de dados
    Então todas as conexões devem estar ativas
    E os schemas devem estar criados corretamente

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

  Cenário: Validar Dados CSV Carregados Automaticamente
    Dado que os microsserviços foram iniciados
    Quando verifico se os dados dos CSVs foram carregados
    Então todos os microsserviços devem ter dados iniciais
    E a quantidade deve corresponder aos arquivos CSV originais
