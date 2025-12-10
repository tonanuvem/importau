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

  Cenário: Verificar endpoint de saúde
    Quando acesso o endpoint "/api/v1/status"
    Então devo receber status 200
