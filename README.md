# SafeNotes API - Java Spring Boot & AppSec

Uma API REST robusta desenvolvida com foco em **Security by Design**, demonstrando a implementação prática de defesa contra as vulnerabilidades mais críticas do OWASP Top 10 em um ambiente Spring Boot.

## Tecnologias Utilizadas

* **Java 17** (LTS)
* **Spring Boot 3.3.x**
* **Spring Security 6** (Autenticação e Autorização)
* **PostgreSQL** (Banco de Dados Relacional)
* **Spring Data JPA / Hibernate** (Persistência)
* **SpringDoc OpenAPI / Swagger UI** (Documentação interativa)
* **BCrypt** (Algoritmo de Hashing de Senhas)
* **Lombok** (Produtividade e redução de boilerplate)
* **Maven** (Gerenciamento de dependências)

## Implementações de Segurança (AppSec)

Este projeto foi desenhado para identificar e corrigir falhas comuns de segurança em APIs:

### 1. Proteção contra IDOR (Insecure Direct Object Reference)
Implementação de lógica rigorosa de **Propriedade (Ownership)** no `NoteService`.
* **Criação Segura:** Notas criadas são automaticamente vinculadas ao usuário autenticado via `SecurityContextHolder`.
* **Acesso Restrito:** Tentativas de ler (`GET`) ou apagar (`DELETE`) notas de outros usuários são bloqueadas com validação no servidor, retornando `403 Forbidden`.
* **Listagem Filtrada:** O endpoint `GET /notes` retorna apenas os dados pertencentes ao usuário logado.

### 2. Prevenção de Information Disclosure (Vazamento de Dados)
* **DTO Pattern:** Uso de `UserResponseDTO` no registro para garantir que dados sensíveis não sejam retornados na resposta da API.
* **Defesa em Profundidade:** Uso da anotação `@JsonIgnore` na entidade `User` para garantir que o hash da senha nunca seja serializado para JSON, mesmo em casos de erro ou manutenção futura.

### 3. Gestão Segura de Credenciais
* **Hashing:** Senhas são armazenadas usando **BCrypt** (`PasswordEncoder`), nunca em texto puro.
* **Infraestrutura:** Credenciais do banco de dados não estão hardcoded no código fonte. São injetadas via Variáveis de Ambiente (`${DB_USER}` e `${DB_PASSWORD}`), prevenindo vazamento de segredos no controle de versão.

### 4. Integridade Transacional
* Operações de deleção (`deleteAll`, `deleteById`) utilizam `@Transactional`, garantindo que falhas parciais não deixem o banco em estado inconsistente.

## Estrutura do Projeto

```
src/main/java/com/vitor/safenotes/
├── config/
│   └── SecurityConfig.java       # Configuração do Spring Security (HTTP Basic, rotas públicas)
├── controller/
│   ├── NoteController.java       # Endpoints de notas
│   └── UserController.java       # Endpoint de registro
├── dto/
│   └── UserResponseDTO.java      # Payload de resposta sem dados sensíveis
├── model/
│   ├── Note.java                 # Entidade nota
│   └── User.java                 # Entidade usuário (@JsonIgnore na senha)
├── repository/
│   ├── NoteRepository.java
│   └── UserRepository.java
└── service/
    ├── CustomUserDetailsService.java
    └── NoteService.java          # Lógica de negócio e validação de ownership
```

## ⚙️ Como Rodar o Projeto

### Pré-requisitos
* Java 17 instalado.
* PostgreSQL rodando na porta `5432`.
* Banco de dados criado com o nome `safenotes_db`.

### Passo a Passo

1. **Clone o repositório:**
    ```bash
    git clone https://github.com/vitorlop0/java-safenotes-api.git
    cd java-safenotes-api
    ```

2. **Configure as credenciais do banco:**
    Por segurança, o projeto lê usuário e senha do banco via variáveis de ambiente.

    * **Linux/Mac:**
      ```bash
      export DB_USER=postgres
      export DB_PASSWORD=sua_senha_postgres
      ```
    * **Windows (PowerShell):**
      ```powershell
      $env:DB_USER="postgres"
      $env:DB_PASSWORD="sua_senha_postgres"
      ```
    * *(Alternativa para IDE):* Configure `DB_USER` e `DB_PASSWORD` nas configurações de execução (Run Configuration).

    > **Nota:** O valor padrão de `DB_USER` é `postgres`. Só é necessário exportar essa variável se o seu usuário for diferente.

3. **Execute a aplicação:**
    Não é necessário ter o Maven instalado, use o Wrapper do projeto:
    ```bash
    ./mvnw spring-boot:run
    ```
    *(No Windows: `.\mvnw.cmd spring-boot:run`)*

4. **Acesse:**
    A API estará disponível em: `http://localhost:8080`

    A documentação interativa (Swagger UI) estará disponível em: `http://localhost:8080/swagger-ui/index.html`

## Documentação da API (Endpoints)

| Método | Endpoint | Descrição | Auth Necessária? |
| :--- | :--- | :--- | :--- |
| `POST` | `/register` | Cria um novo usuário (senha é criptografada) | ❌ Não |
| `POST` | `/notes` | Cria uma nota vinculada ao usuário logado | ✅ Sim (Basic Auth) |
| `GET` | `/notes` | Lista apenas as notas do usuário logado | ✅ Sim (Basic Auth) |
| `GET` | `/notes/{id}` | Lê uma nota específica (valida propriedade) | ✅ Sim (Basic Auth) |
| `DELETE` | `/notes/{id}` | Apaga uma nota específica (valida propriedade) | ✅ Sim (Basic Auth) |
| `DELETE` | `/notes` | Apaga todas as notas do usuário logado | ✅ Sim (Basic Auth) |

### Exemplos de Requisição

**Registrar usuário** — `POST /register`
```json
{
  "username": "vitor",
  "password": "minhasenha123"
}
```

**Criar nota** — `POST /notes` *(requer Basic Auth)*
```json
{
  "title": "Minha primeira nota",
  "content": "Conteúdo da nota aqui."
}
```

> A autenticação Basic Auth utiliza o `username` e `password` definidos no registro. Ferramentas como Insomnia, Postman e o próprio Swagger UI suportam esse mecanismo nativamente.
