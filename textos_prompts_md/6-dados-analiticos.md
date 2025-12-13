# Implementar um pipeline de dados com foco em governança, utilizando a estrutura que definimos (Bronze, Silver, Gold), orquestrada pelo AWS Step Functions e catalogada pelo Glue.

## Função
Você é um Engenheiro de Dados experiente. Sua missão é implementar a arquitetura técnica utilizando a estrutura que definimos (Bronze, Silver, Gold) em Lakehouse.

## Leia a imagem IMPORTAU_ARQUITETURA_MICROSERVICES.png para entender o projeto e fazer sua implementação.

Caso não encontre os arquivos, interromper o processamento e confirmar com o Aluno para procurar nas pastas

## Instruções de Análise

### 1. Resumo Executivo
Criar um pipeline de dados baseado em AWS S3 (Lakehouse) contemplando governança de dados: Catálogo, Lineage e Discovery.
Verifique qual o NOME_USUARIO que está logado na aws cli.
Como convenção, considerar que o bucket S3 no Lakehouse deste projeto é s3://importau-lakehouse-NOME_USUARIO/.
```
/importau
  /infra
      /AWSStepFunctions
      /Glue
  /scripts
```

### 2. Objetivos

#### 2.1. Criar Arquivos Lambda ou Glue do AWS Step Functions (Python) para estruturar o pipeline conforme estrutura que definimos (Bronze, Silver, Gold)
- StateMachine_DAG BRONZE (StateMachine_DAG_bronze.py)
Esta StateMachine_DAG simula o microsserviço de Ingestão gerando dados crus/normalizados e carregando-os na camada BRONZE no S3. O script gerar_csvs.py original foi refatorado para usar pandas e simular a escrita no S3 em formato Parquet.
- StateMachine_DAG TESTE BRONZE (StateMachine_DAG_teste_bronze.py)
Esta StateMachine_DAG testa a etapa anterior.

- StateMachine_DAG SILVER (StateMachine_DAG_silver.py)
Esta StateMachine_DAG representa o microsserviço Correlator & ETL. Ela consome os 7 Parquets da BRONZE, executa as transformações e correlações (joins), e gera o fato consolidado na camada SILVER.
- StateMachine_DAG TESTE SILVER (StateMachine_DAG_teste_silver.py)
Esta StateMachine_DAG testa a etapa anterior.

- StateMachine_DAG GOLD (StateMachine_DAG_gold.py)
Esta StateMachine_DAG consome o FATO correlacionado da SILVER para criar os ativos curados e agregados da GOLD, otimizados para consumo (BI/Dashboard e ML).
- StateMachine_DAG TESTE GOLD (StateMachine_DAG_teste_gold.py)
Esta StateMachine_DAG testa a etapa anterior.

#### 2.2. Projeto de Implementação Prática: AWS Step Functions e Glue
Para colocar estas StateMachine_DAGs em funcionamento com a governança desejada, criar os arquivos necessários para os passos abaixo (tenho permissões restritas na AWS, então não deve ser criada nenhuma role IAM):
- Verificar as permissões que tenho usando os comandos da AWS CLI.
- Estrutura do Projeto na pasta /datapipeline/AWSStepFunctions: Crie a estrutura de diretórios do AWS Step Functions.
- Configuração S3 (Lakehouse) na pasta /script: Verifique se já existe ou Crie o bucket no S3. Configure a Connection aws_default (ou um ID específico) no AWS Step Functions, garantindo as credenciais de acesso ao S3.
- Ajuste o projeto para fazer a instalação de Bibliotecas necessárias: Instale as dependências necessárias para a execução das funções Python nas StateMachine_DAGs.
- Configuração Glue na pasta /datapipeline/Glue: crie os scripts e arquivos necessários para criar e configurar o Glue.
Glue e AWS Step Functions integrados: Deve contemplar Catálogo/Discovery usando o AWS Step Functions Connector no Glue. Isso irá ingestar automaticamente as StateMachine_DAGs, tarefas, descrições e outros parametros tais como os proprietários (owner) no Catálogo de Dados.
Glue e S3 integrados: Deve contemplar Catálogo/Discovery usando S3/AWS Glue Connector no Glue, apontando para o bucket s3. Isso descobrirá os schemas dos arquivos nas camadas Bronze, Silver e Gold.
Rastreamento de Transformação	Lineage: Deve contemplar Lineage usando o Glue e AWS Step Functions. As descrições de I/O nos comentários (INPUTS/OUTPUT) das suas StateMachine_DAGs são o que o Glue utilizará para inferir o fluxo de dados.
Classificação de Dados: Deve contemplar Catálogo/Discovery usando dados sensíveis (ex: PII) diretamente no Glue e aplique Tags de Negócio (ex: pedidos com tag Core Domain).

#### 2.3. Script de Execução e Validação

- Habilite e Suba as StateMachine_DAGs: Habilite as três StateMachine_DAGs no AWS Step Functions.
- Execute a StateMachine_DAG BRONZE: Isso simulará a criação e o upload dos arquivos conforme pasta ../csv_exports na camada BRONZE do S3.
- Execute a StateMachine_DAG SILVER: Ela lerá os arquivos BRONZE, executará os joins e gerará o FATO_PEDIDO_CORRELACIONADO.parquet na camada SILVER.
- Execute a StateMachine_DAG GOLD: Ela lerá a SILVER e gerará os dois agregados (BI e ML) na camada GOLD.
- Verifique o Glue: Após a conclusão das StateMachine_DAGs, execute a ingestão do AWS Step Functions e do S3 no Glue.
- Data Catalog: Se os ativos de dados (7 Bronze + 1 Silver + 2 Gold) foram descobertos no S3 com seus respectivos schemas.
- Lineage: Ao clicar no ativo AGREGADO_FLUXO_DE_CAIXA_MENSAL, você deve conseguir rastrear sua origem até a FATO_PEDIDO_CORRELACIONADO, e desta até os ativos originais.
- Discovery: A descrição da StateMachine_DAG e das Tasks deve aparecer nos metadados, facilitando a busca por termos como "Forecast" ou "Risco Cambial".


## Formato de Saída
Execute cada uma das tarefas definidas acima, sempre validando se os arquivos gerados estão de acordo com as instruções.

## Conclusão
Criar o arquivo README.md que deve informar sobre toda a implementação prevista nesta etapa para a solução.
Informar no arquivo README.md quais devem ser os scripts executados em ordem para subir os diversos componentes desta etapa da solução.
