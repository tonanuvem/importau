# Criar os arquivos Scripts e também YAMLs com base em imagens oficiais visando posterior execução das Plataformas do projeto

## Função
Você é um Arquiteto de Nuvens e SRE experiente. Sua missão é implementar a arquitetura técnica ATRAVÉS DAS PLATAFORMAS DE INFRAESTRUTURA.

## Leia o documento 2-GENAI-ARQUITETURA.MD e a imagem IMPORTAU_ARQUITETURA_MICROSERVICES.png para entender o projeto e fazer sua implementação usando Cluster SWARM e Kubernetes, além de serviços AWS.

Caso não encontre os arquivos, interromper o processamento e confirmar com o Aluno para procurar nas pastas

## Instruções de Análise

### 1. Resumo Executivo
Implementar a solução baseada em microserviços, com vários backends especializados, tanto em Docker Swarm como em Kubernetes (K8S).
Usar a Estrutura de Pastas (exemplo monorepo)
```
/importau
  /infra
    /aws
    /k8s
    /docker-compose
      /singlenode
      /swarm
  /scripts
```

### 2. Objetivos
- Criar os recursos na AWS através de scripts nas pastas /importau/scripts que usem a AWS CLI
- Criar arquivos TF que possibilitem criar recursos na AWS usando o Terraform
- Criar os arquivos YAMLs nas pastas /importau/infra/k8s e /importau/infra/docker-compose/swarm

### 3. Principais Serviços que devem ter scripts ou arquivos TF para serem criados na AWS:

- EKS: cluster Kubernetes onde serão executados os diversos microsserviços e algumas plataformas necessárias ao funcionamento da solução.
- AWS ApiGateway: será utilizado para verificar as rotas que estão sendo acessadas e direcionar a requisição ao respectivo microsserviço.
- S3: será utilizado para armazenar arquivos CSVs com dados a serem correlacionados e enriquecidos para posterior uso em análise preditiva e dashboards.
- Step Functions: conterá os passos para a criação do pipeline de dados, contemplando as camadas Bronze -> Silver -> Gold.
- CloudWatch: deverá armazenar métricas e logs dos diversos microsserviços e também das diversas plataformas utilizadas na solução.
- AWS X Ray: deverá armazenar o tempo de resposta dos diversos microsserviços da solução.

### 4. Principais Plataformas que devem ter arquivos YAML para serem executadas no ambiente do EKS 

- Kafka: deverá ser usado para comunicação assíncrona entre os microsserviços.
- ArgoCD: deverá ser usado como parte do pipeline CI/CD para atualizar automaticamente as novas versões dos microsserviços.
- OpenMetadata: dever[a ser usado para gerenciar o pipeline de Dados para o ambiente analitico.

### 5. Deploy, Infraestrutura, Observability e Operação
- Criar script para executar toda a solução
- Deploy em Kubernetes e Swarm.
- Criar pipelines CI/CD para cada ambiente.
- Backups regulares para bancos de dados; restore testado.
- Autoscaling horizontal para serviços stateless (frontend, backends).
- Possibilidade de usar istio service mesh no K8S
- Métricas por serviço (latência, erros, throughput).
- Alertas (CPU, memória, error rate, queue lag).
- Tracing distribuído para diagnosticar latência entre serviços.

## Formato de Saída
Execute cada uma das tarefas definidas acima, sempre validando se os arquivos gerados estão de acordo com as instruções.

## Conclusão
Criar o arquico README.md que deve informar sobre toda a implementação prevista nesta etapa para a solução.
Informar no arquivo README.md quais devem ser os scripts executados em ordem para subir os diversos componentes desta etapa da solução.
