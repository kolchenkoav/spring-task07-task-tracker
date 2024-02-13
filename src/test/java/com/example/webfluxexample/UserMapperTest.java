package com.example.webfluxexample;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.mapper.TaskListMapper;
import com.example.webfluxexample.mapper.UserMapper;
import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
//@RequiredArgsConstructor
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskListMapper taskListMapper;

    @Test
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

    @Test
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

//    private String id;
//    private String name;
//    private String description;
//    private Instant createdAt;
//    private Instant updatedAt;
//    private TaskStatus status;
//    private String authorId;
//    private String assigneeId;
//    private Set<String> observerIds;
    @Test
    void shouldProperlyMapTaskModelListToTaskEntityList() {
        Instant dateNow = Instant.now();
        List<TaskModel> taskModelList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TaskModel taskModel = new TaskModel();
            taskModel.setId("id " + i);
            taskModel.setName("Task " + i);
            taskModel.setDescription("Description " + i);
            taskModel.setCreatedAt(dateNow);
            taskModel.setUpdatedAt(dateNow);
            taskModel.setAuthorId("AuthorId " + i);
            taskModel.setAssigneeId("AssigneeId " + i);
            taskModelList.add(taskModel);
        }

        List<Task> taskList = taskListMapper.toEntityList(taskModelList);
        assertNotNull(taskList);
        for (int i = 0; i < 3; i++) {
            assertEquals(taskModelList.get(i).getId(), taskList.get(i).getId());
            assertEquals(taskModelList.get(i).getName(), taskList.get(i).getName());
            assertEquals(taskModelList.get(i).getDescription(), taskList.get(i).getDescription());
            assertEquals(taskModelList.get(i).getCreatedAt(), taskList.get(i).getCreatedAt());
            assertEquals(taskModelList.get(i).getUpdatedAt(), taskList.get(i).getUpdatedAt());
            assertEquals(taskModelList.get(i).getAuthorId(), taskList.get(i).getAuthorId());
            assertEquals(taskModelList.get(i).getAssigneeId(), taskList.get(i).getAssigneeId());
        }
    }
}
