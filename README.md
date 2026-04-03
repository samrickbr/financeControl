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
* Spring Boot 3
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

---

## 🧱 Arquitetura do Projeto

O projeto segue o padrão de **arquitetura em camadas**, com separação clara de responsabilidades:

```text
src/main/java/com/samrick/financecontrol/
├── audit/        # Rastreabilidade e log de alterações
├── controller/   # Endpoints REST
├── dto/          # Request e Response (Records)
├── mapper/       # Conversão entre Entidades e DTOs
├── service/      # Regras de negócio
├── repository/   # Acesso ao banco de dados
├── model/        # Entidades JPA
├── infra/        # Tratamento global de exceções
├── exceptions/   # Classes de erro padronizadas
└── config/       # Configurações da aplicação
```

---

## 🔥 Funcionalidades Implementadas

### 👤 Usuários

* Cadastro com validação de e-mail único
* Criptografia de senha com BCrypt

---

### 💸 Lançamentos Financeiros

* Cadastro de receitas e despesas
* Associação com usuário
* Validação de dados obrigatórios
* Conversão segura de tipos (ENUM)

---

### 📜 Auditoria de Dados

Sistema de rastreabilidade com:

* Registro de ações: **CADASTRAR, ALTERAR, EXCLUIR**
* Armazenamento do estado anterior (JSON)
* Justificativa obrigatória para alterações
* Validação de Propriedade: O sistema impede que um usuário altere lançamentos de terceiros, garantindo a privacidade dos dados.
* Hierarquia de Acesso: Administradores possuem permissão para gerenciar e auditar registros de qualquer usuário.

💡 Ideal para cenários que exigem controle e histórico de mudanças

---

### ⚠️ Tratamento de Erros

* Uso de `@RestControllerAdvice`
* Respostas padronizadas para erros de validação e negócio
* Mensagens claras para o cliente da API

---

## 🔐 Segurança e Autenticação

A API utiliza **Spring Security** com **JWT (JSON Web Token)** para controle de acesso. As senhas são criptografadas utilizando o algoritmo **BCrypt**.

### Configuração
O projeto utiliza uma chave secreta para geração e validação dos tokens. Em ambiente de desenvolvimento, certifique-se de que a variável de ambiente ou propriedade esteja configurada:
- `api.security.token.secret`: Sua chave secreta (ex: ${JWT_SECRET:12345678})

### Endpoints de Autenticação
| Método | Endpoint    | Descrição                               | Acesso  |
| :--- |:------------|:----------------------------------------|:--------|
| POST | `/login`    | Autentica usuário e devolve o Token JWT | Público |
| POST | `/usuarios` | Cadastro de novos usuários              | Público |

### Endpoints Gerais
| Método | Endpoint            | Descrição                                         | Acesso     |
|:-------|:--------------------|:--------------------------------------------------|:-----------|
| GET    | `/lancamentos`      | Lista lançamentos(Dono vê os seus, Admin vê todos | User/Admin |
| POST   | `/lancamentos`      | Cria novo lançamento vinculado ao token           | User/Admin |
| PUT    | `/lancamentos/{id}` | Atualiza com justificativa(Dono ou Admin)         | User/Admin |
| DELETE | `/usuarios/{id}`    | Exclusão de usuários (Admin)                      | Admin      |
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

**POST /usuarios**

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

**POST /lancamentos**

```json
{
  "tipo": "RECEITA",
  "valor": 150.00,
  "dataVencimento": "2026-04-10",
  "descricao": "Serviço realizado",
  "categoria": "Oficina"
}
```

---

### ✏️ Atualizar lançamento (com auditoria)

**PUT /lancamentos/{id}**

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

## ▶️ Como Executar o Projeto

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

---

## 📈 Próximas Melhorias

* [x] Implementar Logs de Auditoria para todas as entidades críticas
* [ ] Validação de CPF
* [x] Autenticação com Spring Security + JWT
* [x] Controle de acesso por Perfis(ADMIN, USER)
* [ ] Relatórios financeiros por usuário (mensal/anual)
* [ ] Soft Delete para usuários e lançamentos
* [ ] Documentação com Swagger/OpenAPI

---

## 👨‍💻 Autor

**Rick (Ricardo)**
Desenvolvedor Backend Java / Spring Boot

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
