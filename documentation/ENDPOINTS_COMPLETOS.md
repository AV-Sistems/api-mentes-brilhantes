# 📡 Documentação Completa de Endpoints - API Mentes Brilhantes

## Módulos Concluídos

### 1. Listar Todos os Módulos
**Método:** `GET`  
**Path:** `/completed-modules`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/completed-modules
```

#### Response (200 OK)
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Módulo de Java"
  },
  {
    "id": "223e4567-e89b-12d3-a456-426614174000",
    "name": "Módulo de Python"
  }
]
```

---

### 2. Buscar Módulo por ID
**Método:** `GET`  
**Path:** `/completed-modules/{id}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/completed-modules/123e4567-e89b-12d3-a456-426614174000
```

#### Response (200 OK)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Módulo de Java"
}
```

#### Response (404 Not Found)
```json
{
  "message": "Módulo com ID: 123e4567-e89b-12d3-a456-426614174000 não encontrado."
}
```

---

### 3. Buscar Módulos por Nome
**Método:** `GET`  
**Path:** `/completed-modules/name/{name}`  
**Autenticação:** Não requerida  
**Descrição:** Busca case-insensitive por parte do nome

#### Request
```bash
curl -X GET http://localhost:8080/completed-modules/name/Java
```

#### Response (200 OK)
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Módulo de Java"
  }
]
```

#### Response (200 OK - Sem resultados)
```json
[]
```

---

### 4. Criar Novo Módulo
**Método:** `POST`  
**Path:** `/completed-modules`  
**Autenticação:** Não requerida  
**Content-Type:** `application/json`

#### Request Body
```json
{
  "name": "Módulo de Java"
}
```

#### Request
```bash
curl -X POST http://localhost:8080/completed-modules \
  -H "Content-Type: application/json" \
  -d '{"name": "Módulo de Java"}'
```

#### Response (201 Created)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Módulo de Java"
}
```

#### Response (400 Bad Request)
```json
{
  "message": "O nome do módulo não pode estar vazio."
}
```

---

### 5. Atualizar Módulo
**Método:** `PUT`  
**Path:** `/completed-modules/{id}`  
**Autenticação:** Não requerida  
**Content-Type:** `application/json`

#### Request Body
```json
{
  "name": "Novo Nome do Módulo"
}
```

#### Request
```bash
curl -X PUT http://localhost:8080/completed-modules/123e4567-e89b-12d3-a456-426614174000 \
  -H "Content-Type: application/json" \
  -d '{"name": "Novo Nome do Módulo"}'
```

#### Response (200 OK)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Novo Nome do Módulo"
}
```

#### Response (404 Not Found)
```json
{
  "message": "Módulo com ID: 123e4567-e89b-12d3-a456-426614174000 não encontrado."
}
```

---

### 6. Deletar Módulo
**Método:** `DELETE`  
**Path:** `/completed-modules/{id}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X DELETE http://localhost:8080/completed-modules/123e4567-e89b-12d3-a456-426614174000
```

#### Response (204 No Content)
```
(sem corpo)
```

#### Response (404 Not Found)
```json
{
  "message": "Módulo com ID: 123e4567-e89b-12d3-a456-426614174000 não encontrado."
}
```

---

## Prêmios Recebidos

### 7. Listar Todos os Prêmios
**Método:** `GET`  
**Path:** `/received-awards`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/received-awards
```

#### Response (200 OK)
```json
[
  {
    "id": "789e0123-e89b-12d3-a456-426614174000",
    "name": "Prêmio Destaque"
  },
  {
    "id": "889e0123-e89b-12d3-a456-426614174000",
    "name": "Prêmio Melhor Desempenho"
  }
]
```

---

### 8. Buscar Prêmio por ID
**Método:** `GET`  
**Path:** `/received-awards/{id}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/received-awards/789e0123-e89b-12d3-a456-426614174000
```

#### Response (200 OK)
```json
{
  "id": "789e0123-e89b-12d3-a456-426614174000",
  "name": "Prêmio Destaque"
}
```

#### Response (404 Not Found)
```json
{
  "message": "Prêmio com ID: 789e0123-e89b-12d3-a456-426614174000 não encontrado."
}
```

---

### 9. Buscar Prêmios por Nome
**Método:** `GET`  
**Path:** `/received-awards/name/{name}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/received-awards/name/Destaque
```

#### Response (200 OK)
```json
[
  {
    "id": "789e0123-e89b-12d3-a456-426614174000",
    "name": "Prêmio Destaque"
  }
]
```

---

### 10. Criar Novo Prêmio
**Método:** `POST`  
**Path:** `/received-awards`  
**Autenticação:** Não requerida  
**Content-Type:** `multipart/form-data`

#### Campos do FormData
- `data`: `{"name":"Prêmio Destaque"}`
- `image`: arquivo opcional da premiação

#### Request
```bash
curl -X POST http://localhost:8080/received-awards \
  -F 'data={"name":"Prêmio Destaque"}' \
  -F 'image=@/caminho/chapolim.png'
```

#### Response (201 Created)
```json
{
  "id": "789e0123-e89b-12d3-a456-426614174000",
  "name": "Prêmio Destaque",
  "imageUrl": "uploads/received-awards/789e0123-e89b-12d3-a456-426614174000.png"
}
```

#### Response (400 Bad Request)
```json
{
  "message": "O nome do prêmio não pode estar vazio."
}
```

---

### 11. Atualizar Prêmio
**Método:** `PUT`  
**Path:** `/received-awards/{id}`  
**Autenticação:** Não requerida  
**Content-Type:** `multipart/form-data`

#### Campos do FormData
- `data`: `{"name":"Novo Nome do Prêmio"}`
- `image`: arquivo opcional para substituir a imagem atual

#### Request
```bash
curl -X PUT http://localhost:8080/received-awards/789e0123-e89b-12d3-a456-426614174000 \
  -F 'data={"name":"Novo Nome do Prêmio"}' \
  -F 'image=@/caminho/novo-premio.png'
```

#### Response (200 OK)
```json
{
  "id": "789e0123-e89b-12d3-a456-426614174000",
  "name": "Novo Nome do Prêmio",
  "imageUrl": "uploads/received-awards/789e0123-e89b-12d3-a456-426614174000.png"
}
```

---

### 12. Deletar Prêmio
**Método:** `DELETE`  
**Path:** `/received-awards/{id}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X DELETE http://localhost:8080/received-awards/789e0123-e89b-12d3-a456-426614174000
```

#### Response (204 No Content)
```
(sem corpo)
```

---

## Conclusões de Módulos por Usuário

### 13. Listar Todas as Conclusões
**Método:** `GET`  
**Path:** `/user-completed-modules`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/user-completed-modules
```

#### Response (200 OK)
```json
[
  {
    "id": "999e1111-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "João Silva",
    "completedModuleId": "123e4567-e89b-12d3-a456-426614174000",
    "completedModuleName": "Módulo de Java",
    "completedDate": "2026-04-16T17:30:00"
  }
]
```

---

### 14. Buscar Conclusão por ID
**Método:** `GET`  
**Path:** `/user-completed-modules/{id}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/user-completed-modules/999e1111-e89b-12d3-a456-426614174000
```

#### Response (200 OK)
```json
{
  "id": "999e1111-e89b-12d3-a456-426614174000",
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "userName": "João Silva",
  "completedModuleId": "123e4567-e89b-12d3-a456-426614174000",
  "completedModuleName": "Módulo de Java",
  "completedDate": "2026-04-16T17:30:00"
}
```

#### Response (404 Not Found)
```json
{
  "message": "Registro com ID: 999e1111-e89b-12d3-a456-426614174000 não encontrado."
}
```

---

### 15. Listar Módulos Concluídos por Usuário
**Método:** `GET`  
**Path:** `/user-completed-modules/user/{userId}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/user-completed-modules/user/456e7890-e89b-12d3-a456-426614174000
```

#### Response (200 OK)
```json
[
  {
    "id": "999e1111-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "João Silva",
    "completedModuleId": "123e4567-e89b-12d3-a456-426614174000",
    "completedModuleName": "Módulo de Java",
    "completedDate": "2026-04-16T17:30:00"
  },
  {
    "id": "999e1112-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "João Silva",
    "completedModuleId": "223e4567-e89b-12d3-a456-426614174000",
    "completedModuleName": "Módulo de Python",
    "completedDate": "2026-04-15T14:20:00"
  }
]
```

---

### 16. Listar Usuários que Completaram um Módulo
**Método:** `GET`  
**Path:** `/user-completed-modules/module/{completedModuleId}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/user-completed-modules/module/123e4567-e89b-12d3-a456-426614174000
```

#### Response (200 OK)
```json
[
  {
    "id": "999e1111-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "João Silva",
    "completedModuleId": "123e4567-e89b-12d3-a456-426614174000",
    "completedModuleName": "Módulo de Java",
    "completedDate": "2026-04-16T17:30:00"
  }
]
```

---

### 17. Marcar Módulo como Concluído
**Método:** `POST`  
**Path:** `/user-completed-modules`  
**Autenticação:** Não requerida  
**Content-Type:** `application/json`

#### Request Body
```json
{
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "completedModuleId": "123e4567-e89b-12d3-a456-426614174000"
}
```

#### Request
```bash
curl -X POST http://localhost:8080/user-completed-modules \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "completedModuleId": "123e4567-e89b-12d3-a456-426614174000"
  }'
```

#### Response (201 Created)
```json
{
  "id": "999e1111-e89b-12d3-a456-426614174000",
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "userName": "João Silva",
  "completedModuleId": "123e4567-e89b-12d3-a456-426614174000",
  "completedModuleName": "Módulo de Java",
  "completedDate": "2026-04-16T17:30:00"
}
```

#### Response (400 Bad Request - IDs nulos)
```json
{
  "message": "IDs de usuário e módulo não podem ser nulos."
}
```

#### Response (400 Bad Request - Já concluído)
```json
{
  "message": "O usuário já completou este módulo."
}
```

#### Response (404 Not Found - Usuário)
```json
{
  "message": "Usuário com ID: 456e7890-e89b-12d3-a456-426614174000 não encontrado."
}
```

#### Response (404 Not Found - Módulo)
```json
{
  "message": "Módulo com ID: 123e4567-e89b-12d3-a456-426614174000 não encontrado."
}
```

---

### 18. Remover Conclusão de Módulo
**Método:** `DELETE`  
**Path:** `/user-completed-modules/{id}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X DELETE http://localhost:8080/user-completed-modules/999e1111-e89b-12d3-a456-426614174000
```

#### Response (204 No Content)
```
(sem corpo)
```

#### Response (404 Not Found)
```json
{
  "message": "Registro com ID: 999e1111-e89b-12d3-a456-426614174000 não encontrado."
}
```

---

## Prêmios Recebidos por Usuário

### 19. Listar Todos os Prêmios Recebidos
**Método:** `GET`  
**Path:** `/user-received-awards`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/user-received-awards
```

#### Response (200 OK)
```json
[
  {
    "id": "888e2222-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "Maria Silva",
    "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000",
    "receivedAwardName": "Prêmio Destaque",
    "awardedDate": "2026-04-16T17:30:00"
  }
]
```

---

### 20. Buscar Prêmio Recebido por ID
**Método:** `GET`  
**Path:** `/user-received-awards/{id}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/user-received-awards/888e2222-e89b-12d3-a456-426614174000
```

#### Response (200 OK)
```json
{
  "id": "888e2222-e89b-12d3-a456-426614174000",
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "userName": "Maria Silva",
  "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000",
  "receivedAwardName": "Prêmio Destaque",
  "awardedDate": "2026-04-16T17:30:00"
}
```

---

### 21. Listar Prêmios Recebidos por Usuário
**Método:** `GET`  
**Path:** `/user-received-awards/user/{userId}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/user-received-awards/user/456e7890-e89b-12d3-a456-426614174000
```

#### Response (200 OK)
```json
[
  {
    "id": "888e2222-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "Maria Silva",
    "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000",
    "receivedAwardName": "Prêmio Destaque",
    "awardedDate": "2026-04-16T17:30:00"
  }
]
```

---

### 22. Listar Usuários que Receberam um Prêmio
**Método:** `GET`  
**Path:** `/user-received-awards/award/{receivedAwardId}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X GET http://localhost:8080/user-received-awards/award/789e0123-e89b-12d3-a456-426614174000
```

#### Response (200 OK)
```json
[
  {
    "id": "888e2222-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "Maria Silva",
    "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000",
    "receivedAwardName": "Prêmio Destaque",
    "awardedDate": "2026-04-16T17:30:00"
  }
]
```

---

### 23. Conceder Prêmio a Usuário
**Método:** `POST`  
**Path:** `/user-received-awards`  
**Autenticação:** Não requerida  
**Content-Type:** `application/json`

#### Request Body
```json
{
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000"
}
```

#### Request
```bash
curl -X POST http://localhost:8080/user-received-awards \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000"
  }'
```

#### Response (201 Created)
```json
{
  "id": "888e2222-e89b-12d3-a456-426614174000",
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "userName": "Maria Silva",
  "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000",
  "receivedAwardName": "Prêmio Destaque",
  "awardedDate": "2026-04-16T17:30:00"
}
```

#### Response (400 Bad Request - IDs nulos)
```json
{
  "message": "IDs de usuário e prêmio não podem ser nulos."
}
```

#### Response (400 Bad Request - Já recebido)
```json
{
  "message": "O usuário já recebeu este prêmio."
}
```

#### Response (404 Not Found - Usuário)
```json
{
  "message": "Usuário com ID: 456e7890-e89b-12d3-a456-426614174000 não encontrado."
}
```

#### Response (404 Not Found - Prêmio)
```json
{
  "message": "Prêmio com ID: 789e0123-e89b-12d3-a456-426614174000 não encontrado."
}
```

---

### 24. Remover Prêmio Recebido
**Método:** `DELETE`  
**Path:** `/user-received-awards/{id}`  
**Autenticação:** Não requerida

#### Request
```bash
curl -X DELETE http://localhost:8080/user-received-awards/888e2222-e89b-12d3-a456-426614174000
```

#### Response (204 No Content)
```
(sem corpo)
```

#### Response (404 Not Found)
```json
{
  "message": "Registro com ID: 888e2222-e89b-12d3-a456-426614174000 não encontrado."
}
```

---

## Sumário de Endpoints

| # | Método | Path | Descrição |
|---|--------|------|-----------|
| 1 | GET | `/completed-modules` | Listar todos os módulos |
| 2 | GET | `/completed-modules/{id}` | Buscar módulo por ID |
| 3 | GET | `/completed-modules/name/{name}` | Buscar módulos por nome |
| 4 | POST | `/completed-modules` | Criar novo módulo |
| 5 | PUT | `/completed-modules/{id}` | Atualizar módulo |
| 6 | DELETE | `/completed-modules/{id}` | Deletar módulo |
| 7 | GET | `/received-awards` | Listar todos os prêmios |
| 8 | GET | `/received-awards/{id}` | Buscar prêmio por ID |
| 9 | GET | `/received-awards/name/{name}` | Buscar prêmios por nome |
| 10 | POST | `/received-awards` | Criar novo prêmio |
| 11 | PUT | `/received-awards/{id}` | Atualizar prêmio |
| 12 | DELETE | `/received-awards/{id}` | Deletar prêmio |
| 13 | GET | `/user-completed-modules` | Listar todas as conclusões |
| 14 | GET | `/user-completed-modules/{id}` | Buscar conclusão por ID |
| 15 | GET | `/user-completed-modules/user/{userId}` | Módulos por usuário |
| 16 | GET | `/user-completed-modules/module/{moduleId}` | Usuários que completaram módulo |
| 17 | POST | `/user-completed-modules` | Marcar módulo como concluído |
| 18 | DELETE | `/user-completed-modules/{id}` | Remover conclusão |
| 19 | GET | `/user-received-awards` | Listar todos os prêmios recebidos |
| 20 | GET | `/user-received-awards/{id}` | Buscar prêmio recebido por ID |
| 21 | GET | `/user-received-awards/user/{userId}` | Prêmios por usuário |
| 22 | GET | `/user-received-awards/award/{awardId}` | Usuários que receberam prêmio |
| 23 | POST | `/user-received-awards` | Conceder prêmio a usuário |
| 24 | DELETE | `/user-received-awards/{id}` | Remover prêmio recebido |

---

## Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 204 | No Content - Requisição bem-sucedida sem retorno |
| 400 | Bad Request - Erro na validação ou dados inválidos |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro no servidor |

---

## Headers Padrão

### Request
```
Content-Type: application/json
Accept: application/json
```

### Response
```
Content-Type: application/json
```

---

## Tipo de Dados

### UUID
Formato: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`  
Exemplo: `123e4567-e89b-12d3-a456-426614174000`

### DateTime (ISO 8601)
Formato: `YYYY-MM-DDTHH:mm:ss`  
Exemplo: `2026-04-16T17:30:00`

---

## Notas Importantes

1. **Soft Delete**: Todos os deletes são soft deletes. Os dados não são fisicamente removidos do banco.
2. **Prevenção de Duplicatas**: Um usuário não pode completar o mesmo módulo duas vezes nem receber o mesmo prêmio duas vezes.
3. **Validação**: Todos os campos obrigatórios são validados no servidor.
4. **Timestamps**: As datas de conclusão/concessão são automaticamente preenchidas com a data/hora atual.

---

## Autenticação Futura

Atualmente, nenhum endpoint requer autenticação. Em versões futuras, será necessário incluir JWT tokens no header `Authorization`.

```
Authorization: Bearer <token>
```

---

**Versão:** 1.0.0  
**Data:** 2026-04-16  
**Framework:** Quarkus 3.32.4  
**Banco de Dados:** PostgreSQL 16
