package com.example.webfluxexample;

import com.example.webfluxexample.entity.*;
import com.example.webfluxexample.repository.TaskRepository;
import com.example.webfluxexample.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Абстрактный класс для тестирования, использующий Testcontainers для MongoDB.
 */
@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
public abstract class AbstractTest {

    protected static String FIRST_TASK_ID = UUID.randomUUID().toString();
    protected static String SECOND_TASK_ID = UUID.randomUUID().toString();

    protected static String FIRST_USER_ID = UUID.randomUUID().toString();
    protected static String SECOND_USER_ID = UUID.randomUUID().toString();
    protected static String THIRD_USER_ID = UUID.randomUUID().toString();
    protected static String FOURTH_USER_ID = UUID.randomUUID().toString();
    protected static String AUTHOR_USER_ID = UUID.randomUUID().toString();
    protected static String ASSIGNEE_USER_ID = UUID.randomUUID().toString();

    protected static Instant CREATED_AT = Instant.now();
    protected static Instant UPDATED_AT = Instant.now().plusSeconds(1000);
    /**
     * Контейнер MongoDB для тестирования.
     */
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.8")
            .withReuse(true);

    /**
     * Настройка динамических свойств для подключения к MongoDB.
     *
     * @param registry Регистр динамических свойств.
     */
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.saveAll(List.of(
                new User(FIRST_USER_ID,
                        "First_user",
                        "First_user@some.com",
                        "123",
                        Set.of(Role.from(RoleType.ROLE_USER))
                ),
                new User(SECOND_USER_ID,
                        "Second_user",
                        "Second_user@some.com",
                        "123",
                        Set.of(Role.from(RoleType.ROLE_USER))
                ),
                new User(THIRD_USER_ID,
                        "Third_user",
                        "Third_user@some.com",
                        "123",
                        Set.of(Role.from(RoleType.ROLE_USER))
                ),
                new User(FOURTH_USER_ID,
                        "Fourth_user",
                        "Fourth_user@some.com",
                        "123",
                        Set.of(Role.from(RoleType.ROLE_USER))
                ),
                new User(AUTHOR_USER_ID,
                        "Author_user",
                        "Author_user@some.com",
                        "123",
                        Set.of(Role.from(RoleType.ROLE_USER))
                ),
                new User(ASSIGNEE_USER_ID,
                        "Assignee_user",
                        "Assignee_user@some.com",
                        "123",
                        Set.of(Role.from(RoleType.ROLE_USER))
                )
        )).collectList().block();

        taskRepository.saveAll(List.of(
                new Task(
                        FIRST_TASK_ID,
                        "Task 1",
                        "Description 1",
                        CREATED_AT,
                        UPDATED_AT,
                        TaskStatus.TODO,
                        AUTHOR_USER_ID,
                        ASSIGNEE_USER_ID,
                        Set.of(FIRST_USER_ID, SECOND_USER_ID),
                        null,
                        null,
                        null
                ),
                new Task(
                        SECOND_TASK_ID,
                        "Task 2",
                        "Description 2",
                        CREATED_AT,
                        UPDATED_AT,
                        TaskStatus.IN_PROGRESS,
                        AUTHOR_USER_ID,
                        ASSIGNEE_USER_ID,
                        Set.of(THIRD_USER_ID, FOURTH_USER_ID),
                        null,
                        null,
                        null
                )
        )).collectList().block();
    }

    /**
     * Очистка данных после каждого теста.
     */
    @AfterEach
    public void afterEach() {
        taskRepository.deleteAll().block();
        userRepository.deleteAll().block();
    }
}
