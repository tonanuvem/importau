# Implementação dos Backends da Solução conforme Arquitetura e garantindo cobertura de teste a partir de requisitos

## Função
Você é um Desenvolvedor Backend e Testador de Software experiente. Sua missão é implementar a arquitetura técnica SOMENTE DO CORE DOMAIN E SUPPORTING DOMAIN.

## Leia o documento 2-GENAI-ARQUITETURA.MD para fazer sua implementação, e utilize também o documento TESTES.MD para garantir qualidade.

Caso não encontre os arquivos, interromper o processamento e confirmar com o Aluno para procurar nas pastas

## Instruções de Análise

### 1. Resumo Executivo
Implementar a solução baseada em microserviços, com vários backends especializados. A arquitetura foca em escalabilidade, independência de implantação, observabilidade e facilidade de teste.

### 2. Objetivos
- Fornecer funcionalidade responsiva aos usuários.
- Separar responsabilidades por domínio.
- Implementar usando pouco código e simples de entender
- Inserir explicações no próprio código através de comentários
- Permitir deploy independente e escalonamento por serviço.
- Garantir segurança (autenticação/autorização) e resiliência (retry, circuit breaker).
- Facilitar integração entre frontend e APIs via HTTP/REST.

### 3. Principais Microserviços com Endpoints (REST) e também consumindo Eventos

- Criar todos os microsserviços com OpenAPI/Swagger.
- Usar Eventos para comunicação assíncrona e desacoplada.
- Os mesmos campos do JSON passados em chamadas síncronas também podem estar disponíveis para serem usados em chamadas assíncronas.
- Criar cenários de Teste de integração (BDD — Gherkin) para comunicações síncronas e assíncronas
- Implementar Modelos de Dados simples de cada componente e documentar através de comentários em cada código 
- Criar scripts simples de inicialização dos dados nos respectivos bancos de dados

### 4. Integração Frontend ↔ Backend (padrões e boas práticas)
- Utilizar Autenticação para garantir segurança.
- Usar retries, service mesh e circuit breaker para chamadas críticas.
- Proteger dados sensíveis, ao usar redirect para provedores ou integrar via sessão segura.
- Facilidade de uso com HTTP nos Backends ( não usar HTTPS para economia de custos).
- Proteção contra CSRF, XSS e SQL injection.
- Tokens com expiração curta e refresh token seguro.
- Logs sem dados sensíveis (compliance com melhores práticas).

### 5. Tecnologias dominadas pela equipe técnica da empresa (ex: open source)
- Microservices: Python (FastAPI), Node.js (Express), Java (Quarkus ou Spring Boot)
- Databases: Mongo, PostgreSQL, Redis (cache/sessions)
- Message Broker: Apache Kafka
- Search: Elasticsearch
- Observability: Prometheus + Grafana, Jaeger, ELK/EFK
- Auth: Keycloak (OpenID Connect)
- CI/CD: GitHub Actions
- Containerização: Docker + Kubernetes (k8s) para orquestração

### 6. Deploy, Infraestrutura, Observability e Operação
- Criar script para executar toda a solução
- Deploy em Kubernetes e docker-compose.
- Criar pipelines CI/CD para cada ambiente.
- Backups regulares para bancos de dados; restore testado.
- Autoscaling horizontal para serviços stateless (frontend, backends).
- Possibilidade de usar istio service mesh no K8S
- Métricas por serviço (latência, erros, throughput).
- Alertas (CPU, memória, error rate, queue lag).
- Tracing distribuído para diagnosticar latência entre serviços.

## Formato de Saída
Execute cada uma das tarefas a seguir, sempre validando se os arquivos gerados estão de acordo com as instruções de análise

### 1. Criar a Estrutura de Pastas (exemplo monorepo)
```
/importau
  /backend
    /backend1
    /backend2
    /backend3
  /infra
    /k8s
    /docker-compose
      /singlenode
      /swarm
  /scripts
  /testes_integracao
```

### 2. Criar um projeto em Java usando Cucumber para implementar contemplar os testes de integração seguindo melhores práticas de BDD:
- Os arquivos devem ser gerados em /importau/testes_integracao
- Usar os cenários de testes em portugues
- Contemplar o uso de Esquema de Cenários, incluindo exemplos com valores no formato tabular para serem usados nos testes
- No final desta etapa, executar o projeto com "mvn test", porém como os microsserviços ainda não foram criados, esses testes devem falhar
- Verificar e corrigir somente erros relacionados ao Cucumber
- Criar script para disparar teste de integração em /importau/scripts

### 3. Criar o código de cada um dos backends, contemplando os testes unitários seguindo melhores práticas de TDD:
- Os arquivos de cada backend devem ser gerados em /importau/backend
- Os diversos microsserviços devem ser criados usando Docker, ou seja, deve ser criado um Dockerfile
- Cada backend deve acessar seu próprio banco de dados. Cada banco de dados deve ser executado em docker.
- Deve ser habilitado o CORS para redirecionamento aos backends
- Deve ser configurado o Content Security Policy (CSP) para que o cabeçalho CSP possa incluir todos os domínios necessários
- Desabilitar o uso do header HSTS para o navegador não forçar HTTPS em um servidor HTTP.
- Deve ser implementado o Swagger UI em todos os backends
- Swagger UI não deve carregar os assets via HTTPS quando a página é servida via HTTP. Portanto, configurar o Swagger para usar URLs relativas
- As ações CRUDs usando implementadas em cada backend devem suportar as requisições de acordo com a documentação do react-admin usando ra-data-simple-rest.
- Cada backend deve usar variaveis de ambiente para suas configurações
- Cada backend deve habilitar o recebimento de chamada em suas rotas utilizandos o IP público (0.0.0.0)
- Cada backend deve disponibilizar uma rota que verifique a sua saúde (por exemplo: /status)
- Os testes unitários devem ser implementados para validar funcionalidades internas de cada microsserviço
- Criar a imagem de cada microsserviço através do comando docker build
- Executar cada microsserviço e verificar se os testes unitários de cada microsserviço está sendo realizado com sucesso
- Quando todos os microsserviços estiverem rodando com sucesso, deve ser adaptado o teste de integracao da pasta /importau/testes_integracao para implementar a chamada ao endpoint de cada um dos microsserviços
- Disparar o teste de integração e garantir que esteja efetivamente validando todos os microsserviços criados na solução, de acordo com os requisitos funcionais e não funcionais definidos no arquivo 1-genai-requisitos.md
- Criar um docker-compose.yml na pasta /infra/docker-compose/singlenode
- Verificar se no docker-compose.yml existe a configuração para executar o banco de dados de cada um dos microsserviços
- Criar o script de "migrations" que vai inserir dados em todos os bancos de dados de cada um dos backends
- Executar a solução, inclusive o script para inserir dados nos bancos de dados, e corrigir automaticamente os erros que forem sendo encontrados
- Criar script para executar a solução em /importau/scripts

### 5. Garantir frontend e os backends estão executando e funcionando corretamente
- Digitando docker ps
- Pesquisar na pasta do projeto /importau e verificar se existe algum código que esteja apontando para localhost, e se for encontrado, ajustar para usar variável de ambiente
- Verificar se as configurações do Swagger UI em cada backend está configurado corretamente para acesso externo e suportando CORS, Content Security Policy (CSP). 
- Verificar se todos os Backends da solução foram implementados, estão rodando e funcionando

## Conclusão
Informar sobre a implementação da solução.
Deve ser exibido o IP público do ambiente (disponível através da URL checkip.amazonaws.com) e em seguida deve ser exibida a URL de acesso ao SWAGGERUI de cada um dos microsserviços.

