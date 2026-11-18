# Kanowa Backend 🚀

Serviço de backend REST construído com **Spring Boot 3.3.5**, **Kotlin** e **PostgreSQL** para o aplicativo Kanowa.

## 📋 Funcionalidades

- **Autenticação & Usuários:**
  - `POST /api/auth/register` ou `POST /api/users`: Criação de novo usuário.
  - `POST /api/auth/login`: Autenticação com e-mail e senha.
  - `GET /api/users/{id}`: Consulta de usuário por ID.
  - `GET /api/users?email={email}`: Consulta de usuário por e-mail.
  - `GET /api/auth/me`: Consulta do usuário autenticado.

- **CRUD de Créditos & Entradas:**
  - `GET /api/credits`: Listagem de créditos.
  - `GET /api/credits/{id}`: Detalhes de um crédito.
  - `POST /api/credits`: Cadastro de nova entrada financeira.
  - `PUT /api/credits/{id}`: Atualização de crédito existente.
  - `DELETE /api/credits/{id}`: Exclusão de crédito.
  - `GET /api/credits/total`: Total mensal de créditos acumulados.

- **CRUD de Débitos & Despesas:**
  - `GET /api/debits`: Listagem de débitos.
  - `GET /api/debits/{id}`: Detalhes de um débito.
  - `POST /api/debits`: Cadastro de nova saída financeira.
  - `PUT /api/debits/{id}`: Atualização de débito existente.
  - `DELETE /api/debits/{id}`: Exclusão de débito.
  - `GET /api/debits/total`: Total mensal de débitos acumulados.

- **Dashboard Integrado:**
  - `GET /api/dashboard/summary`: Resumo financeiro consolidado (saldo total, receitas, despesas, taxa de economia).
  - `GET /api/dashboard/transactions`: Histórico unificado de transações recentes.

## 🛠️ Como Executar

### 1. Iniciar o Banco de Dados PostgreSQL
Você pode iniciar o PostgreSQL via Docker com:

```bash
cd kanowa-back
docker compose up -d
```

Credenciais padrão do PostgreSQL:
- **Host:** `localhost:5432`
- **Database:** `kanowa_db`
- **Usuário:** `postgres`
- **Senha:** `postgres`

### 2. Iniciar o Backend Spring Boot

Com Gradle:
```bash
./gradlew bootRun
```
Ou no Windows:
```cmd
gradlew.bat bootRun
```

O servidor estará disponível em: `http://localhost:8080` (ou `http://10.0.2.2:8080` a partir do emulador Android).

### 3. Usuário de Demonstração Inicial
Ao iniciar pela primeira vez com banco vazio, os dados iniciais são populados automaticamente:
- **E-mail:** `usuario@kanowa.com`
- **Senha:** `password123`
