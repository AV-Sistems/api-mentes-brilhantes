# Documentação: Tabelas de Módulos Concluídos e Prêmios Recebidos

## Visão Geral
Foram criadas duas novas tabelas no banco de dados com relacionamentos many-to-many com a tabela `users`:

### 1. Tabela `completed_modules` (Módulos Concluídos)
Armazena os módulos que podem ser concluídos pelos usuários.

**Campos:**
- `id` (UUID): Identificador único
- `name` (VARCHAR 255): Nome do módulo
- `created_at`: Data de criação
- `updated_at`: Data de última atualização
- `deleted_at`: Data de soft delete (exclusão lógica)

**Relacionamento:**
- Muitos para muitos com `users` através da tabela `user_completed_modules`

---

### 2. Tabela `received_awards` (Prêmios Recebidos)
Armazena os prêmios/premiações que podem ser concedidas aos usuários.

**Campos:**
- `id` (UUID): Identificador único
- `name` (VARCHAR 255): Nome do prêmio
- `image_url` (VARCHAR 255): Caminho da imagem da premiação
- `created_at`: Data de criação
- `updated_at`: Data de última atualização
- `deleted_at`: Data de soft delete (exclusão lógica)

**Relacionamento:**
- Muitos para muitos com `users` através da tabela `user_received_awards`

---

## Tabelas de Junção (Junction Tables)

### Tabela `user_completed_modules`
Relaciona usuários com módulos concluídos.

**Campos:**
- `id` (UUID): Identificador único do relacionamento
- `user_id` (UUID): Referência ao usuário
- `completed_module_id` (UUID): Referência ao módulo
- `completed_date` (TIMESTAMP): Data em que o módulo foi concluído
- `created_at`, `updated_at`, `deleted_at`: Auditoria

**Constraints:**
- Chave estrangeira para `users(id)`
- Chave estrangeira para `completed_modules(id)`
- Constraint UNIQUE (user_id, completed_module_id) - garante que um usuário não conclua o mesmo módulo duas vezes

---

### Tabela `user_received_awards`
Relaciona usuários com prêmios recebidos.

**Campos:**
- `id` (UUID): Identificador único do relacionamento
- `user_id` (UUID): Referência ao usuário
- `received_award_id` (UUID): Referência ao prêmio
- `awarded_date` (TIMESTAMP): Data em que o prêmio foi concedido
- `created_at`, `updated_at`, `deleted_at`: Auditoria

**Constraints:**
- Chave estrangeira para `users(id)`
- Chave estrangeira para `received_awards(id)`
- Constraint UNIQUE (user_id, received_award_id) - garante que um usuário não receba o mesmo prêmio duas vezes

---

## Entidades JPA Criadas

### 1. `CompletedModulesEntity`
Entidade que representa um módulo concluído.

**Propriedades:**
- Herda de `BaseEntity` (id, createdAt, updatedAt, deletedAt)
- `name`: Nome do módulo
- `users`: Set de usuários que completaram este módulo (relacionamento many-to-many)

**Anotações:**
- `@Entity` com `@Table(name = "completed_modules")`
- `@SQLDelete` e `@SQLRestriction` para soft delete

---

### 2. `ReceivedAwardsEntity`
Entidade que representa um prêmio recebido.

**Propriedades:**
- Herda de `BaseEntity` (id, createdAt, updatedAt, deletedAt)
- `name`: Nome do prêmio
- `imageUrl`: Caminho da imagem da premiação
- `users`: Set de usuários que receberam este prêmio (relacionamento many-to-many)

**Anotações:**
- `@Entity` com `@Table(name = "received_awards")`
- `@SQLDelete` e `@SQLRestriction` para soft delete

---

### 3. `UserCompletedModulesEntity`
Entidade que representa o relacionamento entre usuário e módulo concluído.

**Propriedades:**
- Herda de `BaseEntity` (id, createdAt, updatedAt, deletedAt)
- `user`: Referência para o usuário
- `completedModule`: Referência para o módulo
- `completedDate`: Data de conclusão

---

### 4. `UserReceivedAwardsEntity`
Entidade que representa o relacionamento entre usuário e prêmio recebido.

**Propriedades:**
- Herda de `BaseEntity` (id, createdAt, updatedAt, deletedAt)
- `user`: Referência para o usuário
- `receivedAward`: Referência para o prêmio
- `awardedDate`: Data da concessão do prêmio

---

## Atualização da UserEntity

A entidade `UserEntity` foi atualizada para incluir os relacionamentos many-to-many:

```java
@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
public Set<CompletedModulesEntity> completedModules = new HashSet<>();

@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
public Set<ReceivedAwardsEntity> receivedAwards = new HashSet<>();
```

---

## Repositórios Criados

Foram criados 4 repositórios para gerenciar as entidades:

1. **`CompletedModulesRepository`** - Gerencia operações CRUD para módulos concluídos
2. **`ReceivedAwardsRepository`** - Gerencia operações CRUD para prêmios recebidos
3. **`UserCompletedModulesRepository`** - Gerencia relacionamentos usuário-módulo
4. **`UserReceivedAwardsRepository`** - Gerencia relacionamentos usuário-prêmio

Todos herdam de `PanacheRepositoryBase<Entity, UUID>` do Quarkus Panache, fornecendo operações CRUD automáticas.

---

## Migrations Flyway

Foram criadas duas migrations SQL:

1. **V3__create_completed_modules_table.sql** - Cria as tabelas `completed_modules` e `user_completed_modules`
2. **V4__create_received_awards_table.sql** - Cria as tabelas `received_awards` e `user_received_awards`
3. **V5__add_image_url_to_received_awards.sql** - Adiciona o campo `image_url` em `received_awards`

As migrations serão executadas automaticamente quando a aplicação iniciar.

---

## Exemplo de Uso

### Cadastro inicial do usuário com seleções
O endpoint `POST /auth/register` agora aceita opcionalmente:

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

### Cadastro administrativo de premiações com imagem
O endpoint `POST /received-awards` passou a receber `multipart/form-data` com:

- `data`: JSON em texto com o campo `name`
- `image`: arquivo opcional da premiação

### Adicionar um módulo concluído para um usuário:
```java
UserEntity user = userRepository.findById(userId);
CompletedModulesEntity module = completedModulesRepository.findById(moduleId);

UserCompletedModulesEntity relation = new UserCompletedModulesEntity(user, module);
userCompletedModulesRepository.persist(relation);
```

### Adicionar um prêmio para um usuário:
```java
UserEntity user = userRepository.findById(userId);
ReceivedAwardsEntity award = receivedAwardsRepository.findById(awardId);

UserReceivedAwardsEntity relation = new UserReceivedAwardsEntity(user, award);
userReceivedAwardsRepository.persist(relation);
```

### Acessar módulos concluídos de um usuário:
```java
UserEntity user = userRepository.findById(userId);
Set<CompletedModulesEntity> completedModules = user.completedModules;
```

### Acessar prêmios recebidos de um usuário:
```java
UserEntity user = userRepository.findById(userId);
Set<ReceivedAwardsEntity> receivedAwards = user.receivedAwards;
```

---

## Recurso de Soft Delete

Todas as entidades implementam soft delete através das anotações `@SQLDelete` e `@SQLRestriction`, o que significa que:
- Registros não são fisicamente deletados do banco de dados
- Um timestamp `deleted_at` é preenchido quando um registro é deletado
- Queries automáticas filtram registros deletados

---

## Status Atual

✅ Tabelas de banco de dados criadas
✅ Entidades JPA implementadas
✅ Repositórios criados
✅ Relacionamentos many-to-many configurados
✅ Soft delete implementado
✅ Projeto compilado com sucesso

---
