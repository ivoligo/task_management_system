package org.ivoligo.task_management_system.controller;

import jakarta.validation.Valid;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/task")
@Tag(name = "Задачи", description = "Контроллер для управления задачами")
public interface TaskControllerApi {

    @GetMapping("/list")
    ResponseEntity<List<TaskDto>> getTasks(@RequestBody FilterSortDto filterSort);

    @GetMapping("/{id}")
    @Operation(summary = "Получение задачи по идентификатору",
            description = "Позволяет данные конкретной задачи")
    ResponseEntity<TaskDto> getTask(
            @PathVariable(value = "id") @Parameter(description = "Идентификатор задачи", example = "1") Long id);

    @PostMapping("/create")
    @Operation(summary = "Заведение задачи",
            description = "Позволяет завести новую задачу")
    ResponseEntity<Long> addTask(
            @RequestBody @Valid TaskDto task);

    @PatchMapping("/update")
    @Operation(summary = "Обновление задачи",
            description = "Позволяет обновить конкретную задачу, например описание или статус")
    ResponseEntity<?> updateTask(
            @RequestBody @Valid TaskDto task);

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление конкретной задачи",
            description = "Позволяет удалить задачу")
    ResponseEntity<?> deleteTask(
            @PathVariable(value = "id") @Parameter(description = "Идентификатор задачи", example = "1") Long id);

}
