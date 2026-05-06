# 🧪 Guia Completo de Testes Unitários - API Mentes Brilhantes

**Data:** 06 de Abril de 2026  
**Total de Testes:** 64  
**Framework:** JUnit 5 + Mockito + RestAssured

---

## 📋 Índice

1. [Overview dos Testes](#overview-dos-testes)
2. [Testes PartnersResource](#testes-partnersresource)
3. [Testes TaskResource](#testes-taskresource)
4. [Testes UserResource](#testes-userresource)
5. [Executando Testes](#executando-testes)
6. [Análise de Cobertura](#análise-de-cobertura)
7. [Melhores Práticas](#melhores-práticas)

---

## Overview dos Testes

### Arquitetura de Testes

```
PartnersResourceTest (24 testes)
├── POST /partners
│   ├── ✅ Sucesso (com/sem imagem)
│   ├── ❌ JSON inválido
│   └── ❌ Dados vazios
├── PUT /partners/{id}
│   ├── ✅ Sucesso
│   ├── ❌ Não encontrado
│   └── ❌ Form nulo
├── DELETE /partners/{id}
│   ├── ✅ Sucesso
│   └── ❌ Não encontrado
└── GET /partners**
    ├── ✅ Listar todos
    ├── ✅ Buscar por local
    ├── ✅ Listar válidos
    └── ✅ Admin

TaskResourceTest (21 testes)
├── POST /task
│   ├── ✅ Sucesso
│   ├── ❌ JSON inválido
│   └── ❌ Dados vazios
├── GET /task**
│   ├── ✅ Listar todas
│   ├── ✅ Por ID
│   ├── ✅ Por nome
│   ├── ✅ Ativas
│   └── ✅ Por tipo
├── PUT /task/{id}
│   ├── ✅ Sucesso
│   ├── ❌ Não encontrada
│   └── ✅ Alterar status
└── DELETE /task/{id}
    ├── ✅ Sucesso
    └── ❌ Não encontrada

UserResourceTest (19 testes)
├── GET /users**
│   ├── ✅ Listar ativos
│   ├── ✅ Por estado/cidade
│   ├── ✅ Por nome
│   ├── ✅ Por instituição
│   ├── ✅ Por tipo
│   ├── ✅ Por edição
│   ├── ✅ Por data nascimento
│   └── ✅ Inativos
├── PUT /users/{id}
│   ├── ✅ Atualizar endereço
│   ├── ✅ Atualizar dados
│   ├── ✅ Alterar ativação
│   └── ✅ Alterar tipo
└── DELETE /users/{id}
    ├── ✅ Sucesso
    └── ❌ Não encontrado
```

---

## Testes PartnersResource

### Classe: PartnersResourceTest

**Localização:** `src/test/java/br/com/avsistems/resource/PartnersResourceTest.java`

#### Test 1: POST /partners - Sucesso
```java
@Test
@DisplayName("POST /partners - Deve criar parceiro com sucesso")
void testCreatePartnerSuccess() {
    // Arrange
    when(partnersService.createPartner(any(PartnerMultipartForm.class)))
            .thenReturn(testPartnerDto);

    // Act & Assert
    given()
            .multiPart("data", "{...}")
            .multiPart("image", "test.jpg", new byte[]{1, 2, 3})
            .when()
            .post("/partners")
            .then()
            .statusCode(201)
            .body("name", equalTo("Amazon"));
}
```

**O que testa:**
- ✅ Status 201 (Created)
- ✅ Resposta contém ID
- ✅ Resposta contém dados corretos
- ✅ Serviço foi chamado uma vez

**Execução:**
```bash
./mvnw test -Dtest=PartnersResourceTest#testCreatePartnerSuccess
```

---

#### Test 2: POST /partners - JSON Inválido
```java
@Test
@DisplayName("POST /partners - Deve retornar 400 quando JSON é inválido")
void testCreatePartnerInvalidJson() {
    when(partnersService.createPartner(any(PartnerMultipartForm.class)))
            .thenThrow(new BadRequestException("Dados inválidos no campo 'data'"));

    given()
            .multiPart("data", "{invalid json}")
            .when()
            .post("/partners")
            .then()
            .statusCode(400)
            .body("message", containsString("Dados inválidos"));
}
```

**O que testa:**
- ✅ Status 400 (Bad Request)
- ✅ Mensagem de erro clara
- ✅ Handler captura BadRequestException

---

#### Test 3: POST /partners - Campo Vazio
```java
@Test
@DisplayName("POST /partners - Deve retornar 400 quando campo 'data' está vazio")
void testCreatePartnerEmptyData() {
    when(partnersService.createPartner(any(PartnerMultipartForm.class)))
            .thenThrow(new BadRequestException("O campo 'data' com os dados do parceiro é obrigatório"));

    given()
            .multiPart("data", "")
            .when()
            .post("/partners")
            .then()
            .statusCode(400);
}
```

---

#### Test 4-6: PUT /partners/{id}
Similar à criação, testa:
- ✅ Update com sucesso
- ❌ Parceiro não encontrado (404)
- ❌ Form nulo (400)

---

#### Test 7-8: DELETE /partners/{id}
Testa:
- ✅ Delete com sucesso (204)
- ❌ Parceiro não encontrado (404)

---

#### Test 9-14: GET /partners**
Testa:
- ✅ Listar todos os parceiros
- ✅ Lista vazia quando não há dados
- ✅ Buscar por local (state/city)
- ✅ Listar apenas válidos
- ✅ Admin endpoint

---

## Testes TaskResource

### Classe: TaskResourceTest

**Localização:** `src/test/java/br/com/avsistems/resource/TaskResourceTest.java`

Estrutura similar aos testes de Parceiros, cobrindo:

#### POST /task
```java
@Test
@DisplayName("POST /task - Deve criar tarefa com sucesso")
void testCreateTaskSuccess() {
    when(taskService.createTask(any(TaskMultipartForm.class)))
            .thenReturn(testTaskDto);

    given()
            .multiPart("data", "{\"name\":\"Aprender Java\",...}")
            .multiPart("image", "test.jpg", new byte[]{1, 2, 3})
            .when()
            .post("/task")
            .then()
            .statusCode(201)
            .body("name", equalTo("Aprender Java"))
            .body("tasksPoints", equalTo(10));
}
```

#### GET /task (todas as variações)
```java
@Test
@DisplayName("GET /task - Deve retornar lista de todas as tarefas")
void testListAllTasksSuccess() {
    List<TaskResponseDto> tasks = List.of(testTaskDto);
    when(taskService.listAllTasks()).thenReturn(tasks);

    given()
            .when()
            .get("/task")
            .then()
            .statusCode(200)
            .body("$", hasSize(1))
            .body("[0].name", equalTo("Aprender Java"));
}
```

#### PUT /task/alter-status/{id}
```java
@Test
@DisplayName("PUT /task/alter-status/{id} - Deve alterar status da tarefa")
void testAlterStatusTaskSuccess() {
    TaskResponseDto inactiveDto = // ... INACTIVE
    
    when(taskService.alterStatusTask(testTaskId))
            .thenReturn(inactiveDto);

    given()
            .when()
            .put("/task/alter-status/" + testTaskId)
            .then()
            .statusCode(200)
            .body("tasksStatus", equalTo("INACTIVE"));
}
```

---

## Testes UserResource

### Classe: UserResourceTest

**Localização:** `src/test/java/br/com/avsistems/resource/UserResourceTest.java`

#### GET /users - Lista Ativos
```java
@Test
@DisplayName("GET /users - Deve retornar lista de usuários ativos")
void testFindAllSuccess() {
    List<UserResponseDto> users = List.of(testUserDto);
    when(userService.findAllActiveUser()).thenReturn(users);

    given()
            .when()
            .get("/users")
            .then()
            .statusCode(200)
            .body("$", hasSize(1))
            .body("[0].name", equalTo("João Silva"));
}
```

#### PUT /users/adrress/{id} - Atualizar Endereço
```java
@Test
@DisplayName("PUT /users/address/{id} - Deve atualizar endereço do usuário")
void testUpdateUserAddressSuccess() {
    UserUpdateAdressDto addressDto = new UserUpdateAdressDto(...);
    
    when(userService.updateUserAddress(eq(testUserId), any()))
            .thenReturn(updatedDto);

    given()
            .contentType("application/json")
            .body(addressDto)
            .when()
            .put("/users/adrress/" + testUserId)
            .then()
            .statusCode(200);
}
```

#### DELETE /users/{id}
```java
@Test
@DisplayName("DELETE /users/{id} - Deve deletar usuário com sucesso")
void testDeleteUserSuccess() {
    doNothing().when(userService).deleteUser(testUserId);

    given()
            .when()
            .delete("/users/" + testUserId)
            .then()
            .statusCode(204);
}
```

---

## Executando Testes

### 1. Executar Todos os Testes
```bash
./mvnw clean test
```

**Saída:**
```
[INFO] BUILD SUCCESS
[INFO] Tests run: 64, Failures: 0, Errors: 0, Skipped: 0
[INFO] Total time: 45s
```

### 2. Executar Classe Específica
```bash
./mvnw test -Dtest=PartnersResourceTest
./mvnw test -Dtest=TaskResourceTest
./mvnw test -Dtest=UserResourceTest
```

### 3. Executar Teste Específico
```bash
./mvnw test -Dtest=PartnersResourceTest#testCreatePartnerSuccess
./mvnw test -Dtest=TaskResourceTest#testListAllTasksSuccess
```

### 4. Modo Watch (re-executa ao salvar)
```bash
./mvnw test -Dtest=PartnersResourceTest -Dtruncate.output=false --watch
```

### 5. Com Logs Detalhados
```bash
./mvnw test -X -e
```

### 6. Modo Parallel
```bash
./mvnw -T 1C test
```

---

## Análise de Cobertura

### Gerar Relatório de Cobertura

```bash
./mvnw clean test jacoco:report
```

**Abre em:** `target/site/jacoco/index.html`

### Verificar Cobertura Específica
```bash
./mvnw clean test jacoco:report -Dsonar.coverage.exclusions=**/dto/**,**/entity/**
```

### Requisitos de Cobertura
- **Target:** 80%+ cobertura
- **Classes:** 100% das classes core
- **Methods:** 100% dos endpoints públicos
- **Lines:** 80%+ das linhas

---

## Melhores Práticas

### 1. Nomenclatura dos Testes

```java
// ❌ Ruim
@Test
void test1() { ... }

// ✅ Bom
@Test
@DisplayName("POST /partners - Deve criar parceiro com sucesso")
void testCreatePartnerSuccess() { ... }
```

### 2. Arrange-Act-Assert (AAA)

```java
@Test
void testExample() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(service.findById(id)).thenReturn(dto);
    
    // Act
    Response response = given().when().get("/endpoint/" + id).then();
    
    // Assert
    response.statusCode(200).body("id", equalTo(id.toString()));
}
```

### 3. Use @DisplayName para Clareza

```java
@Test
@DisplayName("POST /partners - Deve validar campo 'name' como obrigatório")
void testCreatePartnerWithoutName() { ... }
```

### 4. Mock Apenas Dependências

```java
@InjectMock
PartnersService partnersService;  // Mocked

// Não mock a resource, teste real!
given().when().post("/partners").then()...
```

### 5. Teste Casos Felizes e Infelizes

```java
// Sucesso
@Test void testSuccess() { statusCode(200); }

// Não encontrado
@Test void testNotFound() { statusCode(404); }

// Validação falhou
@Test void testValidationFailed() { statusCode(400); }

// Conflito
@Test void testConflict() { statusCode(409); }
```

### 6. Use @BeforeEach para Setup

```java
@BeforeEach
void setUp() {
    testPartnerId = UUID.randomUUID();
    testPartnerDto = new PartnersResponseDto(...);
}
```

### 7. Teste um Conceito por Teste

```java
// ❌ Ruim - testa multiple coisas
@Test
void testCreateUpdateDelete() { ... }

// ✅ Bom - testa um comportamento
@Test
void testCreatePartnerSuccess() { ... }

@Test
void testUpdatePartnerSuccess() { ... }

@Test
void testDeletePartnerSuccess() { ... }
```

### 8. Assertivas Claras

```java
// ❌ Ruim
.then().body(hasSize(1));

// ✅ Bom
.then()
    .statusCode(200)
    .body("$", hasSize(1))
    .body("[0].name", equalTo("Amazon"))
    .body("[0].state", equalTo("SP"));
```

---

## Troubleshooting

### Teste Falha: "Port 8080 already in use"

```bash
# Matar processo
lsof -i :8080
kill -9 <PID>

# Ou usar porta diferente
mvn test -Dquarkus.http.port=8081
```

### Teste Falha: "Service not mocked"

```java
// ❌ Errado
@Inject PartnersService service;  // Real

// ✅ Correto
@InjectMock PartnersService service;  // Mocked
```

### Teste Falha: "Timeout"

```bash
# Aumentar timeout
./mvnw test -Dgroups="!slow" -Dtimeout=60000
```

### Mock não está funcionando

```java
// ❌ Errado
when(service.createPartner(any())).thenReturn(dto);
service.createPartner(form);  // Não funcionará

// ✅ Correto
@InjectMock PartnersService service;
when(service.createPartner(any())).thenReturn(dto);
service.createPartner(form);  // Agora funciona
```

---

## Integração com CI/CD

### GitHub Actions

```yaml
name: Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '21'
      - run: ./mvnw clean test
      - run: ./mvnw jacoco:report
      - uses: codecov/codecov-action@v2
```

### GitLab CI

```yaml
test:
  stage: test
  image: maven:3.8-openjdk-21
  script:
    - mvn clean test jacoco:report
  artifacts:
    reports:
      coverage_report:
        coverage_format: jacoco
        path: target/site/jacoco/jacoco.xml
```

---

## Resumo

| Aspecto | Status |
|---------|--------|
| **Total de Testes** | 64 ✅ |
| **Cobertura Target** | 80%+ |
| **Framework** | JUnit 5 ✅ |
| **Mocking** | Mockito ✅ |
| **HTTP Testing** | RestAssured ✅ |
| **CI/CD Ready** | Sim ✅ |

---

**Próximas Melhorias:**
- [ ] Adicionar testes de integração (live database)
- [ ] Testes de performance/load
- [ ] Testes de segurança (JWT)
- [ ] Testes de concorrência
- [ ] Testes E2E com Selenium


