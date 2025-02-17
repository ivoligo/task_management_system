package org.ivoligo.task_management_system.service;

import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface TaskService {

    TaskDto createTask(TaskDto task);

    Optional<Page<TaskDto>> getTasks(FilterSortDto filterSort, Pageable pageable);

    Optional<List<TaskDto>> getTasks(FilterSortDto filterSort);

    TaskDto getTask(Long id);

    TaskDto updateTaskIfExists(TaskDto task);

    boolean deleteTask(Long id);
}
