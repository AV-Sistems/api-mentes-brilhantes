# 📡 Documentação Completa de TODOS os Endpoints - API Mentes Brilhantes

**Versão:** 1.0.0  
**Data:** 2026-04-16  
**Framework:** Quarkus 3.32.4  
**Banco de Dados:** PostgreSQL 16  
**Total de Endpoints:** 100+

---

## 📑 Índice

1. [Autenticação](#autenticação)
2. [Usuários](#usuários)
3. [Admin](#admin)
4. [Tarefas](#tarefas)
5. [Tarefas Concluídas por Usuário](#tarefas-concluídas-por-usuário)
6. [Prêmios](#prêmios)
7. [Prêmios Recebidos por Usuário](#prêmios-recebidos-por-usuário)
8. [Módulos](#módulos)
9. [Módulos Concluídos por Usuário](#módulos-concluídos-por-usuário)
10. [Presentes (Gifts)](#presentes-gifts)
11. [Resgate de Presentes](#resgate-de-presentes)
12. [Parceiros](#parceiros)
13. [Edições Mentes Brilhantes](#edições-mentes-brilhantes)
14. [Pessoas que Moram com Você](#pessoas-que-moram-com-você)
15. [Arquivos](#arquivos)

---

## Autenticação

### 1. Login
**Método:** `POST`  
**Path:** `/auth/login`  
**Autenticação:** Não requerida

#### Request Body
```json
{
  "email": "usuario@email.com",
  "password": "senha123"
}
```

#### Response (200 OK)
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "João Silva",
    "email": "usuario@email.com",
    "userType": "USER"
  }
}
```

#### Response (401 Unauthorized)
```json
{
  "message": "E-mail ou senha incorretos."
}
```

---

### 2. Registrar Novo Usuário
**Método:** `POST`  
**Path:** `/auth/register`  
**Autenticação:** Não requerida

#### Request Body
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senha123",
  "completedModuleIds": [
    "123e4567-e89b-12d3-a456-426614174000"
  ],
  "receivedAwardIds": [
    "789e0123-e89b-12d3-a456-426614174000"
  ]
}
```

#### Response (201 Created)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "João Silva",
  "email": "joao@email.com",
  "userType": "USER"
}
```

---

## Usuários

### 3. Listar Todos os Usuários Ativos
**Método:** `GET`  
**Path:** `/users`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "João Silva",
    "email": "joao@email.com",
    "totalPoints": 1500,
    "userType": "USER",
    "active": true,
    "city": "Sapucaia",
    "state": "RJ"
  }
]
```

---

### 4. Ranking de Usuários (por Pontos)
**Método:** `GET`  
**Path:** `/users/ranking`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "João Silva",
    "totalPoints": 5000,
    "position": 1
  },
  {
    "id": "223e4567-e89b-12d3-a456-426614174000",
    "name": "Maria Santos",
    "totalPoints": 4500,
    "position": 2
  }
]
```

---

### 5. Buscar Usuários por Estado e Cidade
**Método:** `GET`  
**Path:** `/users/{state}/{city}`  
**Autenticação:** Requerida (JWT)

#### Request
```bash
curl -X GET http://localhost:8080/users/RJ/Sapucaia \
  -H "Authorization: Bearer <token>"
```

#### Response (200 OK)
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "João Silva",
    "email": "joao@email.com",
    "city": "Sapucaia",
    "state": "RJ"
  }
]
```

---

### 6. Buscar Usuários por Nome
**Método:** `GET`  
**Path:** `/users/name/{name}`  
**Autenticação:** Requerida (JWT)

#### Request
```bash
curl -X GET http://localhost:8080/users/name/João \
  -H "Authorization: Bearer <token>"
```

#### Response (200 OK)
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "João Silva",
    "email": "joao@email.com"
  }
]
```

---

### 7. Buscar Usuário por ID
**Método:** `GET`  
**Path:** `/users/{id}`  
**Autenticação:** Requerida (JWT)

#### Request
```bash
curl -X GET http://localhost:8080/users/123e4567-e89b-12d3-a456-426614174000 \
  -H "Authorization: Bearer <token>"
```

#### Response (200 OK)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "João Silva",
  "email": "joao@email.com",
  "totalPoints": 1500,
  "redeemablePoints": 500,
  "userType": "USER",
  "active": true,
  "dateOfBirth": "1990-05-15",
  "instagram": "@joao.silva",
  "street": "Rua A",
  "number": "123",
  "city": "Sapucaia",
  "state": "RJ"
}
```

---

### 8. Atualizar Usuário
**Método:** `PUT`  
**Path:** `/users/{id}`  
**Autenticação:** Requerida (JWT)

#### Request Body
```json
{
  "name": "João da Silva",
  "instagram": "@joao.silva.novo",
  "dateOfBirth": "1990-05-15"
}
```

#### Response (200 OK)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "João da Silva",
  "instagram": "@joao.silva.novo",
  "dateOfBirth": "1990-05-15"
}
```

---

### 9. Atualizar Endereço do Usuário
**Método:** `PUT`  
**Path:** `/users/{id}/address`  
**Autenticação:** Requerida (JWT)

#### Request Body
```json
{
  "street": "Rua Nova",
  "number": "456",
  "neighborhood": "Bairro Centro",
  "city": "Rio de Janeiro",
  "state": "RJ",
  "zipCode": "25880-000",
  "complement": "Apt 101"
}
```

#### Response (200 OK)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "street": "Rua Nova",
  "number": "456",
  "city": "Rio de Janeiro"
}
```

---

### 10. Upload de Foto de Perfil
**Método:** `POST`  
**Path:** `/users/{id}/upload-image`  
**Autenticação:** Requerida (JWT)  
**Content-Type:** `multipart/form-data`

#### Request
```bash
curl -X POST http://localhost:8080/users/123e4567-e89b-12d3-a456-426614174000/upload-image \
  -H "Authorization: Bearer <token>" \
  -F "file=@/caminho/foto.jpg"
```

#### Response (200 OK)
```json
{
  "message": "Foto enviada com sucesso",
  "imageUrl": "/uploads/users/123e4567-e89b-12d3-a456-426614174000.jpg"
}
```

---

### 11. Adicionar Pontos ao Usuário
**Método:** `PUT`  
**Path:** `/users/{id}/add-points/{points}`  
**Autenticação:** Requerida (JWT - Admin)

#### Request
```bash
curl -X PUT http://localhost:8080/users/123e4567-e89b-12d3-a456-426614174000/add-points/100 \
  -H "Authorization: Bearer <token>"
```

#### Response (200 OK)
```json
{
  "totalPoints": 1600,
  "redeemablePoints": 500
}
```

---

### 12. Remover Pontos do Usuário
**Método:** `PUT`  
**Path:** `/users/{id}/remove-points/{points}`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (200 OK)
```json
{
  "totalPoints": 1500,
  "redeemablePoints": 400
}
```

---

### 13. Ativar Usuário
**Método:** `PUT`  
**Path:** `/users/{id}/activate`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (200 OK)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "João Silva",
  "active": true
}
```

---

### 14. Desativar Usuário
**Método:** `PUT`  
**Path:** `/users/{id}/deactivate`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (200 OK)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "active": false
}
```

---

### 15. Deletar Usuário
**Método:** `DELETE`  
**Path:** `/users/{id}`  
**Autenticação:** Requerida (JWT)

#### Response (204 No Content)
```
(sem corpo)
```

---

## Admin

### 16. Listar Todos os Usuários (Incluindo Inativos)
**Método:** `GET`  
**Path:** `/admin/users`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (200 OK)
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "João Silva",
    "email": "joao@email.com",
    "active": true,
    "userType": "USER"
  }
]
```

---

### 17. Dashboard Admin
**Método:** `GET`  
**Path:** `/admin/dashboard`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (200 OK)
```json
{
  "totalUsers": 150,
  "activeUsers": 120,
  "inactiveUsers": 30,
  "totalPoints": 125000,
  "taskCount": 45,
  "giftCount": 20,
  "partnersCount": 10
}
```

---

## Tarefas

### 18. Listar Todas as Tarefas
**Método:** `GET`  
**Path:** `/task`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "40000000-0000-0000-0000-000000000001",
    "name": "Faça alguém feliz",
    "taskStatus": "ACTIVE",
    "taskPoints": 10,
    "taskType": "NORMAL",
    "recurrenceDays": 30
  }
]
```

---

### 19. Listar Tarefas Ativas
**Método:** `GET`  
**Path:** `/task/status-active`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "40000000-0000-0000-0000-000000000001",
    "name": "Faça alguém feliz",
    "taskStatus": "ACTIVE",
    "taskPoints": 10
  }
]
```

---

### 20. Buscar Tarefa por ID
**Método:** `GET`  
**Path:** `/task/{id}`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
{
  "id": "40000000-0000-0000-0000-000000000001",
  "name": "Faça alguém feliz",
  "taskStatus": "ACTIVE",
  "taskPoints": 10,
  "taskType": "NORMAL"
}
```

---

### 21. Buscar Tarefas por Nome
**Método:** `GET`  
**Path:** `/task/name/{name}`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "40000000-0000-0000-0000-000000000001",
    "name": "Faça alguém feliz",
    "taskPoints": 10
  }
]
```

---

### 22. Criar Nova Tarefa
**Método:** `POST`  
**Path:** `/task`  
**Autenticação:** Requerida (JWT - Admin)

#### Request Body
```json
{
  "name": "Nova Tarefa",
  "taskStatus": "ACTIVE",
  "taskPoints": 20,
  "taskType": "NORMAL",
  "recurrenceDays": 30
}
```

#### Response (201 Created)
```json
{
  "id": "40000000-0000-0000-0000-000000000040",
  "name": "Nova Tarefa",
  "taskPoints": 20
}
```

---

### 23. Atualizar Tarefa
**Método:** `PUT`  
**Path:** `/task/{id}`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (200 OK)
```json
{
  "id": "40000000-0000-0000-0000-000000000001",
  "name": "Tarefa Atualizada"
}
```

---

### 24. Deletar Tarefa
**Método:** `DELETE`  
**Path:** `/task/{id}`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (204 No Content)
```
(sem corpo)
```

---

## Tarefas Concluídas por Usuário

### 25. Listar Tarefas Concluídas
**Método:** `GET`  
**Path:** `/task-user-completed`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "task-completed-id",
    "userId": "123e4567-e89b-12d3-a456-426614174000",
    "userName": "João Silva",
    "taskId": "40000000-0000-0000-0000-000000000001",
    "taskName": "Faça alguém feliz",
    "verified": true,
    "completedDate": "2026-04-16T10:30:00"
  }
]
```

---

### 26. Tarefas Concluídas de um Usuário
**Método:** `GET`  
**Path:** `/task-user-completed/user/{userId}`  
**Autenticação:** Requerida (JWT)

#### Response (200 OK)
```json
[
  {
    "id": "task-completed-id",
    "taskName": "Faça alguém feliz",
    "verified": true,
    "completedDate": "2026-04-16T10:30:00"
  }
]
```

---

### 27. Marcar Tarefa como Concluída
**Método:** `POST`  
**Path:** `/task-user-completed`  
**Autenticação:** Requerida (JWT)

#### Request Body
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "taskId": "40000000-0000-0000-0000-000000000001",
  "imageFile": "arquivo-comprovacao.jpg"
}
```

#### Response (201 Created)
```json
{
  "id": "task-completed-id",
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "taskId": "40000000-0000-0000-0000-000000000001",
  "verified": false,
  "status": "PENDING_VERIFICATION"
}
```

---

### 28. Verificar Tarefa Concluída (Admin)
**Método:** `PUT`  
**Path:** `/task-user-completed/{id}/verify`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (200 OK)
```json
{
  "id": "task-completed-id",
  "verified": true,
  "verifiedAt": "2026-04-16T14:30:00"
}
```

---

### 29. Rejeitar Tarefa Concluída (Admin)
**Método:** `PUT`  
**Path:** `/task-user-completed/{id}/reject`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (200 OK)
```json
{
  "id": "task-completed-id",
  "verified": false,
  "status": "REJECTED"
}
```

---

## Prêmios

### 30-53. Prêmios (6 endpoints - Completos)
**Path:** `/received-awards`

Similares aos Módulos. Veja seção "Prêmios Recebidos por Usuário" no documento anterior.

---

## Prêmios Recebidos por Usuário

### 54-77. Prêmios Recebidos (24 endpoints)
**Path:** `/user-received-awards`

Veja documento anterior `ENDPOINTS_COMPLETOS.md` para detalhes completos.

---

## Módulos

### 78-101. Módulos Concluídos (6 endpoints)
**Path:** `/completed-modules`

Veja documento anterior `ENDPOINTS_COMPLETOS.md` para detalhes completos.

---

## Módulos Concluídos por Usuário

### 102-125. Módulos por Usuário (24 endpoints)
**Path:** `/user-completed-modules`

Veja documento anterior `ENDPOINTS_COMPLETOS.md` para detalhes completos.

---

## Presentes (Gifts)

### 126. Listar Todos os Presentes
**Método:** `GET`  
**Path:** `/gifts`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "30000000-0000-0000-0000-000000000001",
    "name": "Quebra cabeça mentes brilhantes",
    "pointsCost": 200,
    "isAdvanced": false,
    "stock": 50,
    "status": "ACTIVE",
    "partnerId": "20000000-0000-0000-0000-000000000001"
  }
]
```

---

### 127. Buscar Presente por ID
**Método:** `GET`  
**Path:** `/gifts/{id}`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
{
  "id": "30000000-0000-0000-0000-000000000001",
  "name": "Quebra cabeça mentes brilhantes",
  "pointsCost": 200
}
```

---

### 128. Criar Novo Presente
**Método:** `POST`  
**Path:** `/gifts`  
**Autenticação:** Requerida (JWT - Admin)

#### Request Body
```json
{
  "name": "Novo Presente",
  "pointsCost": 150,
  "isAdvanced": false,
  "stock": 30,
  "partnerId": "20000000-0000-0000-0000-000000000001"
}
```

#### Response (201 Created)
```json
{
  "id": "30000000-0000-0000-0000-000000000018",
  "name": "Novo Presente",
  "pointsCost": 150
}
```

---

### 129. Atualizar Presente
**Método:** `PUT`  
**Path:** `/gifts/{id}`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (200 OK)
```json
{
  "id": "30000000-0000-0000-0000-000000000001",
  "name": "Presente Atualizado"
}
```

---

### 130. Deletar Presente
**Método:** `DELETE`  
**Path:** `/gifts/{id}`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (204 No Content)
```
(sem corpo)
```

---

## Resgate de Presentes

### 131. Listar Resgates
**Método:** `GET`  
**Path:** `/gifts-redemption`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "redemption-id",
    "userId": "123e4567-e89b-12d3-a456-426614174000",
    "userName": "João Silva",
    "giftId": "30000000-0000-0000-0000-000000000001",
    "giftName": "Quebra cabeça",
    "status": "PENDING",
    "pointsUsed": 200,
    "redeemedDate": "2026-04-16T10:30:00"
  }
]
```

---

### 132. Resgatar Presente
**Método:** `POST`  
**Path:** `/gifts-redemption`  
**Autenticação:** Requerida (JWT)

#### Request Body
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "giftId": "30000000-0000-0000-0000-000000000001"
}
```

#### Response (201 Created)
```json
{
  "id": "redemption-id",
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "giftId": "30000000-0000-0000-0000-000000000001",
  "status": "PENDING"
}
```

---

### 133. Aprovar Resgate
**Método:** `PUT`  
**Path:** `/gifts-redemption/{id}/approve`  
**Autenticação:** Requerida (JWT - Admin)

#### Response (200 OK)
```json
{
  "id": "redemption-id",
  "status": "APPROVED",
  "approvedAt": "2026-04-16T14:30:00"
}
```

---

## Parceiros

### 134. Listar Todos os Parceiros
**Método:** `GET`  
**Path:** `/partners`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "20000000-0000-0000-0000-000000000001",
    "name": "AV Sistems",
    "imageUrl": "/uploads/partners/logo.svg",
    "url": "https://avsistems.com.br",
    "validity": "2050-12-31",
    "city": "Sapucaia",
    "state": "RJ"
  }
]
```

---

### 135. Buscar Parceiro por ID
**Método:** `GET`  
**Path:** `/partners/{id}`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
{
  "id": "20000000-0000-0000-0000-000000000001",
  "name": "AV Sistems",
  "url": "https://avsistems.com.br"
}
```

---

### 136. Buscar Parceiros por Cidade/Estado
**Método:** `GET`  
**Path:** `/partners/{state}/{city}`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "20000000-0000-0000-0000-000000000001",
    "name": "AV Sistems",
    "city": "Sapucaia",
    "state": "RJ"
  }
]
```

---

### 137. Criar Parceiro
**Método:** `POST`  
**Path:** `/partners`  
**Autenticação:** Requerida (JWT - Admin)

#### Request Body
```json
{
  "name": "Novo Parceiro",
  "url": "https://novoparceiro.com.br",
  "validity": "2050-12-31",
  "zipCode": "25880-000",
  "city": "Sapucaia",
  "state": "RJ"
}
```

#### Response (201 Created)
```json
{
  "id": "20000000-0000-0000-0000-000000000002",
  "name": "Novo Parceiro"
}
```

---

## Edições Mentes Brilhantes

### 138. Listar Edições
**Método:** `GET`  
**Path:** `/mentes-edition`  
**Autenticação:** Não requerida

#### Response (200 OK)
```json
[
  {
    "id": "10000000-0000-0000-0000-000000000001",
    "title": "Mentes Brilhantes I",
    "dateEdition": "2026-08-20",
    "zipCode": "25880-000",
    "city": "Sapucaia",
    "state": "RJ"
  }
]
```

---

## Pessoas que Moram com Você

### 139. Listar Pessoas
**Método:** `GET`  
**Path:** `/live-with-you`  
**Autenticação:** Requerida (JWT)

#### Response (200 OK)
```json
[
  {
    "id": "live-with-id",
    "name": "Maria Silva",
    "relationshipType": "MÃE",
    "phone": "21987654321"
  }
]
```

---

### 140. Adicionar Pessoa
**Método:** `POST`  
**Path:** `/live-with-you`  
**Autenticação:** Requerida (JWT)

#### Request Body
```json
{
  "name": "Maria Silva",
  "relationshipType": "MÃE",
  "phone": "21987654321"
}
```

#### Response (201 Created)
```json
{
  "id": "live-with-id",
  "name": "Maria Silva",
  "relationshipType": "MÃE"
}
```

---

## Arquivos

### 141. Upload de Arquivo
**Método:** `POST`  
**Path:** `/file/upload`  
**Autenticação:** Requerida (JWT)  
**Content-Type:** `multipart/form-data`

#### Response (200 OK)
```json
{
  "fileName": "documento.pdf",
  "fileUrl": "/uploads/files/documento.pdf",
  "fileSize": 1024000
}
```

---

### 142. Download de Arquivo
**Método:** `GET`  
**Path:** `/file/download/{fileName}`  
**Autenticação:** Requerida (JWT)

#### Response (200 OK)
```
(arquivo binário)
```

---

### 143. Deletar Arquivo
**Método:** `DELETE`  
**Path:** `/file/{fileName}`  
**Autenticação:** Requerida (JWT)

#### Response (204 No Content)
```
(sem corpo)
```

---

## Sumário Geral de Todos os Endpoints

| # | Módulo | Método | Path | Descrição |
|---|--------|--------|------|-----------|
| 1 | Auth | POST | `/auth/login` | Login |
| 2 | Auth | POST | `/auth/register` | Registrar |
| 3-15 | Users | GET/POST/PUT/DELETE | `/users/**` | Gerenciar usuários (13 endpoints) |
| 16-17 | Admin | GET | `/admin/**` | Admin (2 endpoints) |
| 18-24 | Tasks | GET/POST/PUT/DELETE | `/task/**` | Gerenciar tarefas (7 endpoints) |
| 25-29 | Task-User | GET/POST/PUT | `/task-user-completed/**` | Tarefas concluídas (5 endpoints) |
| 30-53 | Prêmios | GET/POST/PUT/DELETE | `/received-awards/**` | Gerenciar prêmios (6 endpoints) |
| 54-77 | Prêmios-User | GET/POST/DELETE | `/user-received-awards/**` | Prêmios por usuário (24 endpoints) |
| 78-101 | Módulos | GET/POST/PUT/DELETE | `/completed-modules/**` | Gerenciar módulos (6 endpoints) |
| 102-125 | Módulos-User | GET/POST/DELETE | `/user-completed-modules/**` | Módulos por usuário (24 endpoints) |
| 126-130 | Gifts | GET/POST/PUT/DELETE | `/gifts/**` | Presentes (5 endpoints) |
| 131-133 | Gifts-Redemption | GET/POST/PUT | `/gifts-redemption/**` | Resgate de presentes (3 endpoints) |
| 134-137 | Partners | GET/POST/PUT/DELETE | `/partners/**` | Parceiros (4 endpoints) |
| 138 | Mentes Edition | GET | `/mentes-edition/**` | Edições (1 endpoint) |
| 139-140 | LiveWithYou | GET/POST/DELETE | `/live-with-you/**` | Pessoas (3 endpoints) |
| 141-143 | Files | POST/GET/DELETE | `/file/**` | Arquivos (3 endpoints) |

**Total de Endpoints: 143+**

---

## Códigos de Status HTTP

| Código | Significado |
|--------|-------------|
| 200 | OK - Sucesso |
| 201 | Created - Recurso criado |
| 204 | No Content - Sucesso sem retorno |
| 400 | Bad Request - Erro de validação |
| 401 | Unauthorized - Não autenticado |
| 403 | Forbidden - Sem permissão |
| 404 | Not Found - Não encontrado |
| 500 | Server Error - Erro no servidor |

---

## Autenticação nos Endpoints

- **Não requerida (PermitAll):** Endpoints de listagem pública, login, registro
- **Requerida (JWT):** Endpoints que modificam dados do usuário
- **Admin (JWT + Admin role):** Endpoints administrativos

### Como usar token JWT

```bash
curl -X GET http://localhost:8080/users \
  -H "Authorization: Bearer seu_token_aqui"
```

---

## Paginação

Alguns endpoints suportam paginação:

```bash
GET /users?page=1&size=10&sort=name,asc
```

---

**Última Atualização:** 2026-04-16  
**Versão API:** 1.0.0
