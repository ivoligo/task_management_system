package org.ivoligo.task_management_system.controller;

import jakarta.validation.Valid;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
public interface TaskControllerApi {

    @GetMapping("/list")
    ResponseEntity<List<TaskDto>> getTasks();

    @GetMapping("/task/{taskId}")
    ResponseEntity<TaskDto> getTask(@PathVariable(value = "taskId") Long id);

    @PostMapping("/create")
    ResponseEntity<Long> addTask(@RequestBody TaskDto task);

    @PatchMapping("/update")
    ResponseEntity<?> updateTask(@RequestBody @Valid TaskDto task);

    @DeleteMapping("/delete/{taskId}")
    ResponseEntity<?> deleteTask(@PathVariable(value = "taskId") Long id);

}
