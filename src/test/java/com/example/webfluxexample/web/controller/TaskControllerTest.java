package com.example.webfluxexample.web.controller;

import com.example.webfluxexample.AbstractTest;
import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.TaskStatus;
import com.example.webfluxexample.model.TaskModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskControllerTest extends AbstractTest {

//    @Test
//    @DisplayName("When get all tasks, then return list of tasks from database")
//    public void whenGetAllTasksThenReturnListOfTasksFromDatabase() {
//        var expectedData = List.of(new TaskModel(
//                        FIRST_ITEM_ID,
//                        "Task 1",
//                        "Description 1",
//                        CREATED_AT,
//                        UPDATED_AT,
//                        TaskStatus.TODO,
//                        "author1",
//                        "assignee1",
//                        Set.of(FIRST_USER_ID, SECOND_USER_ID),
//                        null,
//                        null,
//                        null
//                ),
//                new TaskModel(
//                        SECOND_ITEM_ID,
//                        "Task 2",
//                        "Description 2",
//                        CREATED_AT.plusSeconds(3000),
//                        UPDATED_AT.plusSeconds(4000),
//                        TaskStatus.IN_PROGRESS,
//                        "author2",
//                        "assignee2",
//                        Set.of(THIRD_USER_ID, FOURTH_USER_ID),
//                        null,
//                        null,
//                        null
//                )
//        );
//
//
//        webTestClient.get().uri("/api/v1/tasks")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(TaskModel.class)
//                .hasSize(0)
//                .contains(expectedData.toArray(TaskModel[]::new));
//    }
}
