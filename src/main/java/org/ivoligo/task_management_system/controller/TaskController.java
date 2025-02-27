package org.ivoligo.task_management_system.controller;

import org.ivoligo.task_management_system.aop.logging.annotation.LoggingAround;
import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.ivoligo.task_management_system.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TaskController implements TaskControllerApi {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @LoggingAround
    @Override
    public ResponseEntity<Page<TaskDto>> getTasks(int page, int size, FilterSortDto filterSort) {

        Pageable pageable = PageRequest.of(page, size);
        Optional<Page<TaskDto>> tasks = taskService.getTasks(filterSort, pageable);
        return tasks
                .map(taskDtos -> new ResponseEntity<>(taskDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @LoggingAround
    @Override
    public ResponseEntity<List<TaskDto>> getTasks(FilterSortDto filterSort) {

        Optional<List<TaskDto>> tasks = taskService.getTasks(filterSort);
        return tasks
                .map(taskDtos -> new ResponseEntity<>(taskDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @LoggingAround
    @Override
    public ResponseEntity<TaskDto> getTask(Long id) throws IllegalArgumentException {

        Optional<TaskDto> task = taskService.getTaskDtoByTaskId(id);
        return task
                .map(taskDto -> new ResponseEntity<>(taskDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @Override
    @LoggingAround
    public ResponseEntity<TaskDto> createTask(TaskDto task) {

        TaskDto createdTaskDto = taskService.createTask(task).orElseThrow(() -> new IllegalArgumentException("Задача не создана"));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDto);
    }

    @Override
    @LoggingAround
    public ResponseEntity<TaskDto> updateTask(TaskDto task) throws IllegalArgumentException {

        TaskDto updatedTask = taskService.updateTaskIfExists(task).orElseThrow(() -> new IllegalArgumentException("Задача не обновлена"));
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @Override
    @LoggingAround
    public ResponseEntity<Void> deleteTask(Long taskId) throws IllegalArgumentException {

        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
