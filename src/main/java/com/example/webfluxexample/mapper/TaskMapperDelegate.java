package com.example.webfluxexample.mapper;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

/**
 * Абстрактный класс для преобразования объектов Task и TaskModel.
 */
@Slf4j
public abstract class TaskMapperDelegate implements TaskMapper {
    @Autowired
    private UserRepository userRepository;

    /**
     * Преобразует TaskModel в Task.
     *
     * @param taskModel объект TaskModel для преобразования.
     * @return объект Task.
     */
    @Override
    public Task toEntity(TaskModel taskModel) {
        Task task = new Task();
        task.setId(taskModel.getId());
        task.setName(taskModel.getName());
        task.setDescription(taskModel.getDescription());
        task.setStatus(taskModel.getStatus());
        task.setAuthorId(taskModel.getAuthorId());
        task.setAssigneeId(taskModel.getAssigneeId());
        if (taskModel.getObserverIds() != null) {
            task.setObserverIds(taskModel.getObserverIds());
        }
        return task;
    }

    /**
     * Преобразует Task в TaskModel.
     *
     * @param task объект Task для преобразования.
     * @return объект TaskModel.
     */
    @Override
    public TaskModel toModel(Task task) {
        TaskModel taskModel = new TaskModel();
        taskModel.setId(task.getId());
        taskModel.setName(task.getName());
        taskModel.setDescription(task.getDescription());
        taskModel.setStatus(task.getStatus());
        taskModel.setCreatedAt(task.getCreatedAt());
        taskModel.setUpdatedAt(task.getUpdatedAt());
        taskModel.setAuthorId(task.getAuthorId());
        taskModel.setAssigneeId(task.getAssigneeId());

        if (task.getObserverIds() != null) {
            taskModel.setObserverIds(task.getObserverIds());
            Set<User> userSet = new HashSet<>();

            Flux<User> userFlux = userRepository.findAllById(taskModel.getObserverIds())
                .doOnNext(user -> {
                    userSet.add(user);
                    taskModel.setObservers(userSet);
                });
            userFlux.subscribe();

            Set<User> userSet1 = new HashSet<>();
            userFlux.toIterable().forEach(userSet1::add);
            taskModel.setObservers(userSet1);
        }
        return taskModel;
    }
}
