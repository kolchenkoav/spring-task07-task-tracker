package com.example.webfluxexample.mapper;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.model.TaskModel;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;


//@Mapper(componentModel = "spring")
//@Mapper
//@MapperConfig(mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {TaskModel.class, Task.class})
public interface TaskListMapper {
    Task taskToEntity(TaskModel taskModel);
    default List<Task> toEntityList(List<TaskModel> taskModelList) {
        return new ArrayList<>(taskModelList.stream()
                .map(this::taskToEntity).toList());
    }

    TaskModel entityToTask(Task task);
    default List<TaskModel> toModelList(List<Task> taskList) {
        return new ArrayList<>(taskList.stream()
                .map(this::entityToTask).toList());
    }
}
