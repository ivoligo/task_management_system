package org.ivoligo.task_management_system.service;

import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    Long createTask(TaskDto task);

    Page<TaskDto> getTasks(FilterSortDto filterSort, Pageable pageable);

    List<TaskDto> getTasks(FilterSortDto filterSort);

    TaskDto getTask(Long id);

    boolean updateTask(TaskDto task);

    boolean deleteTask(Long id);
}
