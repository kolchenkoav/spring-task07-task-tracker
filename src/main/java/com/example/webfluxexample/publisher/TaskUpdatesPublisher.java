package com.example.webfluxexample.publisher;

import com.example.webfluxexample.model.TaskModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class TaskUpdatesPublisher {
    private final Sinks.Many<TaskModel> taskModelUpdatesSink;

    public TaskUpdatesPublisher() {
        this.taskModelUpdatesSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(TaskModel taskModel) {
        taskModelUpdatesSink.tryEmitNext(taskModel);
    }

    public Sinks.Many<TaskModel> getUpdatesSink() {
        return taskModelUpdatesSink;
    }
}
