package org.ivoligo.task_management_system.service.impl;

import lombok.val;
import org.ivoligo.task_management_system.aop.logging.annotation.LoggingAround;
import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.ivoligo.task_management_system.model.entity.Task;
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

import java.sql.Timestamp;
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

        var task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        var date = new Timestamp(System.currentTimeMillis());
        var status = taskStatusService.getTaskStatus(taskDto.getStatus());
        task.setStatus(status);
        task.setCreatedDate(date);
        task.setUpdatedDate(date);

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
    public Optional<TaskDto> getTaskById(Long id) {

        var task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Задача с идентификатором: " + id + "не найдена."));
        return Optional.of(ConvertUtils.convertTaskToDto(task));
    }


    @Override
    @LoggingAround
    public Optional<TaskDto> updateTaskIfExists(TaskDto taskDto) {

        taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Задача с идентификатором: " + taskDto.getId() + "не найдена."));
        var task = ConvertUtils.convertTaskDtoToTask(taskDto);
        task.setStatus(taskStatusRepository.findTaskStatusByName(taskDto.getStatus()));

        return Optional.of(ConvertUtils.convertTaskToDto(taskRepository.save(task)));

    }

    @Override
    @LoggingAround
    public void deleteTask(Long taskId) {

        taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Задача с идентификатором: " + taskId + "не найдена."));
        taskRepository.deleteById(taskId);
    }

}
