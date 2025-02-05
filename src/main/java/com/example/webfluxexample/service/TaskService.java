package com.example.webfluxexample.service;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.mapper.TaskMapper;
import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.repository.TaskRepository;
import com.example.webfluxexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

import java.time.Instant;
import java.util.UUID;

/**
 * Сервис для управления задачами.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;

    /**
     * Находит все задачи.
     *
     * @return Поток задач.
     */
    public Flux<TaskModel> findAll() {
        Flux<Task> taskFlux = taskRepository.findAll();
        return taskFlux
            .flatMap(task -> getTaskModelMono(task.getId()));
    }

    /**
     * Получает модель задачи по идентификатору.
     *
     * @param id Идентификатор задачи.
     * @return Монада модели задачи.
     */
    private Mono<TaskModel> getTaskModelMono(String id) {
        Mono<Task> taskMono = taskRepository.findById(id);

        Mono<User> authorMono = userRepository.findById(taskMono.map(Task::getAuthorId))
            .onErrorResume(e -> Mono.just(new User()));
        Mono<User> assigneeMono = userRepository.findById(taskMono.map(Task::getAssigneeId))
            .onErrorResume(e -> Mono.just(new User()));

        Mono<TaskModel> taskModelMono = taskMono.map(taskMapper::toModel);

        taskModelMono = taskModelMono
            .zipWhen(task -> authorMono, Tuples::of)
            .publishOn(Schedulers.boundedElastic())
            .zipWhen(tuple -> assigneeMono, (tuple, assignee) -> {
                TaskModel taskModel = tuple.getT1();
                User author = tuple.getT2();
                if (taskModel.getAuthorId() != null) {
                    taskModel.setAuthor(author);
                }
                if (taskModel.getAssigneeId() != null) {
                    taskModel.setAssignee(assignee);
                }

                return taskModel;
            });

        return taskModelMono;
    }

    /**
     * Находит задачу по идентификатору.
     *
     * @param id Идентификатор задачи.
     * @return Монада ответа с моделью задачи.
     */
    public Mono<ResponseEntity<TaskModel>> findById(String id) {
        return getTaskModelMono(id)
            .map(ResponseEntity::ok).log()
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Сохраняет новую задачу.
     *
     * @param taskModel Модель задачи.
     * @return Монада ответа с моделью задачи.
     */
    public Mono<ResponseEntity<TaskModel>> save(TaskModel taskModel) {
        Task task = taskMapper.toEntity(taskModel);
        task.setId(UUID.randomUUID().toString());
        Instant instant = Instant.now();
        task.setCreatedAt(instant);
        task.setUpdatedAt(instant);

        log.info("save task {}", taskModel);
        Mono<Task> taskMono = taskRepository.save(task);
        return taskMono.map(taskMapper::toModel).cast(TaskModel.class)
            .map(ResponseEntity::ok);
    }

    /**
     * Обновляет существующую задачу.
     *
     * @param id Идентификатор задачи.
     * @param taskModel Модель задачи.
     * @return Монада ответа с моделью задачи.
     */
    public Mono<ResponseEntity<TaskModel>> update(String id, TaskModel taskModel) {
        taskModel.setId(id);

        return taskRepository.findById(id).flatMap(taskForUpdate -> {
            taskForUpdate.setId(id);
            taskForUpdate.setUpdatedAt(Instant.now());

            if (StringUtils.hasText(taskModel.getName())) {
                taskForUpdate.setName(taskModel.getName());
            }
            if (StringUtils.hasText(taskModel.getDescription())) {
                taskForUpdate.setDescription(taskModel.getDescription());
            }
            if (taskModel.getStatus() != null) {
                taskForUpdate.setStatus(taskModel.getStatus());
            }

            if (taskModel.getAuthorId() != null) {
                taskForUpdate.setAuthorId(taskModel.getAuthorId());
            }
            if (taskModel.getAssigneeId() != null) {
                taskForUpdate.setAssigneeId(taskModel.getAssigneeId());
            }
            if (taskModel.getObserverIds() != null) {
                taskForUpdate.setObserverIds(taskModel.getObserverIds());
            }
            log.info("UPDATE id:{} taskModel:{}", id, taskForUpdate);
            return taskRepository.save(taskForUpdate).map(taskMapper::toModel)
                .map(ResponseEntity::ok).log()
                .defaultIfEmpty(ResponseEntity.notFound().build());
        });
    }

    /**
     * Удаляет задачу по идентификатору.
     *
     * @param id Идентификатор задачи.
     * @return Монада ответа.
     */
    public Mono<ResponseEntity<Void>> deleteById(String id) {
        return taskRepository.deleteById(id).log()
            .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
