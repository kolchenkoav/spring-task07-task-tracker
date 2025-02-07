package com.example.webfluxexample.controller;

import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Контроллер для управления задачами.
 */
@Slf4j
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
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    @GetMapping
    public Flux<TaskModel> getAllTasks(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("User: {}", userDetails.getUsername());
        log.info("Role: {}", userDetails.getAuthorities());
        return taskService.findAll();
    }

    /**
     * Получает задачу по идентификатору.
     *
     * @param id идентификатор задачи.
     * @return Mono<ResponseEntity < TaskModel>> задача.
     */
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> getTaskById(@PathVariable String id) {
        return taskService.findById(id);
    }

    /**
     * Создает новую задачу.
     *
     * @param taskModel модель задачи.
     * @return Mono<ResponseEntity < TaskModel>> созданная задача.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public Mono<ResponseEntity<TaskModel>> createTask(@RequestBody TaskModel taskModel) {
        return taskService.save(taskModel).log();
    }

    /**
     * Обновляет существующую задачу.
     *
     * @param id        идентификатор задачи.
     * @param taskModel модель задачи.
     * @return Mono<ResponseEntity < TaskModel>> обновленная задача.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> updateTask(@PathVariable String id,
                                                      @RequestBody TaskModel taskModel,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        return taskService.update(id, taskModel, userDetails);
    }

    /**
     * Удаляет задачу по идентификатору.
     *
     * @param id идентификатор задачи.
     * @return Mono<ResponseEntity < Void>> результат удаления.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String id) {
        return taskService.deleteById(id).log();
    }
}
