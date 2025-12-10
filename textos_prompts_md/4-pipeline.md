# 📦 Descrição do Projeto e Objetivo do Pipeline

Este repositório contém um microsserviços do projeto **Importau**

O objetivo é criar um **pipeline de CI/CD simples** que possa ser **executado tanto localmente quanto em ambiente de CI** (por exemplo: GitHub Actions), garantindo a execução automatizada de:
1. Build dos microserviços
2. Testes unitários de cada microserviço
3. Testes de integração de ponta a ponta (Cucumber)
4. Opção futura de implantação (deploy) para ambientes de staging/produção

---

## 🧩 Estrutura Geral do Repositório

```
importau/
├── backend/
├── testes_integracao/
│   └── pom.xml
└── .github/
    └── workflows/
        └── ci.yml
```

---

## 🧠 Requisitos Técnicos do Pipeline

### 1. Linguagem e Build
- Linguagem: **Java 17**
- Ferramenta de build: **Maven Wrapper (`./mvnw`)**
- O pipeline deve configurar o ambiente Java antes de executar testes.

### 1. Testes Unitários
- Cada microserviço deve rodar seus próprios testes unitários com.
- Executar sequencialmente ou em paralelo, conforme suporte da plataforma.

### 2. Testes de Integração
- Localizados na pasta ` testes_integracao/`
- Criar ou melhorar os arquivo .feature com os cenários Behavior Driven Development
- Devem ser executados com:
  ```bash
  mvn clean test
  ```
- Usam **Cucumber** para validações entre componentes.

### 3. Testes de User Interface usando Selenium
- Localizados na pasta ` testes_integracao/`
- Criar ou melhorar os arquivo .feature com os cenários Behavior Driven Development
- Usam **Cucumber** para validações chamar o Selenium.
- Usam **Selenium** headless em container para validações usando o Swagger UI.
- Para o **Selenium** headless deve ser configurado o screenshot das telas, organizando em pastas no formato ano_mês_dia_hora_min: AAAA_MM_DD_HH_MM.

### 4. Estrutura de Jobs
- Um job principal chamado `build-and-test`
- Etapas:
  - Checkout do código
  - Verifique se há Dockerfile no projeto
  - Gere o build da imagem 
  - Execução do continer
  - Execução dos testes unitários dos microserviços executados no conteiner
  - Execução dos testes de integração Cucumber
  - Execução dos testes de Interface de Usuário usando Selenium
- Opcionalmente, um job futuro de deploy.

### 5. Compatibilidade
- O pipeline deve poder rodar:
  - Localmente via **act (GitHub Actions local runner)**
  - Em **GitHub Actions** (estrutura YAML padrão)

---

## ⚙️ Expectativa de Saída do Pipeline

- Um arquivo YAML de pipeline (ex: `.github/workflows/ci.yml`) que:
  - Faça o checkout do repositório
  - Gere o build da imagem e execute o contêiner
  - Execute todos os testes unitários de cada microserviço
  - Execute os testes de integração (Cucumber)
  - Execute os testes de interface do usuário (Selenium)
  - Exiba o status final do build (sucesso/falha)
  - Use etapas nomeadas e com logs legíveis

---

## 🧩 Requisitos Opcionais (para evolução futura)

- Cache Maven (`actions/cache` ou similar)
- Geração de relatórios HTML
- Build paralelo dos microserviços
- Deploy automatizado para ambiente de staging no EKS/EC2
- Integração com ferramentas de code quality (SonarQube)

---

## ✅ Objetivo do Prompt

Gerar um pipeline funcional, legível e portável (em YAML), com foco em **simplicidade, clareza e execução local via `act`** ou em **ambiente Github Actions**, de forma que o time de desenvolvimento possa:
- Rodar testes automaticamente em cada commit/pull request
- Validar integração completa antes de deploy
- Ter logs claros para depuração de falhas

---

## 🧭 Exemplo de comando desejado para execução local

```bash
act workflow_dispatch
```

---

## 💬 Instrução para o Amazon Q Developer

> Com base nesta descrição (`pipeline.md`), gere um arquivo de pipeline CI/CD YAML que atenda aos requisitos acima, priorizando simplicidade e compatibilidade com execução local (`act`) e com AWS CodeBuild.

> Efetuar melhorias nos códigos para criar, validar e garantir a executação dos testes unitários, integração e de interface do usuário (usando selenium). Executar e validar se os testes estão funcionando.
