package org.ivoligo.task_management_system.service;

import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.dto.TaskDto;

import java.util.List;

public interface TaskService {

    Long createTask(TaskDto task);

    List<TaskDto> getTasks(FilterSortDto filterSort);

    TaskDto getTask(Long id);

    boolean updateTask(TaskDto task);

    boolean deleteTask(Long id);
}
