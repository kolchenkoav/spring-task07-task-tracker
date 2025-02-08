package com.example.webfluxexample;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.mapper.TaskMapper;
import com.example.webfluxexample.mapper.UserMapper;
import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.model.UserModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * UserMapperTest - тестовый класс для проверки корректности работы маппера UserMapper.
 */
@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskMapper taskMapper;

    /**
     * Проверяет правильность преобразования UserModel в User.
     */
    @Test
    @DisplayName("should properly map ModelToEntity")
    void shouldProperlyMapModelToEntity() {
        UserModel userModel = new UserModel();
        userModel.setId("1");
        userModel.setUsername("User 1");
        userModel.setEmail("mail@mail.ru");

        User user = UserMapper.INSTANCE.toEntity(userModel);

        assertNotNull(user);
        assertEquals(userModel.getId(), user.getId());
        assertEquals(userModel.getUsername(), user.getUsername());
        assertEquals(userModel.getEmail(), user.getEmail());
    }

    /**
     * Проверяет правильность преобразования User в UserModel.
     */
    @Test
    @DisplayName("should properly map EntityToModel")
    void shouldProperlyMapEntityToModel() {
        User user = new User();
        user.setId("1");
        user.setUsername("User 1");
        user.setEmail("mail@mail.ru");

        UserModel userModel = userMapper.toModel(user);

        assertNotNull(userModel);
        assertEquals(userModel.getId(), user.getId());
        assertEquals(userModel.getUsername(), user.getUsername());
        assertEquals(userModel.getEmail(), user.getEmail());
    }

    /**
     * Проверяет правильность преобразования списка TaskModel в список Task.
     */
    @Test
    @DisplayName("should properly map TaskModelListToTaskEntityList")
    void shouldProperlyMapTaskModelListToTaskEntityList() {
        List<TaskModel> taskModelList = new ArrayList<>();
        Instant now = Instant.now();

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setId("1");
        taskModel1.setName("Task 1");
        taskModel1.setDescription("Description 1");
        taskModel1.setCreatedAt(now);

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setId("2");
        taskModel2.setName("Task 2");
        taskModel2.setDescription("Description 2");
        taskModel2.setCreatedAt(now);

        taskModelList.add(taskModel1);
        taskModelList.add(taskModel2);

        List<Task> taskList = taskMapper.toEntityList(taskModelList);

        assertNotNull(taskList);
        assertEquals(taskModelList.size(), taskList.size());

        for (int i = 0; i < taskModelList.size(); i++) {
            TaskModel taskModel = taskModelList.get(i);
            Task task = taskList.get(i);

            assertEquals(taskModel.getId(), task.getId());
            assertEquals(taskModel.getName(), task.getName());
            assertEquals(taskModel.getDescription(), task.getDescription());
            assertEquals(taskModel.getCreatedAt(), task.getCreatedAt());
        }
    }
}
