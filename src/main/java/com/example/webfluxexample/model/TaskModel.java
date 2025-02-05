package com.example.webfluxexample.model;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.TaskStatus;
import com.example.webfluxexample.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskModel {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private TaskStatus status;

    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;

    private User author;
    private User assignee;
    private Set<User> observers;

//    public TaskModel from(Task task) {
//        var model = new TaskModel();
//        model.setId(task.getId());
//        model.setName(task.getName());
//        model.setDescription(task.getDescription());
//        model.setCreatedAt(task.getCreatedAt());
//        model.setUpdatedAt(task.getUpdatedAt());
//        model.setStatus(task.getStatus());
//        model.setAuthorId(task.getAuthorId());
//        model.setAssigneeId(task.getAssigneeId());
//
//        if (task.getObserverIds() != null) {
//            model.setObserverIds(new HashSet<>(task.getObserverIds()));
//        }
//        return model;
//    }
}
