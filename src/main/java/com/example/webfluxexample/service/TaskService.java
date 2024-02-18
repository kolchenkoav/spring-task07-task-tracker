package com.example.webfluxexample.service;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.handler.SampleSubscriber;
import com.example.webfluxexample.handler.TaskSubscriber;
import com.example.webfluxexample.mapper.TaskMapper;
import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.publisher.TaskUpdatesPublisher;
import com.example.webfluxexample.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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

        TaskSubscriber<Task> ss2 = new TaskSubscriber<>();
        Mono<Task> taskMono = taskRepository.findById(id);
        taskMono.subscribe(ss2);

        return taskRepository.findById(id)
                .map(taskMapper::toModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<ResponseEntity<TaskModel>> save(TaskModel taskModel) {
        Task task = taskMapper.toEntity(taskModel);
        task.setId(UUID.randomUUID().toString());
        Instant instant = Instant.now();
        task.setCreatedAt(instant);
        task.setUpdatedAt(instant);
        if (taskModel.getAuthor().getId() != null) {
            task.setAuthorId(taskModel.getAuthor().getId());
        }
        if (taskModel.getAssignee() != null) {
            task.setAssigneeId(taskModel.getAssignee().getId());
        }
        Mono<Task> taskMono = taskRepository.save(task).log();
        return taskMono.map(taskMapper::toModel).cast(TaskModel.class)
                .doOnSuccess(taskUpdatesPublisher::publish)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<TaskModel>> update(String id, TaskModel taskModel) {
        return taskRepository.findById(id).flatMap(taskForUpdate -> {
            Task task = taskMapper.toEntity(taskModel);

            if (StringUtils.hasText(task.getName())) {
                taskForUpdate.setName(task.getName());
                taskForUpdate.setDescription(task.getDescription());
                taskForUpdate.setStatus(task.getStatus());
            }

            return taskRepository.save(taskForUpdate).map(taskMapper::toModel)
                    .map(ResponseEntity::ok).log()
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        });
    }

    public Mono<ResponseEntity<Void>> deleteById(String id) {
        return taskRepository.deleteById(id).log()
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
