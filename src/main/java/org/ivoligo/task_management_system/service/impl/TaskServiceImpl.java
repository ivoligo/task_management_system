package org.ivoligo.task_management_system.service.impl;

import lombok.val;
import org.ivoligo.task_management_system.aop.logging.annotation.LoggingAround;
import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.ivoligo.task_management_system.model.entity.Task;
import org.ivoligo.task_management_system.model.entity.TaskStatus;
import org.ivoligo.task_management_system.repository.TaskRepository;
import org.ivoligo.task_management_system.repository.TaskRepositoryCustom;
import org.ivoligo.task_management_system.repository.TaskStatusRepository;
import org.ivoligo.task_management_system.service.TaskService;
import org.ivoligo.task_management_system.service.TaskStatusService;
import org.ivoligo.task_management_system.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskRepositoryCustom taskRepositoryCustom;
    private final TaskStatusRepository taskStatusRepository;

    private final TaskStatusService taskStatusService;

    public TaskServiceImpl(@Autowired TaskRepository taskRepository, TaskRepositoryCustom taskRepositoryCustom, TaskStatusRepository taskStatusRepository, TaskStatusService taskStatusService) {
        this.taskRepository = taskRepository;
        this.taskRepositoryCustom = taskRepositoryCustom;
        this.taskStatusRepository = taskStatusRepository;
        this.taskStatusService = taskStatusService;
    }

    @Override
    @LoggingAround
    public Optional<TaskDto> createTask(TaskDto taskDto) {

        TaskStatus status = taskStatusRepository.findTaskStatusByName(taskDto.getStatus());
        var task = ConvertUtils.convertTaskDtoToTask(taskDto, status);

        return Optional.of(ConvertUtils.convertTaskToDto(taskRepository.save(task)));
    }

    @Override
    public Optional<Page<TaskDto>> getTasks(FilterSortDto filterSort, Pageable pageable) {

        var taskDtoList = new ArrayList<TaskDto>();
        taskRepositoryCustom.findByParam(filterSort, pageable).forEach(task -> taskDtoList.add(ConvertUtils.convertTaskToDto(task)));
        val countAllTasks = taskRepository.count();

        return Optional.of(new PageImpl<>(taskDtoList, pageable, countAllTasks));
    }

    @Override
    public Optional<List<TaskDto>> getTasks(FilterSortDto filterSort) {

        var tasks = new ArrayList<TaskDto>();
        taskRepositoryCustom.findByParam(filterSort).forEach(task -> tasks.add(ConvertUtils.convertTaskToDto(task)));

        return Optional.of(tasks);
    }

    @Override
    public Optional<TaskDto> getTaskDtoByTaskId(Long id) {

        var task = getTaskById(id);
        return Optional.of(ConvertUtils.convertTaskToDto(task));
    }


    @Override
    @LoggingAround
    public Optional<TaskDto> updateTaskIfExists(TaskDto taskDto) {

        getTaskById(taskDto.getId());
        TaskStatus status = taskStatusRepository.findTaskStatusByName(taskDto.getStatus());
        var task = ConvertUtils.convertTaskDtoToTask(taskDto, status);

        return Optional.of(ConvertUtils.convertTaskToDto(taskRepository.save(task)));

    }

    @Override
    @LoggingAround
    public void deleteTask(Long taskId) {

        getTaskById(taskId);
        taskRepository.deleteById(taskId);
    }

    private Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Задача с идентификатором: " + taskId + "не найдена."));
    }

}
