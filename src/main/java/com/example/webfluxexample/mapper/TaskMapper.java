package com.example.webfluxexample.mapper;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.model.TaskModel;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Интерфейс для преобразования между моделями задач и сущностями.
 */
@DecoratedWith(TaskMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {TaskModel.class, Task.class})
public interface TaskMapper {

    /**
     * Преобразует модель задачи в сущность задачи.
     *
     * @param taskModel модель задачи
     * @return сущность задачи
     */
    Task toEntity(TaskModel taskModel);

    /**
     * Преобразует сущность задачи в модель задачи.
     *
     * @param task сущность задачи
     * @return модель задачи
     */
    TaskModel toModel(Task task);

    /**
     * Преобразует список моделей задач в список сущностей задач.
     *
     * @param taskModelList список моделей задач
     * @return список сущностей задач
     */
    List<Task> toEntityList(List<TaskModel> taskModelList);

    /**
     * Преобразует список сущностей задач в список моделей задач.
     *
     * @param taskList список сущностей задач
     * @return список моделей задач
     */
    List<TaskModel> toModelList(List<Task> taskList);
}
