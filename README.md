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

💡 Ideal para cenários que exigem controle e histórico de mudanças

---

### ⚠️ Tratamento de Erros

* Uso de `@RestControllerAdvice`
* Respostas padronizadas para erros de validação e negócio
* Mensagens claras para o cliente da API

---

## 📡 Exemplos de Uso da API

### ➕ Criar usuário

**POST /usuarios**

```json
{
  "nome": "Rick",
  "email": "rick@email.com",
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
  "categoria": "Oficina",
  "usuarioId": 1
}
```

---

### ✏️ Atualizar lançamento (com auditoria)

**PUT /lancamentos/{id}**

```json
{
  "descricao": "Troca de óleo - Revisão",
  "valor": 180.00,
  "dataVencimento": "2026-04-05",
  "categoria": "Oficina",
  "tipo": "RECEITA",
  "usuarioId": 1,
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
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
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
* [ ] Autenticação com Spring Security + JWT
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
