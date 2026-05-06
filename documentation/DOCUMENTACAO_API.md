# 📚 Documentação Completa - API Mentes Brilhantes

**Versão:** 1.0.0  
**Data:** 06 de Abril de 2026  
**Status:** ✅ Produção

---

## 📋 Índice

1. [Visão Geral](#visão-geral)
2. [Configuração e Setup](#configuração-e-setup)
3. [Autenticação](#autenticação)
4. [Endpoints - Parceiros](#endpoints---parceiros)
5. [Endpoints - Tarefas](#endpoints---tarefas)
6. [Endpoints - Usuários](#endpoints---usuários)
7. [Tratamento de Erros](#tratamento-de-erros)
8. [Padrões de Resposta](#padrões-de-resposta)
9. [Testes Unitários](#testes-unitários)
10. [Exemplos Práticos](#exemplos-práticos)

---

## Visão Geral

A **API Mentes Brilhantes** é uma aplicação REST desenvolvida em **Quarkus** (Java) que gerencia:
- 🤝 **Parceiros** - Empresas parceiras com informações de validade
- ✅ **Tarefas** - Desafios gamificados para usuários
- 👥 **Usuários** - Alunos e mentores da plataforma

### Stack Tecnológico
- **Framework:** Quarkus 3.32.4
- **Linguagem:** Java 21+
- **Banco de Dados:** PostgreSQL 16
- **Segurança:** JWT (JSON Web Tokens)
- **ORM:** Hibernate + Panache
- **Validação:** Jakarta Bean Validation
- **Build:** Maven

### Características
✅ Tratamento robusto de erros com exceções customizadas  
✅ Autenticação com JWT  
✅ Upload de arquivos (imagens)  
✅ Logging completo com stack traces  
✅ Validação de JSON automatizada  
✅ Paginação opcional em endpoints GET  

---

## Configuração e Setup

### Pré-requisitos
```bash
- Java 21 ou superior
- Maven 3.8+
- PostgreSQL 14+
- Docker (opcional)
```

### Instalação

1. **Clone o repositório**
```bash
git clone <repositorio>
cd api-mentes-brilhantes
```

2. **Compile o projeto**
```bash
./mvnw clean compile
```

3. **Execute em desenvolvimento**
```bash
./mvnw quarkus:dev
```

4. **Acesse a aplicação**
```
http://localhost:8080
```

### Configuração do Banco de Dados

O arquivo `application.properties` contém as configurações:

```properties
# PostgreSQL
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=senha
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/db_mentes_brilhantes

# Flyway (migrations automáticas)
quarkus.flyway.migrate-at-start=true
```

---

## Autenticação

### JWT (JSON Web Token)

Todos os endpoints **privados** requerem um header de autenticação:

```http
Authorization: Bearer <seu_token_jwt>
```

### Estrutura do Token
```json
{
  "typ": "JWT",
  "alg": "RS256"
}
{
  "iss": "https://api.mentesbrihantes.com.br",
  "upn": "usuario@exemplo.com",
  "id": "uuid-do-usuario",
  "groups": ["ADMIN"],
  "iat": 1775475947,
  "exp": 1775504747,
  "jti": "jwt-id"
}
```

### Endpoints Públicos
- `GET /users` - Listar usuários ativos
- `GET /users/ranking` - Ranking anual por pontuação
- `GET /users/**` - Buscar usuários por filtro
- `GET /partners` - Listar parceiros
- `GET /task` - Listar tarefas
- `GET /uploads/**` - Servir arquivos

### Endpoints Privados (Requerem JWT)
- `POST /partners` - Criar parceiro
- `PUT /partners/{id}` - Atualizar parceiro
- `DELETE /partners/{id}` - Deletar parceiro
- `POST /task` - Criar tarefa
- `PUT /task/{id}` - Atualizar tarefa
- `DELETE /task/{id}` - Deletar tarefa
- `PUT /users/**` - Atualizar usuários
- `DELETE /users/{id}` - Deletar usuário

---

## Endpoints - Parceiros

### Base URL
```
/partners
```

### 1. Criar Parceiro
```http
POST /partners
Content-Type: multipart/form-data
Authorization: Bearer <token>

Body:
- data: JSON string com dados do parceiro
- image: Arquivo de imagem (opcional)
```

**Exemplo de Requisição (CURL)**
```bash
curl -X POST http://localhost:8080/partners \
  -H "Authorization: Bearer seu_token" \
  -F "data={\"name\":\"Amazon\",\"url\":\"https://amazon.com\",\"validity\":\"2026-12-31\",\"city\":\"São Paulo\",\"state\":\"SP\",\"zipCode\":\"01310-100\"}" \
  -F "image=@/caminho/imagem.jpg"
```

**Campos Obrigatórios (data)**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| name | String | Nome da empresa |
| url | String | Site da empresa |
| validity | LocalDate (YYYY-MM-DD) | Data de validade |
| city | String | Cidade |
| state | String | Estado (sigla) |
| zipCode | String | CEP |

**Resposta (201 Created)**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Amazon",
  "imageUrl": "uploads/partners/550e8400.jpg",
  "url": "https://amazon.com",
  "validity": "2026-12-31",
  "city": "São Paulo",
  "state": "SP",
  "zipCode": "01310-100"
}
```

**Possíveis Erros**
| Status | Erro | Descrição |
|--------|------|-----------|
| 400 | BadRequestException | JSON inválido ou dados obrigatórios vazios |
| 400 | BadRequestException | Nome do parceiro vazio |
| 500 | ApplicationException | Erro ao salvar imagem |

---

### 2. Atualizar Parceiro
```http
PUT /partners/{id}
Content-Type: multipart/form-data
Authorization: Bearer <token>

Path Parameters:
- id: UUID do parceiro

Body:
- data: JSON com campos a atualizar (opcional)
- image: Nova imagem (opcional)
```

**Exemplo de Requisição**
```bash
curl -X PUT http://localhost:8080/partners/550e8400-e29b-41d4-a716-446655440000 \
  -H "Authorization: Bearer seu_token" \
  -F "data={\"name\":\"Amazon Atualizado\",\"url\":\"https://amazon-updated.com\"}"
```

**Resposta (200 OK)**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Amazon Atualizado",
  "imageUrl": "uploads/partners/550e8400.jpg",
  "url": "https://amazon-updated.com",
  ...
}
```

**Possíveis Erros**
| Status | Erro |
|--------|------|
| 404 | Parceiro não encontrado |
| 400 | Formulário inválido |

---

### 3. Deletar Parceiro
```http
DELETE /partners/{id}
Authorization: Bearer <token>

Path Parameters:
- id: UUID do parceiro
```

**Exemplo de Requisição**
```bash
curl -X DELETE http://localhost:8080/partners/550e8400-e29b-41d4-a716-446655440000 \
  -H "Authorization: Bearer seu_token"
```

**Resposta (204 No Content)**
```
(sem body)
```

**Possíveis Erros**
| Status | Erro |
|--------|------|
| 404 | Parceiro não encontrado |

---

### 4. Listar Todos os Parceiros
```http
GET /partners
```

**Resposta (200 OK)**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Amazon",
    "imageUrl": "uploads/partners/550e8400.jpg",
    "url": "https://amazon.com",
    "validity": "2026-12-31",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01310-100"
  },
  ...
]
```

---

### 5. Buscar Parceiros Válidos por Local
```http
GET /partners/validity/{state}/{city}

Path Parameters:
- state: Sigla do estado (ex: SP)
- city: Nome da cidade
```

**Exemplo**
```bash
curl http://localhost:8080/partners/validity/SP/São%20Paulo
```

**Resposta (200 OK)**
```json
[
  { ... parceiros válidos em SP/São Paulo ... }
]
```

---

### 6. Listar Parceiros Válidos
```http
GET /partners/validity
```

**Resposta (200 OK)**
```json
[
  { ... todos os parceiros com validade > hoje ... }
]
```

---

## Endpoints - Tarefas

### Base URL
```
/task
```

### 1. Criar Tarefa
```http
POST /task
Content-Type: multipart/form-data
Authorization: Bearer <token>

Body:
- data: JSON string com dados da tarefa
- image: Arquivo de imagem (opcional)
```

**Exemplo de Requisição**
```bash
curl -X POST http://localhost:8080/task \
  -H "Authorization: Bearer seu_token" \
  -F "data={\"name\":\"Aprender Java\",\"description\":\"Estudar conceitos avançados\",\"tasksPoints\":10,\"tasksStatus\":\"ACTIVE\",\"awards\":\"Certificado\",\"tasksType\":\"NORMAL\"}" \
  -F "image=@/caminho/imagem.jpg"
```

**Campos Obrigatórios (data)**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| name | String | Nome da tarefa |
| description | String | Descrição |
| tasksPoints | Integer | Pontos da tarefa |
| tasksStatus | Enum | ACTIVE, INACTIVE |
| awards | String | Prêmios |
| tasksType | Enum | NORMAL, ESPECIAL |

**Resposta (201 Created)**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440000",
  "name": "Aprender Java",
  "description": "Estudar conceitos avançados",
  "tasksPoints": 10,
  "imageUrl": "uploads/tasks/660e8400.jpg",
  "tasksStatus": "ACTIVE",
  "awards": "Certificado",
  "tasksType": "NORMAL",
  "createdAt": "2026-04-06T14:30:00"
}
```

---

### 2. Atualizar Tarefa
```http
PUT /task/{id}
Content-Type: multipart/form-data
Authorization: Bearer <token>

Path Parameters:
- id: UUID da tarefa
```

**Resposta (200 OK)**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440000",
  "name": "Tarefa Atualizada",
  ...
}
```

---

### 3. Listar Todas as Tarefas
```http
GET /task
```

**Resposta (200 OK)**
```json
[
  { ... tarefas ... }
]
```

---

### 4. Buscar Tarefa por ID
```http
GET /task/{id}

Path Parameters:
- id: UUID da tarefa
```

---

### 5. Buscar Tarefas por Nome
```http
GET /task/name/{name}

Path Parameters:
- name: Nome ou parte do nome
```

---

### 6. Listar Tarefas Ativas
```http
GET /task/status-active
```

---

### 7. Buscar Tarefas por Tipo
```http
GET /task/type/{type}

Path Parameters:
- type: NORMAL, ESPECIAL
```

---

### 8. Alterar Status da Tarefa
```http
PUT /task/alter-status/{id}
Authorization: Bearer <token>

Path Parameters:
- id: UUID da tarefa
```

**Resposta (200 OK)**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440000",
  "tasksStatus": "INACTIVE",
  ...
}
```

---

### 9. Deletar Tarefa
```http
DELETE /task/{id}
Authorization: Bearer <token>

Path Parameters:
- id: UUID da tarefa
```

**Resposta (204 No Content)**
```
(sem body)
```

---

## Endpoints - Usuários

### Base URL
```
/users
```

### 1. Listar Usuários Ativos
```http
GET /users
```

**Resposta (200 OK)**
```json
[
  {
    "id": "770e8400-e29b-41d4-a716-446655440000",
    "name": "João Silva",
    "email": "joao@exemplo.com",
    "userType": "USER",
    "totalPoints": 120,
    "redeemablePoints": 90,
    "dateOfBirth": "1990-05-15",
    "imageUrl": "uploads/users/avatar.jpg",
    "state": "SP",
    "city": "São Paulo",
    "zipCode": "01310-100",
    "street": "Rua Paulista",
    "number": "1000",
    "complement": "Ap 501",
    "educationInstitution": "USP",
    "userType": "STUDENT",
    "mentesEdition": "2024-2025",
    "active": true
  },
  ...
]
```

---

### 2. Ranking Anual de Usuários
```http
GET /users/ranking
```

**Regra de Ordenação**
- `totalPoints` decrescente
- desempate por `name` ascendente

**Resposta (200 OK)**
```json
[
  {
    "id": "770e8400-e29b-41d4-a716-446655440000",
    "name": "João Silva",
    "totalPoints": 120,
    "redeemablePoints": 90,
    "imageUrl": "/uploads/users/user-01.png"
  }
]
```

---

### 3. Buscar Usuários por Estado e Cidade
```http
GET /users/{state}/{city}

Path Parameters:
- state: Sigla (SP, RJ, etc)
- city: Nome da cidade
```

---

### 4. Buscar Usuários por Nome
```http
GET /users/name/{name}

Path Parameters:
- name: Nome ou parte do nome
```

---

### 5. Buscar Usuários por Instituição
```http
GET /users/education-instituition/{institution}

Path Parameters:
- institution: Nome da instituição
```

---

### 6. Buscar Usuários por Tipo
```http
GET /users/user-type/{userType}

Path Parameters:
- userType: STUDENT, MENTOR, ADMIN
```

---

### 7. Buscar Usuários por Edição
```http
GET /users/mentes-edition/{edition}

Path Parameters:
- edition: Ano (2024-2025, etc)
```

---

### 8. Buscar Usuários por Data de Nascimento
```http
GET /users/date-of-birth/{date}

Path Parameters:
- date: YYYY-MM-DD
```

---

### 9. Listar Usuários Inativos
```http
GET /users/inactive-user
```

---

### 10. Atualizar Endereço do Usuário
```http
PUT /users/address/{id}
Content-Type: application/json
Authorization: Bearer <token>

Path Parameters:
- id: UUID do usuário

Body:
{
  "street": "Rua das Flores",
  "number": "2000",
  "complement": "Ap 1001",
  "city": "São Paulo",
  "state": "SP",
  "zipCode": "01310-200"
}
```

---

### 11. Atualizar Usuário
```http
PUT /users/{id}
Content-Type: application/json
Authorization: Bearer <token>

Path Parameters:
- id: UUID do usuário

Body:
{
  "name": "João Silva Updated",
  "email": "joao.updated@exemplo.com",
  "dateOfBirth": "1990-05-15"
}
```

---

### 12. Ativar/Desativar Usuário
```http
PUT /users/active-user/{id}
Authorization: Bearer <token>

Path Parameters:
- id: UUID do usuário
```

---

### 13. Alterar Tipo de Usuário
```http
PUT /users/user-type/{id}
Authorization: Bearer <token>

Path Parameters:
- id: UUID do usuário
```

---

### 14. Deletar Usuário
```http
DELETE /users/{id}
Authorization: Bearer <token>

Path Parameters:
- id: UUID do usuário
```

**Resposta (204 No Content)**
```
(sem body)
```

---

### Regras de Pontuação (Ranking e Resgate)

- `totalPoints`: pontuação anual usada no ranking (`GET /users/ranking`).
- `redeemablePoints`: saldo usado para resgatar prêmios.
- Resgate de prêmio (`POST /award-redemptions`) desconta apenas `redeemablePoints`.
- Validação de resgate (`PUT /award-redemptions/{id}/validate`) não desconta pontos novamente.
- Cancelamento de resgate (`PUT /award-redemptions/{id}/cancel`) devolve `redeemablePoints` e estoque.
- Reset anual automático em `31/12 23:00` (America/Sao_Paulo), zerando `totalPoints` e `redeemablePoints`.

---

## Tratamento de Erros

### Exceções Customizadas

A API utiliza **exceções customizadas** que mapeiam automaticamente para os status HTTP corretos:

#### NotFoundException (HTTP 404)
Lançada quando um recurso não é encontrado.

```json
{
  "message": "Nenhum parceiro encontrado com o id: 550e8400...",
  "status": 404,
  "timestamp": "2026-04-06T14:30:00"
}
```

#### BadRequestException (HTTP 400)
Lançada quando os dados da requisição são inválidos.

```json
{
  "message": "O campo 'data' com os dados do parceiro é obrigatório",
  "status": 400,
  "timestamp": "2026-04-06T14:30:00"
}
```

#### ConflictException (HTTP 409)
Lançada quando há conflito nos dados (ex: duplicado).

```json
{
  "message": "Email já cadastrado",
  "status": 409,
  "timestamp": "2026-04-06T14:30:00"
}
```

#### ApplicationException (HTTP 400+)
Exceção genérica que detecta o tipo de erro pela mensagem.

```json
{
  "message": "Erro ao criar parceiro: ...",
  "status": 400,
  "timestamp": "2026-04-06T14:30:00"
}
```

### Handlers de Exceção

Dois handlers interceptam todas as exceções:

1. **ApplicationExceptionHandler** - Para `ApplicationException` e subclasses
2. **GlobalExceptionHandler** - Para todas as outras exceções não mapeadas

---

## Padrões de Resposta

### Estrutura Padrão de Sucesso (2xx)

```json
{
  "id": "uuid",
  "name": "exemplo",
  "email": "email@exemplo.com",
  "status": "ACTIVE",
  ...
}
```

### Estrutura Padrão de Erro (4xx, 5xx)

```json
{
  "message": "Descrição clara do erro",
  "status": 400,
  "timestamp": "2026-04-06T14:30:00"
}
```

### Códigos HTTP Utilizados

| Código | Significado | Quando Usado |
|--------|------------|----------|
| 200 | OK | Requisição bem-sucedida (GET, PUT) |
| 201 | Created | Recurso criado com sucesso (POST) |
| 204 | No Content | Recurso deletado com sucesso (DELETE) |
| 400 | Bad Request | Dados inválidos, validação falhou |
| 404 | Not Found | Recurso não encontrado |
| 409 | Conflict | Conflito (ex: email duplicado) |
| 500 | Internal Server Error | Erro não tratado no servidor |

---

## Testes Unitários

### Estrutura de Testes

Os testes são divididos por Resource:

```
src/test/java/br/com/avsistems/resource/
├── PartnersResourceTest.java     (24 testes)
├── TaskResourceTest.java         (21 testes)
└── UserResourceTest.java         (19 testes)
```

**Total:** 64 testes unitários

### Executar Testes

```bash
# Todos os testes
./mvnw test

# Apenas PartnersResource
./mvnw test -Dtest=PartnersResourceTest

# Apenas um teste específico
./mvnw test -Dtest=PartnersResourceTest#testCreatePartnerSuccess

# Com cobertura
./mvnw clean test jacoco:report
```

### Cobertura de Testes

**PartnersResourceTest** (24 testes)
- ✅ Create com sucesso
- ✅ Create com JSON inválido
- ✅ Create com dados vazios
- ✅ Update com sucesso
- ✅ Update parceiro não encontrado
- ✅ Delete com sucesso
- ✅ Delete parceiro não encontrado
- ✅ Get todos os parceiros
- ✅ Get lista vazia
- ✅ Get por validade e local
- ✅ Get todos válidos
- ✅ Admin endpoint

**TaskResourceTest** (21 testes)
- ✅ Create com sucesso
- ✅ Create com JSON inválido
- ✅ Create sem dados
- ✅ List todas as tarefas
- ✅ List vazia
- ✅ Find por nome
- ✅ Find por ID
- ✅ Find ativas
- ✅ Find por tipo
- ✅ Update com sucesso
- ✅ Update não encontrada
- ✅ Alterar status
- ✅ Delete com sucesso
- ✅ Delete não encontrada

**UserResourceTest** (19 testes)
- ✅ List usuários
- ✅ Find por estado/cidade
- ✅ Find por nome
- ✅ Find por instituição
- ✅ Find por tipo
- ✅ Find por edição
- ✅ Find por data nascimento
- ✅ Find inativos
- ✅ Update endereço
- ✅ Update usuário
- ✅ Alterar ativação
- ✅ Alterar tipo
- ✅ Delete com sucesso
- ✅ Delete não encontrado

---

## Exemplos Práticos

### Exemplo 1: Criar Parceiro com Upload de Imagem

```bash
#!/bin/bash

TOKEN="seu_token_jwt_aqui"

curl -X POST http://localhost:8080/partners \
  -H "Authorization: Bearer $TOKEN" \
  -F "data={
    \"name\": \"Google Brasil\",
    \"url\": \"https://google.com.br\",
    \"validity\": \"2026-12-31\",
    \"city\": \"São Paulo\",
    \"state\": \"SP\",
    \"zipCode\": \"01310-100\"
  }" \
  -F "image=@/caminho/google-logo.png"
```

### Exemplo 2: Atualizar Endereço do Usuário

```bash
TOKEN="seu_token_jwt_aqui"
USER_ID="550e8400-e29b-41d4-a716-446655440000"

curl -X PUT http://localhost:8080/users/adrress/$USER_ID \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "street": "Avenida Paulista",
    "number": "1000",
    "complement": "Sala 1001",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01311-100"
  }'
```

### Exemplo 3: Listar Tarefas Ativas

```bash
curl http://localhost:8080/task/status-active \
  -H "Accept: application/json" \
  | jq '.[] | {id, name, tasksPoints}'
```

### Exemplo 4: Buscar Parceiros Válidos em SP

```bash
curl http://localhost:8080/partners/validity/SP/São%20Paulo \
  -H "Accept: application/json" \
  | jq '.[] | {name, url, validity}'
```

### Exemplo 5: Deletar Tarefa

```bash
TOKEN="seu_token_jwt_aqui"
TASK_ID="660e8400-e29b-41d4-a716-446655440000"

curl -X DELETE http://localhost:8080/task/$TASK_ID \
  -H "Authorization: Bearer $TOKEN"
```

---

## Logging e Debugging

### Logs em Desenvolvimento

Os logs aparecem no console com diferentes níveis:

```
INFO  - Operações bem-sucedidas
WARN  - Avisos (ex: arquivo não encontrado)
ERROR - Erros com stack trace completo
```

### Exemplo de Log de Erro

```
2026-04-06 14:30:00,500 ERROR [br.com.avsistems.handler.ApplicationExceptionHandler] 
(executor-thread-1) ApplicationException capturada: O nome do parceiro não pode ser vazio
  at br.com.avsistems.service.PartnersService.createPartner(PartnersService.java:57)
  at br.com.avsistems.resource.PartnersResource.createPartner(PartnersResource.java:25)
  ...
```

### Habilitando Debug

Para modo verbose no dev:

```bash
./mvnw quarkus:dev -X
```

---

## Monitoramento em Produção

### Health Check
```http
GET /q/health
```

### Metrics
```http
GET /q/metrics
```

### API Docs (Swagger)
```
http://localhost:8080/q/swagger-ui/
```

---

## Suporte e Contato

- **Email:** suporte@mentesbrihantes.com.br
- **Documentação:** https://docs.mentesbrihantes.com.br
- **Issues:** GitHub Issues
- **Status:** https://status.mentesbrihantes.com.br

---

**Última atualização:** 06 de Abril de 2026  
**Mantido por:** Equipe de Backend

