# FinanceControl API 💰

Repositório do projeto **FinanceControl**, uma API robusta desenvolvida em **Java** e **Spring Boot** para controle financeiro pessoal e de pequenos negócios (como oficinas mecânicas). O foco principal é a organização de lançamentos (Receitas/Despesas) vinculados a usuários cadastrados.

## 🛠️ Tecnologias e Ferramentas
* **Java 17** (Linguagem principal)
* **Spring Boot 3** (Framework para API)
* **IntelliJ IDEA** (IDE de desenvolvimento)
* **Maven** (Gerenciador de dependências e build)
* **PostgreSQL** (Banco de dados relacional)
* **Spring Data JPA** (Persistência de dados)
* **Bean Validation (@Valid)** (Integridade dos dados)
* **Lombok** (Produtividade no código)
* **Postman** (Testes de endpoints)

## 🚀 Estado Atual do Projeto
Atualmente, a API possui as seguintes funcionalidades implementadas e testadas:
* **CRUD de Usuários:** Cadastro com validação de e-mail único, CPF (11 dígitos) e campos obrigatórios.
* **Gestão de Lançamentos:** Registro de movimentações financeiras vinculadas a um usuário existente através de DTOs.
* **Arquitetura DTO:** Separação total entre as entidades do banco de dados e os dados de entrada/saída (Request/Response DTO), garantindo que senhas e dados sensíveis não sejam expostos.
* **Tratamento de Erros Global:** Centralizador de exceções (@RestControllerAdvice) que retorna mensagens amigáveis em português para:
    * Campos vazios ou que violam regras de tamanho (400 Bad Request).
    * Tentativa de cadastro de e-mails duplicados.
    * IDs de usuários não encontrados no sistema.
    * Erros de sintaxe no JSON ou valores de Enums incorretos.

## 📋 Como Testar (Exemplo de JSON)
### Salvar Lançamento (`POST /lancamentos`)
```json
{
    "tipo": "DESPESA",
    "valor": 150.00,
    "dataVencimento": "2026-03-30",
    "descricao": "Troca de óleo - Cliente X",
    "categoria": "Oficina",
    "usuarioId": 1
}
```

## 📈 Próximos Passos

 * [ ] Implementar a listagem de lançamentos com filtros por tipo (Receita/Despesa).

 * [ ] Adicionar autenticação e segurança com Spring Security & JWT.

 * [ ] Criar relatórios de saldo mensal por usuário.

* [ ] Implementar a funcionalidade de "Soft Delete" para usuários e lançamentos.

## 💡 Como rodar o projeto

   1. Clone o repositório: 
```bash
    git clone [https://github.com/samrickbr/financeControl.git] (https://github.com/samrickbr/financeControl.git)
```
   2. Configure as credenciais do seu PostgreSQL no arquivo `application.properties`.

No arquivo `src/main/resources/application.properties`, configure as seguintes propriedades:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
   3. Execute o projeto via IntelliJ ou terminal:
```Bash
    mvn spring-boot:run
```
Desenvolvido por [Ricardo Rick](https://github.com/samrickbr) 🛠️💻