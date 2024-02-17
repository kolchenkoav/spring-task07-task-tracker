package com.example.webfluxexample.service;

import com.example.webfluxexample.mapper.TaskMapper;
import com.example.webfluxexample.model.TaskModel;
import com.example.webfluxexample.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public Flux<TaskModel> findAll() {
        return taskRepository.findAll().map(taskMapper::toModel);
    }
}
