package org.ivoligo.task_management_system.controller;

import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.ivoligo.task_management_system.service.TaskService;
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
    public ResponseEntity<List<TaskDto>> getTasks(FilterSortDto filterSort) {

        var tasks = taskService.getTasks(filterSort);
        return !(tasks == null && tasks.isEmpty())
                ? ResponseEntity.ok(tasks)
                : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<TaskDto> getTask(Long id) {

        var task = taskService.getTask(id);
        return task != null
                ? ResponseEntity.ok(task)
                : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Long> addTask(TaskDto task) {

        var taskId = taskService.createTask(task);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build(taskId)).build();
//        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    @Override
    public ResponseEntity<?> updateTask(TaskDto task) {

        var isUpdated = taskService.updateTask(task);
        return isUpdated
                ? ResponseEntity.ok(isUpdated)
                // not found или not modified ?
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @Override
    public ResponseEntity<?> deleteTask(Long id) {

        return taskService.deleteTask(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
