package com.example.webfluxexample.mapper;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

public abstract class TaskMapperDelegate implements TaskMapper {
    @Autowired
    private UserRepository userRepository;

    public TaskMapperDelegate(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Task toEntity(TaskModel taskModel) {
        Task task = new Task();
        task.setId(taskModel.getId());
        task.setName(taskModel.getName());
        task.setDescription(taskModel.getDescription());
        User author = userRepository.findById(taskModel.getAuthor().getId()).log().block();
        //System.out.println(author.getUsername());
        System.out.println("+++++++++++++++++++++");
        return task;
    }

    @Override
    public TaskModel toModel(Task task) {
        TaskModel taskModel = new TaskModel();
        taskModel.setId(task.getId());
        taskModel.setName(task.getName());
        taskModel.setDescription(task.getDescription());
        Mono<User> userMono = userRepository.findById(taskModel.getAuthor().getId());
        //User user = new User();
        User author = userMono.block();
        System.out.println("*************"+author.getUsername());
        taskModel.setAuthor(author);
        return taskModel;
    }
}
