package com.example.webfluxexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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


}
