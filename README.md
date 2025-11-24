# 🛡️ SafeNotes API - Java Spring Boot & AppSec

Uma API REST robusta desenvolvida com foco em **Security by Design**, demonstrando a implementação prática de defesa contra as vulnerabilidades mais críticas do OWASP Top 10 em um ambiente Spring Boot.

## 🚀 Tecnologias Utilizadas

* **Java 17** (LTS)
* **Spring Boot 3.5.7**
* **Spring Security 6** (Autenticação e Autorização)
* **PostgreSQL** (Banco de Dados Relacional)
* **Spring Data JPA / Hibernate** (Persistência)
* **BCrypt** (Algoritmo de Hashing de Senhas)
* **Lombok** (Produtividade e redução de boilerplate)
* **Maven** (Gerenciamento de dependências)

## 🔒 Implementações de Segurança (AppSec)

Este projeto foi desenhado para identificar e corrigir falhas comuns de segurança em APIs:

### 1. Proteção contra IDOR (Insecure Direct Object Reference)
Implementação de lógica rigorosa de **Propriedade (Ownership)** no `NoteService`.
* **Criação Segura:** Notas criadas são automaticamente vinculadas ao utilizador autenticado.
* **Acesso Restrito:** Tentativas de ler (`GET`) ou apagar (`DELETE`) notas de outros utilizadores são bloqueadas com validação no servidor, retornando `403 Forbidden`.
* **Listagem Filtrada:** O endpoint `GET /notes` retorna apenas os dados pertencentes ao utilizador logado.

### 2. Prevenção de Information Disclosure (Vazamento de Dados)
* **DTO Pattern:** Uso de `UserResponseDTO` no registro para garantir que dados sensíveis não sejam retornados na resposta da API.
* **Defesa em Profundidade:** Uso da anotação `@JsonIgnore` na entidade `User` para garantir que o hash da senha nunca seja serializado para JSON, mesmo em casos de erro ou manutenção futura.

### 3. Gestão Segura de Credenciais
* **Hashing:** Senhas são armazenadas usando **BCrypt** (`PasswordEncoder`), nunca em texto puro.
* **Infraestrutura:** A senha do banco de dados não está hardcoded no código fonte. É injetada via Variável de Ambiente (`${DB_PASSWORD}`), prevenindo vazamento de segredos no controle de versão.

## ⚙️ Como Rodar o Projeto

### Pré-requisitos
* Java 17 instalado.
* PostgreSQL a rodar na porta `5432`.
* Banco de dados criado com o nome `safenotes_db`.

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/SEU_USUARIO/SafeNotes.git](https://github.com/SEU_USUARIO/SafeNotes.git)
    cd SafeNotes
    ```

2.  **Configure a Senha do Banco:**
    Por segurança, o projeto espera a senha do banco numa variável de ambiente.
    * **Linux/Mac:** `export DB_PASSWORD=sua_senha_postgres`
    * **Windows (PowerShell):** `$env:DB_PASSWORD="sua_senha_postgres"`
    * *(Alternativa para IDE):* Configure a variável `DB_PASSWORD` nas configurações de execução (Run Configuration).

3.  **Execute a aplicação:**
    Não é necessário ter o Maven instalado, use o Wrapper do projeto:
    ```bash
    ./mvnw spring-boot:run
    ```
    *(No Windows: `.\mvnw.cmd spring-boot:run`)*

4.  **Acesse:**
    A API estará disponível em: `http://localhost:8081`

## 🧪 Documentação da API (Endpoints)

| Método | Endpoint | Descrição | Auth Necessária? |
| :--- | :--- | :--- | :--- |
| `POST` | `/register` | Cria um novo utilizador (Senha é criptografada) | ❌ Não |
| `POST` | `/notes` | Cria uma nota vinculada ao utilizador logado | ✅ Sim (Basic Auth) |
| `GET` | `/notes` | Lista apenas as notas do utilizador logado | ✅ Sim (Basic Auth) |
| `GET` | `/notes/{id}` | Lê uma nota específica (Valida propriedade) | ✅ Sim (Basic Auth) |
| `DELETE` | `/notes/{id}` | Apaga uma nota específica (Valida propriedade) | ✅ Sim (Basic Auth) |
| `DELETE` | `/notes` | Apaga todas as notas do utilizador logado | ✅ Sim (Basic Auth) |