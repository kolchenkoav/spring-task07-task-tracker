# WebFlux Example

## О проекте

Этот проект представляет собой веб-приложение на основе **Spring WebFlux**, которое использует **MongoDB** в качестве базы данных. Приложение предоставляет REST API для управления пользователями и задачами. Основной стек технологий включает **Spring Boot**, **Reactive Streams**, **Project Reactor** и **Spring Data MongoDB**.

## Требования

Перед началом работы убедитесь, что у вас установлены следующие компоненты:

- **JDK 17** или выше
- **Docker и Docker Compose** (для запуска MongoDB)
- **Maven 3.8+**

## Зависимости

Проект использует следующие ключевые зависимости:

- `spring-boot-starter-webflux` - для реактивного программирования
- `spring-boot-starter-data-mongodb` - для работы с MongoDB
- `lombok` - для сокращения шаблонного кода
- `mapstruct` - для преобразования объектов
- `reactor-test` - для тестирования реактивных потоков
- `testcontainers` - для изолированного тестирования MongoDB

Полный список зависимостей можно найти в `pom.xml`.

## Начало работы

### Установка зависимостей

```sh
mvn clean install
```

### Запуск MongoDB с помощью Docker

```sh
docker-compose up -d
```

MongoDB будет доступна на `localhost:27017` с учетными данными:
- **Пользователь**: `root`
- **Пароль**: `root`
- **База данных**: `appdatabase`

## Как запустить приложение

### Запуск через Maven

```sh
mvn spring-boot:run
```

### Запуск через исполняемый JAR

```sh
mvn package
java -jar target/webflux-example.jar
```

Приложение запустится на `http://localhost:8080`.

## API эндпоинты

### Управление задачами (`/api/v1/tasks`)

- **GET /api/v1/tasks** - получить список всех задач
- **GET /api/v1/tasks/{id}** - получить задачу по ID
- **POST /api/v1/tasks** - создать новую задачу
- **PUT /api/v1/tasks/{id}** - обновить существующую задачу
- **DELETE /api/v1/tasks/{id}** - удалить задачу

Пример создания задачи:

```json
{
  "name": "Новая задача",
  "description": "Описание задачи",
  "status": "TODO",
  "authorId": "user1",
  "assigneeId": "user2"
}
```

### Управление пользователями (`/api/v1/users`)

- **GET /api/v1/users** - получить список всех пользователей
- **GET /api/v1/users/{id}** - получить пользователя по ID
- **GET /api/v1/users/by-name?name={username}** - найти пользователя по имени
- **POST /api/v1/users** - создать нового пользователя
- **PUT /api/v1/users/{id}** - обновить существующего пользователя
- **DELETE /api/v1/users/{id}** - удалить пользователя

Пример создания пользователя:

```json
{
  "username": "new_user",
  "email": "user@example.com"
}
```

## Примеры кода

### Запрос всех задач с использованием WebClient

```java
WebClient webClient = WebClient.create("http://localhost:8080");

Flux<TaskModel> tasks = webClient.get()
    .uri("/api/v1/tasks")
    .retrieve()
    .bodyToFlux(TaskModel.class);

tasks.subscribe(System.out::println);
```

### Тестирование REST API с WebTestClient

```java
@Test
void testGetAllTasks() {
    webTestClient.get().uri("/api/v1/tasks")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(TaskModel.class)
        .hasSize(2);
}
```

## Заключение

Этот проект демонстрирует работу с **Spring WebFlux** и **MongoDB**, реализуя API для управления задачами и пользователями.
Будем рады вашим вопросам и предложениям!

