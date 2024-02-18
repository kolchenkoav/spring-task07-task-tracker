package com.example.webfluxexample.mapper;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.handler.UserSubscriber;
import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class TaskMapperDelegate implements TaskMapper {
    private final UserRepository userRepository;

//    public TaskMapperDelegate(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    public Task toEntity(TaskModel taskModel) {
        Task task = new Task();
        task.setId(taskModel.getId());
        task.setName(taskModel.getName());
        task.setDescription(taskModel.getDescription());

        return task;
    }

    @Override
    public TaskModel toModel(Task task) {
        System.out.println("@@@@@@@@@@@@@@");
        TaskModel taskModel = new TaskModel();

        taskModel.setId(task.getId());
        taskModel.setName(task.getName());
        taskModel.setDescription(task.getDescription());

        if (task.getAuthorId() != null) {
            taskModel.setAuthorId(task.getAuthorId());

            Mono<User> userMono = userRepository.findById(task.getAuthorId());

            UserSubscriber<User> userUserSubscriber = new UserSubscriber<>();
            userMono.subscribe(userUserSubscriber);

//            User author = userUserSubscriber.getUser();
//            taskModel.setAuthor(author);
        } else {
            System.out.println("*************");
        }
        return taskModel;
    }
}
