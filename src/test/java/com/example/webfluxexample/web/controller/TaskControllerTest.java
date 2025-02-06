package com.example.webfluxexample.web.controller;

import com.example.webfluxexample.AbstractTest;
import com.example.webfluxexample.entity.TaskStatus;
import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.model.TaskModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskControllerTest extends AbstractTest {

    @Test
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
                        new User(AUTHOR_USER_ID, "Author_user", "Author_user@some.com"),
                        new User(ASSIGNEE_USER_ID, "Assignee_user", "Assignee_user@some.com"),
                        Set.of(
                                new User(FIRST_USER_ID, "First_user", "First_user@some.com"),
                                new User(SECOND_USER_ID, "Second_user", "Second_user@some.com")
                        )
                ),
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
                        new User(AUTHOR_USER_ID, "Author_user", "Author_user@some.com"),
                        new User(ASSIGNEE_USER_ID, "Assignee_user", "Assignee_user@some.com"),
                        Set.of(
                                new User(THIRD_USER_ID, "Third_user", "Third_user@some.com"),
                                new User(FOURTH_USER_ID, "Fourth_user", "Fourth_user@some.com")
                        )
                )
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
                        assertEquals(expected.getAuthor(), actual.getAuthor());
                        assertEquals(expected.getAssignee(), actual.getAssignee());
                        assertEquals(expected.getObservers(), actual.getObservers());
                    }
                });
    }
}
