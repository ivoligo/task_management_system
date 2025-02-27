package org.ivoligo.task_management_system.service;

import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface TaskService {

    Optional<TaskDto> createTask(TaskDto task);

    Optional<Page<TaskDto>> getTasks(FilterSortDto filterSort, Pageable pageable);

    Optional<List<TaskDto>> getTasks(FilterSortDto filterSort);

    Optional<TaskDto> getTaskDtoByTaskId(Long id);

    Optional<TaskDto> updateTaskIfExists(TaskDto task);

    void deleteTask(Long id);
}
