package com.example.webfluxexample;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.TaskStatus;
import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.repository.TaskRepository;
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

    protected static String FIRST_ITEM_ID = UUID.randomUUID().toString();
    protected static String SECOND_ITEM_ID = UUID.randomUUID().toString();
    protected static String FIRST_USER_ID = UUID.randomUUID().toString();
    protected static String SECOND_USER_ID = UUID.randomUUID().toString();
    protected static String THIRD_USER_ID = UUID.randomUUID().toString();
    protected static String FOURTH_USER_ID = UUID.randomUUID().toString();

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

    @BeforeEach
    public void setup() {
        taskRepository.saveAll(List.of(
                new Task(
                        FIRST_ITEM_ID,
                        "Task 1",
                        "Description 1",
                        CREATED_AT,
                        UPDATED_AT,
                        TaskStatus.TODO,
                        "author1",
                        "assignee1",
                        Set.of(FIRST_USER_ID, SECOND_USER_ID),
                        null,
                        null,
                        null
                ),
                new Task(
                        SECOND_ITEM_ID,
                        "Task 2",
                        "Description 2",
                        CREATED_AT.plusSeconds(3000),
                        UPDATED_AT.plusSeconds(4000),
                        TaskStatus.IN_PROGRESS,
                        "author2",
                        "assignee2",
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
    }
}
