package com.example.webfluxexample.service;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.mapper.TaskMapper;
import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.model.UserModel;
import com.example.webfluxexample.publisher.TaskUpdatesPublisher;
import com.example.webfluxexample.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskUpdatesPublisher taskUpdatesPublisher;

    public Flux<TaskModel> findAll() {
        return taskRepository.findAll().map(taskMapper::toModel);
    }

    public Mono<ResponseEntity<TaskModel>> findById(String id) {
        return taskRepository.findById(id)
                .map(taskMapper::toModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<ResponseEntity<TaskModel>> save(TaskModel taskModel) {
        taskModel.setId(UUID.randomUUID().toString());
        Mono<Task> taskMono = taskRepository.save(taskMapper.toEntity(taskModel));

        return taskMono.map(taskMapper::toModel).cast(TaskModel.class)
                .doOnSuccess(taskUpdatesPublisher::publish)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<Void>> deleteById(String id) {
        return taskRepository.deleteById(id).log()
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
