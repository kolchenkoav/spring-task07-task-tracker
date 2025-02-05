package com.example.webfluxexample.controller;

import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Контроллер для управления задачами.
 */
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    /**
     * Получает все задачи.
     *
     * @return Flux<TaskModel> список всех задач.
     */
    @GetMapping
    public Flux<TaskModel> getAllTasks() {
        return taskService.findAll();
    }

    /**
     * Получает задачу по идентификатору.
     *
     * @param id идентификатор задачи.
     * @return Mono<ResponseEntity<TaskModel>> задача.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> getTaskById(@PathVariable String id) {
        return taskService.findById(id);
    }

    /**
     * Создает новую задачу.
     *
     * @param taskModel модель задачи.
     * @return Mono<ResponseEntity<TaskModel>> созданная задача.
     */
    @PostMapping
    public Mono<ResponseEntity<TaskModel>> createTask(@RequestBody TaskModel taskModel) {
        return taskService.save(taskModel).log();
    }

    /**
     * Обновляет существующую задачу.
     *
     * @param id идентификатор задачи.
     * @param taskModel модель задачи.
     * @return Mono<ResponseEntity<TaskModel>> обновленная задача.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> updateTask(@PathVariable String id,
                                                      @RequestBody TaskModel taskModel) {
        return taskService.update(id, taskModel);
    }

    /**
     * Удаляет задачу по идентификатору.
     *
     * @param id идентификатор задачи.
     * @return Mono<ResponseEntity<Void>> результат удаления.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String id) {
        return taskService.deleteById(id).log();
    }
}
