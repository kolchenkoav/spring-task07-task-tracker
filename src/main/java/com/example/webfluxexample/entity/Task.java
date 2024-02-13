package com.example.webfluxexample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private TaskStatus status;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;

    @ReadOnlyProperty
    private User author;
    @ReadOnlyProperty
    private User assignee;
    @ReadOnlyProperty
    private Set<User> observers;
}
