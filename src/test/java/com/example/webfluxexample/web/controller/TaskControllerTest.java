package com.example.webfluxexample.web.controller;

import com.example.webfluxexample.AbstractTest;
import com.example.webfluxexample.entity.TaskStatus;
import com.example.webfluxexample.model.TaskModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("TaskControllerTest")
public class TaskControllerTest extends AbstractTest {
    @Container
    static MongoDBContainer mongoDBContainer1 = new MongoDBContainer("mongo:6.0.8")
            .withReuse(true);
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer1::getReplicaSetUrl);
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    @DisplayName("When get all tasks, then return list of tasks from database")
    public void whenGetAllTasksThenReturnListOfTasksFromDatabase() {
        var expectedData = List.of(
                new TaskModel(
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
                        null),

                new TaskModel(
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
                        null)
        );

        // Выполняем запрос и проверяем ответ
        webTestClient.get().uri("/api/v1/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskModel.class)
                .hasSize(2)
                .consumeWith(response -> {
                    List<TaskModel> tasks = response.getResponseBody();
                    assert tasks != null;
                    assertEquals(expectedData.size(), tasks.size());
                    tasks.sort(Comparator.comparing(TaskModel::getName));
                    for (int i = 0; i < expectedData.size(); i++) {

                        TaskModel expected = expectedData.get(i);
                        TaskModel actual = tasks.get(i);

                        assertEquals(expected.getId(), actual.getId());
                        assertEquals(expected.getName(), actual.getName());
                        assertEquals(expected.getDescription(), actual.getDescription());
//                        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
//                        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
                        assertEquals(expected.getStatus(), actual.getStatus());
                        assertEquals(expected.getAuthorId(), actual.getAuthorId());
                        assertEquals(expected.getAssigneeId(), actual.getAssigneeId());
                        assertEquals(expected.getObserverIds(), actual.getObserverIds());
                        //assertEquals(expected.getAuthor(), actual.getAuthor());
                        //assertEquals(expected.getAssignee(), actual.getAssignee());
                        //assertEquals(expected.getObservers(), actual.getObservers());
                    }
                });
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    @DisplayName("When create task, then return created task")
    public void whenCreateTaskThenReturnCreatedTask() {
        TaskModel taskModel = new TaskModel();
        taskModel.setName("New Task");
        taskModel.setDescription("Description of new task");

        webTestClient.post().uri("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(taskModel), TaskModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskModel.class)
                .consumeWith(response -> {
                    TaskModel createdTask = response.getResponseBody();
                    assert createdTask != null;
                    assert createdTask.getName().equals("New Task");
                    assert createdTask.getDescription().equals("Description of new task");
                });
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    @DisplayName("When update task, then return updated task")
    public void whenUpdateTaskThenReturnUpdatedTask() {
        String taskId = FIRST_TASK_ID;
        TaskModel updatedTaskModel = new TaskModel();
        updatedTaskModel.setId(FIRST_TASK_ID);
        updatedTaskModel.setName("Updated Task");
        updatedTaskModel.setDescription("Updated description");

        webTestClient.put().uri("/api/v1/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedTaskModel), TaskModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskModel.class)
                .consumeWith(response -> {
                    TaskModel updatedTask = response.getResponseBody();
                    assert updatedTask != null;
                    assert updatedTask.getId().equals(taskId);
                    assert updatedTask.getName().equals("Updated Task");
                    assert updatedTask.getDescription().equals("Updated description");
                });
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    @DisplayName("When delete task, then return no content")
    public void whenDeleteTaskThenReturnNoContent() {
        String taskId = FIRST_TASK_ID;

        webTestClient.delete().uri("/api/v1/tasks/{id}", taskId)
                .exchange()
                .expectStatus().isNoContent();
    }
}
