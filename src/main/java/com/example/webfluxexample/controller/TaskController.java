package com.example.webfluxexample.controller;

import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.model.UserModel;
import com.example.webfluxexample.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public Flux<TaskModel> getAllItems() {
        return taskService.findAll();
    }
}
