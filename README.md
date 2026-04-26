# 💰 FinanceControl API

API REST desenvolvida em **Java + Spring Boot** para controle financeiro pessoal e de pequenos negócios (ex: oficinas, prestadores de serviço).

O sistema permite o gerenciamento completo de usuários e lançamentos financeiros (**receitas e despesas**), com foco em **organização, rastreabilidade e integridade dos dados**.

---

## 🚀 Objetivo do Projeto

Este projeto foi desenvolvido com foco em:

* Aplicar boas práticas de desenvolvimento backend
* Implementar arquitetura em camadas (Controller, Service, Repository)
* Garantir validação e consistência dos dados
* Simular regras reais de negócio (auditoria, rastreabilidade e controle financeiro)

---

## 🛠️ Tecnologias Utilizadas

* Java 17+
* Spring Boot 3.4.2
* Spring Data JPA
* Spring Security
* Auth0 JWT
* PostgreSQL
* Maven
* Bean Validation (`@Valid`, `@NotNull`, etc.)
* BCrypt (criptografia de senha)
* Jackson (serialização/desserialização JSON)
* Lombok
* Postman (testes de API)
* SpringDoc OpenAPI 2.8.5

---

## 🧱 Arquitetura do Projeto

O projeto segue o padrão de **arquitetura em camadas**, com separação clara de responsabilidades:

```text
src/main/java/samrick/financeControl/
├── audit/                      # Rastreabilidade e log de alterações
├── config/                     # Configurações da aplicação (Swagger, etc.)
├── controller/                 # Endpoints REST
├── dto/                        # Request/Response utilizando Java Records
├── exceptions/                 # Definição de exceções personalizadas
├── infra/                      # Infraestrutura técnica do sistema
│   ├── security/               # Configurações de JWT e Filtros de Segurança
│   ├── utils/                  # Utilitários e Validadores (ex: @Cpf)
│   └── RestExceptionHandler    # Tratamento global de erros
├── mapper/                     # Conversão entre Entidades e DTOs (MapStruct/Manual)
├── model/                      # Entidades JPA (Mapeamento do Banco de Dados)
├── repository/                 # Interfaces de acesso ao banco (Spring Data JPA)
└── service/                    # Regras de negócio e lógica da aplicação
```

* ✅ Uso de DTO para evitar exposição de entidades
* ✅ Validações no Service para garantir regras de negócio
* ✅ Uso de Exception Handler para padronização de erros

---

## 🔥 Funcionalidades Implementadas

### 👤 Usuários e Segurança

* ✅ **Cadastro Seguro:** Validação de e-mail único e criptografia de senhas com BCrypt.
* ✅ **Integridade de Dados:** Validação de CPF via Annotation customizada e tratamento global de exceções.
* ✅ **Autenticação JWT:** Proteção de rotas com Stateless Authentication via Bearer Token.
* ✅ **RBAC (Role-Based Access Control):** Controle de acesso granular por perfis (ADMIN e USER).
* ✅ **Reativação Inteligente:** Sistema que detecta categorias desativadas e as reativa automaticamente em novos cadastros, evitando duplicidade e lixo no banco.
* ✅ **Auditoria de Categorias:** Rastreabilidade completa em operações de edição e desativação, exigindo justificativa e vinculando ao usuário logado.

## 💸 Gestão Financeira e Relatórios

* ✅ **Lançamentos:** Cadastro de receitas/despesas com conversão segura de ENUMs e associação automática ao usuário logado.
* ✅ **Resumo Anual Completo:** Endpoint de alta performance que entrega entradas, saídas e saldo mensal, além do fechamento anual.
* ✅ **Auditoria:** Rastreabilidade total com logs de alteração e Soft Delete para preservação de histórico.

## 🛠️ Facilidades para o Desenvolvedor

* ✅ **Swagger UI:** Documentação interativa para teste imediato de todos os endpoints.

## 🛠️ Como Testar (Swagger)
1. Inicie a aplicação.
2. Acesse `http://localhost:8080/swagger-ui/index.html`.
3. Utilize o endpoint `/login` para gerar um token.
4. Clique no botão **Authorize** e cole o token para liberar as rotas protegidas.

---

### 📜 Auditoria de Dados

#### O sistema foi projetado seguindo as melhores práticas de segurança e rastreabilidade:

***Auditoria em Duas Camadas:***

* ***Log de Eventos:*** Cada operação crítica (Criação, Edição, Exclusão) gera um registro em uma tabela separada de logs, detalhando a ação, o recurso afetado e quem a executou.

* ***Rastro de Entidade (Shadow Fields):*** As entidades possuem os campos `data_ultima_alteracao` e `usuario_ultima_alteracao`, permitindo saber o estado atual do registro diretamente na tabela principal.

* ***Soft Delete:*** A exclusão de usuários e lançamentos não remove os dados fisicamente do banco de dados. Utilizamos a anotação `@SQLDelete` para apenas marcar o registro como inativo (`ativo = false`), preservando o histórico para auditorias futuras.

💡 Ideal para cenários que exigem controle e histórico de mudanças

---

### 📊 Inteligência de Dados

* ***Resumo Anual Consolidado:*** Endpoint que processa e agrupa lançamentos por mês diretamente via JPQL.
* ***Cálculo Automático de Saldo:*** O sistema entrega o total de entradas, saídas e o saldo mensal, além do fechamento anual acumulado, otimizando a performance para o Front-end.

---

### ⚠️ Tratamento de Erros

* Uso de `@RestControllerAdvice`
* Respostas padronizadas para erros de validação e negócio
* Mensagens claras para o cliente da API

---

## 🔐 Segurança e Autenticação

A API utiliza ***Spring Security*** com ***JWT (JSON Web Token)*** para controle de acesso. As senhas são criptografadas utilizando o algoritmo **BCrypt**.

* ***Padrão DTO (Data Transfer Objects):*** Blindagem total das entidades. Dados sensíveis como senhas e estruturas internas do banco nunca são expostos no JSON.
* ***Validação de CPF Customizada:*** Implementação de Annotation `@Cpf` e classe de utilitário em `infra/utils` para garantir a integridade dos documentos antes de chegarem ao banco.

### Configuração
O projeto utiliza uma chave secreta para geração e validação dos tokens. Em ambiente de desenvolvimento, certifique-se de que a variável de ambiente ou propriedade esteja configurada:
- `api.security.token.secret`: Sua chave secreta (ex: ${JWT_SECRET:12345678})

### Endpoints da API

## 🔐 Autenticação e Usuários

| Método | Endpoint         | Descrição                                | Acesso  |
|:-------|:-----------------|:-----------------------------------------|:--------|
| POST   | `/login`         | Autentica usuário e devolve o Token JWT  | Público |
| POST   | `/usuarios`      | Cadastro de novos usuários               | Público |
| DELETE | `/usuarios/{id}` | Exclusão lógica(Soft Delete) de usuários | Admin   |


## 💸 Lançamentos Financeiros

| Método | Endpoint                       | Descrição                                                  | Acesso      |
|:-------|:-------------------------------|:-----------------------------------------------------------|:------------|
| GET    | `/lancamentos`                 | Lista lançamentos(Dono vê os seus, Admin vê todos          | User/Admin  |
| POST   | `/lancamentos`                 | Cria novo lançamento vinculado ao token                    | User/Admin  |
| PUT    | `/lancamentos/{id}`            | Atualiza com justificativa(Dono ou Admin)                  | User/Admin  |
| GET    | `/lancamentos/busca-categoria` | Filtra lançamentos por nome de categoria(Case Insensitive) | User/Admin  |
| POST   | `/categorias`                  | Cria ou reativa categoria(Case Insensitive)                | User/Admin  |
| PUT    | `/categorias/{id}`             | Atualiza nome com justificativa e log                      | User/Admin  |
| PUT    | `/categorias/{id}/desativar`   | Soft Delete (inativação) com justificativa                 | User/Admin  |

## 📊 Relatórios e Inteligência

| Método | Endpoint                         | Descrição                                                    | Acesso     |
|:-------|:---------------------------------|:-------------------------------------------------------------|:-----------|
| GET    | `/relatorios/financeiro`         | Resumo rápido: Total, Entradas, Saídas e Saldo Anual         | User/Admin |
| GET    | `/relatorios/resumo-anual`       | Relatório detalhado: Evolução mensal e Saldo Final do ano    | User/Admin |
| GET    | `/relatorios/categoria`          | Distribuição de gastos agrupados por categoria               | User/Admin |

---

## 🚀 Como Testar no Postman

Para acessar as rotas protegidas (como listagem de lançamentos), você deve seguir estes passos:

1. **Obter o Token:**
    - Realize uma requisição `POST` para `/login` enviando `email` e `senha` no corpo (JSON).
    - Copie o valor da chave `token` retornado no corpo da resposta.

2. **Configurar a Autorização:**
    - Na requisição que deseja testar (ex: `GET /lancamentos`), vá na aba **Auth**.
    - No campo **Type**, selecione **Bearer Token**.
    - Cole o token copiado no campo **Token**.

3. **Verificação Manual (Headers):**
    - Caso prefira configurar manualmente, vá na aba **Headers**.
    - Adicione a chave `Authorization` com o valor `Bearer <seu_token_aqui>` (respeitando o espaço após a palavra Bearer).

> **Nota:** Se o token estiver ausente, expirado ou for inválido, a API retornará um erro **403 Forbidden** ou **401 Unauthorized**.

---

## 📡 Exemplos de Uso da API

### ➕ Criar usuário

**POST `/usuarios`**

```json
{
  "nome": "Nome_usuario",
  "email": "seu_email@email.com",
  "senha": "123456",
  "cpf": "12345678901",
  "profissao": "Desenvolvedor"
}
```

---

### 💸 Criar lançamento

**POST `/lancamentos`**

```json
{
   "tipo": "RECEITA",
   "valor": 150,
   "dataLancamento": "2026-04-03",
   "dataVencimento": "2026-05-03",
   "dataPagamento": "2026-04-30",
   "descricao": "Serviço realizado",
   "categoria": "Oficina"
}
```

**Retorno**

```json
{
  "id": 8,
  "tipo": "RECEITA",
  "valor": 150,
  "dataLancamento": "2026-04-03",
  "dataVencimento": "2026-05-03",
  "dataPagamento": "2026-04-30",
  "descricao": "SERVIÇO REALIZADO",
  "categoria": "OFICINA",
  "nomeUsuario": "Administrador",
  "dataUltimaAlteracao": null,
  "usuarioUltimaAlteracao": null
}
```

**Exemplo de erro 400 - Dados incompletos
```json
[
  {
    "timestamp": "03/04/2026 18:04:00",
    "status": 400,
    "error": "Erro de Validação: categoria",
    "message": "O campo deve ter no mínimo 5 caracteres",
    "path": ""
  }
]
```
---

### ✏️ Atualizar lançamento (com auditoria)

**PUT `/lancamentos/{id}`**

```json
{
   "tipo": "DESPESA",
  "valor": 180.00, 
  "dataVencimento": "2026-04-05",
  "dataPagamento": "2026-04-05",
  "descricao": "Troca de óleo - Revisão",
  "categoria": "Oficina",
  "justificativa": "Atualização de valor conforme tabela 2026"
}
```
---
**PUT `/categorias/{id}`**
```json
{
  "categoria": "MORADIA",
  "justificativa": "Ajuste de nomenclatura para padronização do relatório"
}
```
---
### 📡 Exemplo de Resumo Anual
**GET `/relatorios/resumo-anual?ano=2026`**
```json
{
   "meses": [
      { "mes": 4, "entradas": 1780.00, "saidas": 730.80, "saldoMensal": 1049.20 }
   ],
   "totalEntradasAno": 1780.00,
   "totalSaidasAno": 730.80,
   "saldoFinalAno": 1049.20
}
```
## Retorno do Endpoint

```json
{
  "meses": [
    {
      "mes": 3,
      "entradas": 0,
      "saidas": 1680,
      "saldoMensal": -1680
    },
    {
      "mes": 4,
      "entradas": 1780,
      "saidas": 730.8,
      "saldoMensal": 1049.2
    }
  ],
  "totalEntradasAno": 1780,
  "totalSaidasAno": 2410.8,
  "saldoFinalAno": -630.8
}
```

## 💡 Notas de Implementação

* Filtragem de Dados: Todos os endpoints de listagem e relatórios aplicam automaticamente o filtro pelo ID do usuário extraído do Token JWT.
* Proteção de Dados: Os retornos utilizam `LancamentoResponseDTO` e `RelatorioAnualCompletoDTO`, garantindo que informações sensíveis não sejam expostas.
* Validação Automática: O endpoint de cadastro de usuários valida o formato e a veracidade do CPF antes de processar a requisição.

---

## ▶️ Como Executar o Projeto

## 📋 Pré-requisitos

***Antes de começar, você vai precisar ter instalado em sua máquina as seguintes ferramentas:***

* **Java JDK 17:** Versão mínima necessária para suporte a Records e padrões de projeto modernos.

* **Maven 3.9+:** Gerenciador de dependências e build do projeto.

* **PostgreSQL 15+:** Banco de dados relacional (Certifique-se de que o serviço está rodando na porta 5432 ou 5433 conforme seu .properties).

* **IDE de sua preferência:** Recomendado IntelliJ IDEA ou VS Code com extensões Java.

* **Postman ou Insomnia:** Para realizar os testes nos endpoints da API.

### 1. Clonar o repositório

```bash
git clone https://github.com/samrickbr/financeControl.git
```

---

### 2. Configurar o banco de dados

No arquivo:

```properties
src/main/resources/application.properties
```

Configure:

```properties

# Conexão com o Banco
spring.datasource.url=jdbc:postgresql://localhost:5433/finance_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Estratégia do Hibernate (Cria as tabelas automaticamente)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

#Chave de segurança do token de validação
api.security.token.secret=${JWT_SECRET:frase-secreta-de-senha-forte-aqui-123}

```

---

### 3. Executar a aplicação

```bash
mvn spring-boot:run
```

## 🔑 Acesso Padrão: 

Para facilitar o teste e a avaliação do projeto, implementamos um Database Seeder que prepara o ambiente automaticamente.

1. Primeira Execução: Ao rodar a aplicação pela primeira vez, o sistema detecta se a base de dados está vazia e cria automaticamente um usuário Administrador padrão.

2. Credenciais de Acesso:

* Login: `admin@finance.com`

* Senha: `admin123` (Criptografada com BCrypt no banco)

---

## 📈 Próximas Melhorias

* ✅ Implementar Logs de Auditoria para todas as entidades críticas 
* ✅ Validação de CPF 
* ✅ Autenticação com Spring Security + JWT
* ✅ Controle de acesso por Perfis(ADMIN, USER)
* ✅ Relatórios financeiros por usuário (mensal/anual com saldo acomulado)
* ✅ Soft Delete para usuários e lançamentos 
* ✅ Documentação com Swagger/OpenAPI
* [ ] Implementação de Agentes de IA para análise preditiva de gastos (LangChain/Spring AI)
* [ ] Dashboard Front-end para visualização dos gráficos anuais

---

## 👨‍💻 Autor

**Rick (Ricardo)**

Desenvolvedor Backend Java Focado em APIs REST com Spring Boot

🔗 GitHub: https://github.com/samrickbr

---

## 📌 Considerações Finais

Este projeto foi desenvolvido com foco em:

* Código limpo (Clean Code)
* Boas práticas de API REST
* Estrutura escalável
* Simulação de regras reais de negócio

---

🚀 Projeto em evolução contínua.
