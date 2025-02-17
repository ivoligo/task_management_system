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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
public class TaskController implements TaskControllerApi {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ResponseEntity<Page<TaskDto>> getTasks(int page, int size, FilterSortDto filterSort) {

        Pageable pageable = PageRequest.of(page, size);
        var tasks = taskService.getTasks(filterSort, pageable);
        return tasks
                .map(taskDtos -> new ResponseEntity<>(taskDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<List<TaskDto>> getTasks(FilterSortDto filterSort) {

        var tasks = taskService.getTasks(filterSort);
        return tasks
                .map(taskDtos -> new ResponseEntity<>(taskDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<TaskDto> getTask(Long id) {

        var task = taskService.getTask(id);
        return task != null
                ? ResponseEntity.ok(task)
                : ResponseEntity.notFound().build();
    }

    @Override
    @LoggingAround
    public ResponseEntity<Long> addTask(TaskDto task) {

        var taskId = taskService.createTask(task);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build(taskId)).build();
//        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    @Override
    @LoggingAround
    public ResponseEntity<TaskDto> updateTask(TaskDto task) {

        /*
         @todo: здесь указывать еще Not_found?
           или то, что выбросится исключение этого достаточно?
         */
        return new ResponseEntity<>(taskService.updateTaskIfExists(task), HttpStatus.OK);
    }

    @Override
    @LoggingAround
    public ResponseEntity<?> deleteTask(Long id) {

        return taskService.deleteTask(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
