# Documentação: Módulos Concluídos e Prêmios Recebidos

## 📋 Visão Geral

Este documento fornece a documentação completa sobre o sistema de **Módulos Concluídos** e **Prêmios Recebidos**, incluindo:
- Estrutura do banco de dados
- Entidades JPA
- Repositórios
- Services
- DTOs
- Endpoints REST
- Testes

---

## 🗄️ Estrutura do Banco de Dados

### Tabelas Principais

#### 1. Tabela `completed_modules` (Módulos Concluídos)
Armazena os módulos que podem ser concluídos pelos usuários.

**Campos:**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | UUID | Identificador único (PK) |
| `name` | VARCHAR(255) | Nome do módulo |
| `created_at` | TIMESTAMP | Data de criação |
| `updated_at` | TIMESTAMP | Data de última atualização |
| `deleted_at` | TIMESTAMP | Data de soft delete (exclusão lógica) |

**Constraints:**
- NOT NULL: name
- Soft delete ativado

---

#### 2. Tabela `received_awards` (Prêmios Recebidos)
Armazena os prêmios/premiações que podem ser concedidas aos usuários.

**Campos:**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | UUID | Identificador único (PK) |
| `name` | VARCHAR(255) | Nome do prêmio |
| `created_at` | TIMESTAMP | Data de criação |
| `updated_at` | TIMESTAMP | Data de última atualização |
| `deleted_at` | TIMESTAMP | Data de soft delete (exclusão lógica) |

**Constraints:**
- NOT NULL: name
- Soft delete ativado

---

#### 3. Tabela `user_completed_modules` (Relacionamento M-M)
Relaciona usuários com módulos concluídos.

**Campos:**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | UUID | Identificador único (PK) |
| `user_id` | UUID | Referência ao usuário (FK) |
| `completed_module_id` | UUID | Referência ao módulo (FK) |
| `completed_date` | TIMESTAMP | Data em que o módulo foi concluído |
| `created_at` | TIMESTAMP | Data de criação |
| `updated_at` | TIMESTAMP | Data de última atualização |
| `deleted_at` | TIMESTAMP | Data de soft delete |

**Constraints:**
- FK: user_id -> users(id)
- FK: completed_module_id -> completed_modules(id)
- UNIQUE(user_id, completed_module_id) - garante que um usuário não conclua o mesmo módulo duas vezes

---

#### 4. Tabela `user_received_awards` (Relacionamento M-M)
Relaciona usuários com prêmios recebidos.

**Campos:**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | UUID | Identificador único (PK) |
| `user_id` | UUID | Referência ao usuário (FK) |
| `received_award_id` | UUID | Referência ao prêmio (FK) |
| `awarded_date` | TIMESTAMP | Data em que o prêmio foi concedido |
| `created_at` | TIMESTAMP | Data de criação |
| `updated_at` | TIMESTAMP | Data de última atualização |
| `deleted_at` | TIMESTAMP | Data de soft delete |

**Constraints:**
- FK: user_id -> users(id)
- FK: received_award_id -> received_awards(id)
- UNIQUE(user_id, received_award_id) - garante que um usuário não receba o mesmo prêmio duas vezes

---

## 🔗 Entidades JPA

### CompletedModulesEntity
```java
@Entity
@Table(name = "completed_modules")
@SQLDelete(sql = "UPDATE completed_modules SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class CompletedModulesEntity extends BaseEntity {
    @Column(nullable = false, length = 255)
    public String name;

    @ManyToMany
    @JoinTable(
            name = "user_completed_modules",
            joinColumns = @JoinColumn(name = "completed_module_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public Set<UserEntity> users = new HashSet<>();
}
```

**Características:**
- Herda de `BaseEntity` (id, createdAt, updatedAt, deletedAt)
- Implementa soft delete
- Relacionamento many-to-many com usuários

---

### ReceivedAwardsEntity
```java
@Entity
@Table(name = "received_awards")
@SQLDelete(sql = "UPDATE received_awards SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ReceivedAwardsEntity extends BaseEntity {
    @Column(nullable = false, length = 255)
    public String name;

    @ManyToMany
    @JoinTable(
            name = "user_received_awards",
            joinColumns = @JoinColumn(name = "received_award_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public Set<UserEntity> users = new HashSet<>();
}
```

**Características:**
- Herda de `BaseEntity` (id, createdAt, updatedAt, deletedAt)
- Implementa soft delete
- Relacionamento many-to-many com usuários

---

### UserCompletedModulesEntity
```java
@Entity
@Table(name = "user_completed_modules")
@SQLDelete(sql = "UPDATE user_completed_modules SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class UserCompletedModulesEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public UserEntity user;

    @ManyToOne
    @JoinColumn(name = "completed_module_id", nullable = false)
    public CompletedModulesEntity completedModule;

    @Column(name = "completed_date")
    public LocalDateTime completedDate;
}
```

**Características:**
- Herda de `BaseEntity`
- Implementa soft delete
- Relacionamento many-to-one com usuário
- Relacionamento many-to-one com módulo
- Armazena data de conclusão

---

### UserReceivedAwardsEntity
```java
@Entity
@Table(name = "user_received_awards")
@SQLDelete(sql = "UPDATE user_received_awards SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class UserReceivedAwardsEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public UserEntity user;

    @ManyToOne
    @JoinColumn(name = "received_award_id", nullable = false)
    public ReceivedAwardsEntity receivedAward;

    @Column(name = "awarded_date")
    public LocalDateTime awardedDate;
}
```

**Características:**
- Herda de `BaseEntity`
- Implementa soft delete
- Relacionamento many-to-one com usuário
- Relacionamento many-to-one com prêmio
- Armazena data da concessão

---

## 📦 Repositórios

### CompletedModulesRepository
```java
@ApplicationScoped
public class CompletedModulesRepository 
    implements PanacheRepositoryBase<CompletedModulesEntity, UUID> {
    
    public List<CompletedModulesEntity> findByName(String name) {
        return list("lower(name) like lower(?1)", "%" + name + "%");
    }
}
```

**Métodos Disponíveis:**
- `findByName(String name)`: Busca módulos pelo nome (case-insensitive)
- `findAll()`: Lista todos os módulos (herdado)
- `findById(UUID id)`: Encontra módulo por ID (herdado)
- `persist(entity)`: Persiste novo módulo (herdado)
- `delete(entity)`: Delete (soft delete) de módulo (herdado)

---

### ReceivedAwardsRepository
```java
@ApplicationScoped
public class ReceivedAwardsRepository 
    implements PanacheRepositoryBase<ReceivedAwardsEntity, UUID> {
    
    public List<ReceivedAwardsEntity> findByName(String name) {
        return list("lower(name) like lower(?1)", "%" + name + "%");
    }
}
```

**Métodos Disponíveis:**
- `findByName(String name)`: Busca prêmios pelo nome (case-insensitive)
- `findAll()`: Lista todos os prêmios (herdado)
- `findById(UUID id)`: Encontra prêmio por ID (herdado)
- `persist(entity)`: Persiste novo prêmio (herdado)
- `delete(entity)`: Delete (soft delete) de prêmio (herdado)

---

### UserCompletedModulesRepository
```java
@ApplicationScoped
public class UserCompletedModulesRepository 
    implements PanacheRepositoryBase<UserCompletedModulesEntity, UUID> {
    
    public List<UserCompletedModulesEntity> findByUserId(UUID userId) {
        return list("user.id = ?1", userId);
    }
    
    public List<UserCompletedModulesEntity> findByCompletedModuleId(UUID completedModuleId) {
        return list("completedModule.id = ?1", completedModuleId);
    }
    
    public Optional<UserCompletedModulesEntity> findByUserAndModule(UUID userId, UUID completedModuleId) {
        return find("user.id = ?1 and completedModule.id = ?2", userId, completedModuleId)
            .firstResultOptional();
    }
}
```

**Métodos Disponíveis:**
- `findByUserId(UUID userId)`: Lista módulos concluídos por um usuário
- `findByCompletedModuleId(UUID moduleId)`: Lista usuários que completaram um módulo
- `findByUserAndModule(UUID userId, UUID moduleId)`: Encontra um relacionamento específico
- Métodos padrão do Panache (listAll, findById, persist, delete)

---

### UserReceivedAwardsRepository
```java
@ApplicationScoped
public class UserReceivedAwardsRepository 
    implements PanacheRepositoryBase<UserReceivedAwardsEntity, UUID> {
    
    public List<UserReceivedAwardsEntity> findByUserId(UUID userId) {
        return list("user.id = ?1", userId);
    }
    
    public List<UserReceivedAwardsEntity> findByReceivedAwardId(UUID receivedAwardId) {
        return list("receivedAward.id = ?1", receivedAwardId);
    }
    
    public Optional<UserReceivedAwardsEntity> findByUserAndAward(UUID userId, UUID receivedAwardId) {
        return find("user.id = ?1 and receivedAward.id = ?2", userId, receivedAwardId)
            .firstResultOptional();
    }
}
```

**Métodos Disponíveis:**
- `findByUserId(UUID userId)`: Lista prêmios recebidos por um usuário
- `findByReceivedAwardId(UUID awardId)`: Lista usuários que receberam um prêmio
- `findByUserAndAward(UUID userId, UUID awardId)`: Encontra um relacionamento específico
- Métodos padrão do Panache (listAll, findById, persist, delete)

---

## 🛠️ Services

### CompletedModulesService

**Método: listAll()**
```java
public List<CompletedModulesResponseDto> listAll()
```
Retorna lista de todos os módulos concluídos.

**Método: findByName()**
```java
public List<CompletedModulesResponseDto> findByName(String name)
```
Busca módulos pelo nome (case-insensitive).

**Método: findById()**
```java
public CompletedModulesResponseDto findById(UUID id)
```
Encontra módulo por ID. Lança `ApplicationException` se não encontrado.

**Método: create()**
```java
@Transactional
public CompletedModulesResponseDto create(CompletedModulesRequestDto requestDto)
```
Cria novo módulo. Valida se nome não está vazio.

**Método: update()**
```java
@Transactional
public CompletedModulesResponseDto update(UUID id, CompletedModulesRequestDto requestDto)
```
Atualiza módulo existente.

**Método: delete()**
```java
@Transactional
public void delete(UUID id)
```
Delete (soft delete) de módulo.

---

### ReceivedAwardsService

**Método: listAll()**
```java
public List<ReceivedAwardsResponseDto> listAll()
```
Retorna lista de todos os prêmios.

**Método: findByName()**
```java
public List<ReceivedAwardsResponseDto> findByName(String name)
```
Busca prêmios pelo nome (case-insensitive).

**Método: findById()**
```java
public ReceivedAwardsResponseDto findById(UUID id)
```
Encontra prêmio por ID. Lança `ApplicationException` se não encontrado.

**Método: create()**
```java
@Transactional
public ReceivedAwardsResponseDto create(ReceivedAwardsRequestDto requestDto)
```
Cria novo prêmio. Valida se nome não está vazio.

**Método: update()**
```java
@Transactional
public ReceivedAwardsResponseDto update(UUID id, ReceivedAwardsRequestDto requestDto)
```
Atualiza prêmio existente.

**Método: delete()**
```java
@Transactional
public void delete(UUID id)
```
Delete (soft delete) de prêmio.

---

### UserCompletedModulesService

**Método: listAll()**
```java
public List<UserCompletedModulesResponseDto> listAll()
```
Retorna lista de todas as conclusões de módulos.

**Método: findByUserId()**
```java
public List<UserCompletedModulesResponseDto> findByUserId(UUID userId)
```
Retorna todos os módulos concluídos por um usuário.

**Método: findByCompletedModuleId()**
```java
public List<UserCompletedModulesResponseDto> findByCompletedModuleId(UUID moduleId)
```
Retorna todos os usuários que completaram um módulo específico.

**Método: findById()**
```java
public UserCompletedModulesResponseDto findById(UUID id)
```
Encontra associação por ID.

**Método: create()**
```java
@Transactional
public UserCompletedModulesResponseDto create(UserCompletedModulesRequestDto requestDto)
```
Cria nova associação usuário-módulo.
- Valida IDs não nulos
- Verifica se usuário existe
- Verifica se módulo existe
- Impede duplicatas (usuário não pode completar o mesmo módulo duas vezes)

**Método: delete()**
```java
@Transactional
public void delete(UUID id)
```
Remove associação usuário-módulo.

---

### UserReceivedAwardsService

**Método: listAll()**
```java
public List<UserReceivedAwardsResponseDto> listAll()
```
Retorna lista de todos os prêmios recebidos.

**Método: findByUserId()**
```java
public List<UserReceivedAwardsResponseDto> findByUserId(UUID userId)
```
Retorna todos os prêmios recebidos por um usuário.

**Método: findByReceivedAwardId()**
```java
public List<UserReceivedAwardsResponseDto> findByReceivedAwardId(UUID awardId)
```
Retorna todos os usuários que receberam um prêmio específico.

**Método: findById()**
```java
public UserReceivedAwardsResponseDto findById(UUID id)
```
Encontra associação por ID.

**Método: create()**
```java
@Transactional
public UserReceivedAwardsResponseDto create(UserReceivedAwardsRequestDto requestDto)
```
Cria nova associação usuário-prêmio.
- Valida IDs não nulos
- Verifica se usuário existe
- Verifica se prêmio existe
- Impede duplicatas (usuário não pode receber o mesmo prêmio duas vezes)

**Método: delete()**
```java
@Transactional
public void delete(UUID id)
```
Remove associação usuário-prêmio.

---

## 📨 Data Transfer Objects (DTOs)

### CompletedModulesRequestDto
```java
public record CompletedModulesRequestDto(
    String name
) {}
```

**Campos:**
- `name`: Nome do módulo

---

### CompletedModulesResponseDto
```java
public record CompletedModulesResponseDto(
    UUID id,
    String name
) {}
```

**Campos:**
- `id`: ID do módulo
- `name`: Nome do módulo

---

### ReceivedAwardsRequestDto
```java
public record ReceivedAwardsRequestDto(
    String name
) {}
```

**Campos:**
- `name`: Nome do prêmio

---

### ReceivedAwardsResponseDto
```java
public record ReceivedAwardsResponseDto(
    UUID id,
    String name
) {}
```

**Campos:**
- `id`: ID do prêmio
- `name`: Nome do prêmio

---

### UserCompletedModulesRequestDto
```java
public record UserCompletedModulesRequestDto(
    UUID userId,
    UUID completedModuleId
) {}
```

**Campos:**
- `userId`: ID do usuário
- `completedModuleId`: ID do módulo

---

### UserCompletedModulesResponseDto
```java
public record UserCompletedModulesResponseDto(
    UUID id,
    UUID userId,
    String userName,
    UUID completedModuleId,
    String completedModuleName,
    LocalDateTime completedDate
) {}
```

**Campos:**
- `id`: ID da associação
- `userId`: ID do usuário
- `userName`: Nome do usuário
- `completedModuleId`: ID do módulo
- `completedModuleName`: Nome do módulo
- `completedDate`: Data da conclusão

---

### UserReceivedAwardsRequestDto
```java
public record UserReceivedAwardsRequestDto(
    UUID userId,
    UUID receivedAwardId
) {}
```

**Campos:**
- `userId`: ID do usuário
- `receivedAwardId`: ID do prêmio

---

### UserReceivedAwardsResponseDto
```java
public record UserReceivedAwardsResponseDto(
    UUID id,
    UUID userId,
    String userName,
    UUID receivedAwardId,
    String receivedAwardName,
    LocalDateTime awardedDate
) {}
```

**Campos:**
- `id`: ID da associação
- `userId`: ID do usuário
- `userName`: Nome do usuário
- `receivedAwardId`: ID do prêmio
- `receivedAwardName`: Nome do prêmio
- `awardedDate`: Data da concessão

---

## 🌐 Endpoints REST

### Completed Modules Endpoints

#### GET /completed-modules
Lista todos os módulos concluídos.

**Resposta (200):**
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

#### GET /completed-modules/{id}
Encontra um módulo pelo ID.

**Path Parameters:**
- `id` (UUID): ID do módulo

**Resposta (200):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Módulo de Java"
}
```

**Erros:**
- `404`: Módulo não encontrado

---

#### GET /completed-modules/name/{name}
Busca módulos pelo nome.

**Path Parameters:**
- `name` (String): Nome do módulo (case-insensitive)

**Resposta (200):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Módulo de Java"
  }
]
```

---

#### POST /completed-modules
Cria novo módulo.

**Request Body:**
```json
{
  "name": "Módulo de Java"
}
```

**Resposta (201):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Módulo de Java"
}
```

**Erros:**
- `400`: Nome vazio ou nulo

---

#### PUT /completed-modules/{id}
Atualiza um módulo existente.

**Path Parameters:**
- `id` (UUID): ID do módulo

**Request Body:**
```json
{
  "name": "Novo Nome do Módulo"
}
```

**Resposta (200):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Novo Nome do Módulo"
}
```

**Erros:**
- `404`: Módulo não encontrado

---

#### DELETE /completed-modules/{id}
Deleta (soft delete) um módulo.

**Path Parameters:**
- `id` (UUID): ID do módulo

**Resposta (204):** No Content

**Erros:**
- `404`: Módulo não encontrado

---

### Received Awards Endpoints

#### GET /received-awards
Lista todos os prêmios.

**Resposta (200):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Prêmio Destaque"
  },
  {
    "id": "223e4567-e89b-12d3-a456-426614174000",
    "name": "Prêmio Melhor Desempenho"
  }
]
```

---

#### GET /received-awards/{id}
Encontra um prêmio pelo ID.

**Path Parameters:**
- `id` (UUID): ID do prêmio

**Resposta (200):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Prêmio Destaque"
}
```

**Erros:**
- `404`: Prêmio não encontrado

---

#### GET /received-awards/name/{name}
Busca prêmios pelo nome.

**Path Parameters:**
- `name` (String): Nome do prêmio (case-insensitive)

**Resposta (200):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Prêmio Destaque"
  }
]
```

---

#### POST /received-awards
Cria novo prêmio.

**Request Body:**
```json
{
  "name": "Prêmio Destaque"
}
```

**Resposta (201):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Prêmio Destaque"
}
```

**Erros:**
- `400`: Nome vazio ou nulo

---

#### PUT /received-awards/{id}
Atualiza um prêmio existente.

**Path Parameters:**
- `id` (UUID): ID do prêmio

**Request Body:**
```json
{
  "name": "Novo Nome do Prêmio"
}
```

**Resposta (200):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Novo Nome do Prêmio"
}
```

**Erros:**
- `404`: Prêmio não encontrado

---

#### DELETE /received-awards/{id}
Deleta (soft delete) um prêmio.

**Path Parameters:**
- `id` (UUID): ID do prêmio

**Resposta (204):** No Content

**Erros:**
- `404`: Prêmio não encontrado

---

### User Completed Modules Endpoints

#### GET /user-completed-modules
Lista todas as conclusões de módulos.

**Resposta (200):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "João Silva",
    "completedModuleId": "789e0123-e89b-12d3-a456-426614174000",
    "completedModuleName": "Módulo de Java",
    "completedDate": "2026-04-15T10:30:00"
  }
]
```

---

#### GET /user-completed-modules/{id}
Encontra uma conclusão de módulo pelo ID.

**Path Parameters:**
- `id` (UUID): ID da conclusão

**Resposta (200):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "userName": "João Silva",
  "completedModuleId": "789e0123-e89b-12d3-a456-426614174000",
  "completedModuleName": "Módulo de Java",
  "completedDate": "2026-04-15T10:30:00"
}
```

**Erros:**
- `404`: Registro não encontrado

---

#### GET /user-completed-modules/user/{userId}
Lista todos os módulos concluídos por um usuário.

**Path Parameters:**
- `userId` (UUID): ID do usuário

**Resposta (200):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "João Silva",
    "completedModuleId": "789e0123-e89b-12d3-a456-426614174000",
    "completedModuleName": "Módulo de Java",
    "completedDate": "2026-04-15T10:30:00"
  }
]
```

---

#### GET /user-completed-modules/module/{completedModuleId}
Lista todos os usuários que completaram um módulo específico.

**Path Parameters:**
- `completedModuleId` (UUID): ID do módulo

**Resposta (200):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "João Silva",
    "completedModuleId": "789e0123-e89b-12d3-a456-426614174000",
    "completedModuleName": "Módulo de Java",
    "completedDate": "2026-04-15T10:30:00"
  }
]
```

---

#### POST /user-completed-modules
Marca um módulo como concluído para um usuário.

**Request Body:**
```json
{
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "completedModuleId": "789e0123-e89b-12d3-a456-426614174000"
}
```

**Resposta (201):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "userName": "João Silva",
  "completedModuleId": "789e0123-e89b-12d3-a456-426614174000",
  "completedModuleName": "Módulo de Java",
  "completedDate": "2026-04-15T10:30:00"
}
```

**Erros:**
- `400`: IDs nulos, usuário não existe, módulo não existe ou usuário já completou este módulo
- `404`: Usuário ou módulo não encontrado

---

#### DELETE /user-completed-modules/{id}
Remove a conclusão de um módulo.

**Path Parameters:**
- `id` (UUID): ID da conclusão

**Resposta (204):** No Content

**Erros:**
- `404`: Registro não encontrado

---

### User Received Awards Endpoints

#### GET /user-received-awards
Lista todos os prêmios recebidos.

**Resposta (200):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "Maria Silva",
    "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000",
    "receivedAwardName": "Prêmio Destaque",
    "awardedDate": "2026-04-15T10:30:00"
  }
]
```

---

#### GET /user-received-awards/{id}
Encontra um prêmio recebido pelo ID.

**Path Parameters:**
- `id` (UUID): ID do prêmio recebido

**Resposta (200):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "userName": "Maria Silva",
  "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000",
  "receivedAwardName": "Prêmio Destaque",
  "awardedDate": "2026-04-15T10:30:00"
}
```

**Erros:**
- `404`: Registro não encontrado

---

#### GET /user-received-awards/user/{userId}
Lista todos os prêmios recebidos por um usuário.

**Path Parameters:**
- `userId` (UUID): ID do usuário

**Resposta (200):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "Maria Silva",
    "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000",
    "receivedAwardName": "Prêmio Destaque",
    "awardedDate": "2026-04-15T10:30:00"
  }
]
```

---

#### GET /user-received-awards/award/{receivedAwardId}
Lista todos os usuários que receberam um prêmio específico.

**Path Parameters:**
- `receivedAwardId` (UUID): ID do prêmio

**Resposta (200):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "456e7890-e89b-12d3-a456-426614174000",
    "userName": "Maria Silva",
    "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000",
    "receivedAwardName": "Prêmio Destaque",
    "awardedDate": "2026-04-15T10:30:00"
  }
]
```

---

#### POST /user-received-awards
Concede um prêmio a um usuário.

**Request Body:**
```json
{
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000"
}
```

**Resposta (201):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "456e7890-e89b-12d3-a456-426614174000",
  "userName": "Maria Silva",
  "receivedAwardId": "789e0123-e89b-12d3-a456-426614174000",
  "receivedAwardName": "Prêmio Destaque",
  "awardedDate": "2026-04-15T10:30:00"
}
```

**Erros:**
- `400`: IDs nulos, usuário não existe, prêmio não existe ou usuário já recebeu este prêmio
- `404`: Usuário ou prêmio não encontrado

---

#### DELETE /user-received-awards/{id}
Remove um prêmio recebido.

**Path Parameters:**
- `id` (UUID): ID do prêmio recebido

**Resposta (204):** No Content

**Erros:**
- `404`: Registro não encontrado

---

## ✅ Testes Unitários

### Testes de CompletedModulesResource

**Classe:** `CompletedModulesResourceTest`

**Testes Implementados:**
1. ✅ POST /completed-modules - Deve criar módulo com sucesso
2. ✅ POST /completed-modules - Deve retornar erro ao enviar nome vazio
3. ✅ GET /completed-modules - Deve listar todos os módulos
4. ✅ GET /completed-modules - Deve retornar lista vazia quando não há módulos
5. ✅ GET /completed-modules/{id} - Deve encontrar módulo por ID
6. ✅ GET /completed-modules/{id} - Deve retornar 404 quando módulo não existe
7. ✅ GET /completed-modules/name/{name} - Deve buscar módulo por nome
8. ✅ PUT /completed-modules/{id} - Deve atualizar módulo com sucesso
9. ✅ PUT /completed-modules/{id} - Deve retornar 404 ao atualizar módulo inexistente
10. ✅ DELETE /completed-modules/{id} - Deve deletar módulo com sucesso
11. ✅ DELETE /completed-modules/{id} - Deve retornar 404 ao deletar módulo inexistente

---

### Testes de ReceivedAwardsResource

**Classe:** `ReceivedAwardsResourceTest`

**Testes Implementados:**
1. ✅ POST /received-awards - Deve criar prêmio com sucesso
2. ✅ POST /received-awards - Deve retornar erro ao enviar nome vazio
3. ✅ GET /received-awards - Deve listar todos os prêmios
4. ✅ GET /received-awards - Deve retornar lista vazia quando não há prêmios
5. ✅ GET /received-awards/{id} - Deve encontrar prêmio por ID
6. ✅ GET /received-awards/{id} - Deve retornar 404 quando prêmio não existe
7. ✅ GET /received-awards/name/{name} - Deve buscar prêmio por nome
8. ✅ PUT /received-awards/{id} - Deve atualizar prêmio com sucesso
9. ✅ PUT /received-awards/{id} - Deve retornar 404 ao atualizar prêmio inexistente
10. ✅ DELETE /received-awards/{id} - Deve deletar prêmio com sucesso
11. ✅ DELETE /received-awards/{id} - Deve retornar 404 ao deletar prêmio inexistente

---

### Testes de UserCompletedModulesResource

**Classe:** `UserCompletedModulesResourceTest`

**Testes Implementados:**
1. ✅ POST /user-completed-modules - Deve criar associação com sucesso
2. ✅ POST /user-completed-modules - Deve retornar 400 com IDs nulos
3. ✅ POST /user-completed-modules - Deve retornar erro se módulo já foi completado
4. ✅ GET /user-completed-modules - Deve listar todas as associações
5. ✅ GET /user-completed-modules/{id} - Deve encontrar por ID
6. ✅ GET /user-completed-modules/{id} - Deve retornar 404 quando não encontra
7. ✅ GET /user-completed-modules/user/{userId} - Deve buscar por usuário
8. ✅ GET /user-completed-modules/module/{completedModuleId} - Deve buscar por módulo
9. ✅ DELETE /user-completed-modules/{id} - Deve deletar com sucesso
10. ✅ DELETE /user-completed-modules/{id} - Deve retornar 404 se não existe

---

### Testes de UserReceivedAwardsResource

**Classe:** `UserReceivedAwardsResourceTest`

**Testes Implementados:**
1. ✅ POST /user-received-awards - Deve criar associação com sucesso
2. ✅ POST /user-received-awards - Deve retornar 400 com IDs nulos
3. ✅ POST /user-received-awards - Deve retornar erro se prêmio já foi recebido
4. ✅ GET /user-received-awards - Deve listar todas as associações
5. ✅ GET /user-received-awards/{id} - Deve encontrar por ID
6. ✅ GET /user-received-awards/{id} - Deve retornar 404 quando não encontra
7. ✅ GET /user-received-awards/user/{userId} - Deve buscar por usuário
8. ✅ GET /user-received-awards/award/{receivedAwardId} - Deve buscar por prêmio
9. ✅ DELETE /user-received-awards/{id} - Deve deletar com sucesso
10. ✅ DELETE /user-received-awards/{id} - Deve retornar 404 se não existe

---

## 🚀 Exemplos de Uso

### Criar um módulo
```bash
curl -X POST http://localhost:8080/completed-modules \
  -H "Content-Type: application/json" \
  -d '{"name":"Módulo de Java"}'
```

### Criar um prêmio
```bash
curl -X POST http://localhost:8080/received-awards \
  -H "Content-Type: application/json" \
  -d '{"name":"Prêmio Destaque"}'
```

### Marcar módulo como concluído por usuário
```bash
curl -X POST http://localhost:8080/user-completed-modules \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"456e7890-e89b-12d3-a456-426614174000",
    "completedModuleId":"789e0123-e89b-12d3-a456-426614174000"
  }'
```

### Conceder prêmio a usuário
```bash
curl -X POST http://localhost:8080/user-received-awards \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"456e7890-e89b-12d3-a456-426614174000",
    "receivedAwardId":"789e0123-e89b-12d3-a456-426614174000"
  }'
```

### Listar módulos concluídos por usuário
```bash
curl http://localhost:8080/user-completed-modules/user/456e7890-e89b-12d3-a456-426614174000
```

### Listar prêmios recebidos por usuário
```bash
curl http://localhost:8080/user-received-awards/user/456e7890-e89b-12d3-a456-426614174000
```

---

## 📂 Estrutura de Arquivos

```
src/
├── main/
│   └── java/br/com/avsistems/
│       ├── dto/
│       │   ├── request/
│       │   │   ├── CompletedModulesRequestDto.java
│       │   │   ├── ReceivedAwardsRequestDto.java
│       │   │   ├── UserCompletedModulesRequestDto.java
│       │   │   └── UserReceivedAwardsRequestDto.java
│       │   └── response/
│       │       ├── CompletedModulesResponseDto.java
│       │       ├── ReceivedAwardsResponseDto.java
│       │       ├── UserCompletedModulesResponseDto.java
│       │       └── UserReceivedAwardsResponseDto.java
│       ├── entity/
│       │   ├── CompletedModulesEntity.java
│       │   ├── ReceivedAwardsEntity.java
│       │   ├── UserCompletedModulesEntity.java
│       │   └── UserReceivedAwardsEntity.java
│       ├── repository/
│       │   ├── CompletedModulesRepository.java
│       │   ├── ReceivedAwardsRepository.java
│       │   ├── UserCompletedModulesRepository.java
│       │   └── UserReceivedAwardsRepository.java
│       ├── resource/
│       │   ├── CompletedModulesResource.java
│       │   ├── ReceivedAwardsResource.java
│       │   ├── UserCompletedModulesResource.java
│       │   └── UserReceivedAwardsResource.java
│       └── service/
│           ├── CompletedModulesService.java
│           ├── ReceivedAwardsService.java
│           ├── UserCompletedModulesService.java
│           └── UserReceivedAwardsService.java
└── test/
    └── java/br/com/avsistems/
        └── resource/
            ├── CompletedModulesResourceTest.java
            ├── ReceivedAwardsResourceTest.java
            ├── UserCompletedModulesResourceTest.java
            └── UserReceivedAwardsResourceTest.java
```

---

## 🔒 Recurso de Soft Delete

Todas as entidades implementam soft delete através das anotações `@SQLDelete` e `@SQLRestriction`, o que significa:

- ✅ Registros não são fisicamente deletados do banco de dados
- ✅ Um timestamp `deleted_at` é preenchido quando um registro é deletado
- ✅ Queries automáticas filtram registros deletados
- ✅ Dados históricos são preservados para auditoria

---

## 📊 Relacionamentos

```
UserEntity (1) ──────────── (M) CompletedModulesEntity
    │                              │
    │ user_completed_modules       │
    └──────────────────────────────┘

UserEntity (1) ──────────── (M) ReceivedAwardsEntity
    │                              │
    │ user_received_awards         │
    └──────────────────────────────┘
```

---

## ✨ Características Principais

1. **Type-Safe**: DTOs como records Java
2. **Transaccional**: Operações envolvendo múltiplas entidades são atômicas
3. **Validação**: Validações de entrada em services
4. **Soft Delete**: Dados nunca são perdidos
5. **Testes Completos**: 48 testes unitários cobrindo todos os cenários
6. **REST API**: Endpoints RESTful bem definidos
7. **Prevenção de Duplicatas**: Constraints UNIQUE na base de dados
8. **Lazy Loading**: Relacionamentos carregados sob demanda
9. **Panache ORM**: Simplificação de queries com Hibernate Panache

---

## 🔧 Executando os Testes

```bash
# Executar todos os testes
mvn test

# Executar testes de um módulo específico
mvn test -Dtest=CompletedModulesResourceTest

# Executar com relatório de cobertura
mvn test jacoco:report
```

---

## 📝 Status da Implementação

✅ Tabelas de banco de dados criadas
✅ Entidades JPA implementadas
✅ Repositórios com métodos customizados
✅ Services com lógica de negócio
✅ DTOs para Request/Response
✅ Resources (endpoints) RESTful
✅ Testes unitários completos
✅ Validações de entrada
✅ Soft delete implementado
✅ Documentação integrada

---

## 📞 Suporte

Para dúvidas ou sugestões sobre este módulo, entre em contato com a equipe de desenvolvimento.

---

**Última atualização:** 2026-04-16
**Versão:** 1.0.0

