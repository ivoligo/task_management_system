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

    //TODO
    // + 1) !(tasks == null && tasks.isEmpty()) всегда будет true, если tasks не null, даже если список пустой.
    // + 2) tasks лучше использовать прямое указание типа без var более читаемо.
    // + 3) Page<TaskDto> — это не список, а объект Page. Метод isEmpty() немного не то, что нужно. Лучше проверять наличие элементов. Типа, hasContent().
    // + 4) tasks == null && tasks.hasContent() - если task будет null, tasks.isEmpty() вернет NullPointerException. Разделить проверки
    // + 5) Надо добавить обработку исключений и логирование тоже.
    // + 6) Валидация входных параметров

    @LoggingAround
    @Override
    public ResponseEntity<Page<TaskDto>> getTasks(int page, int size, FilterSortDto filterSort) {

        Pageable pageable = PageRequest.of(page, size);
        Optional<Page<TaskDto>> tasks = taskService.getTasks(filterSort, pageable);
        return tasks
                .map(taskDtos -> new ResponseEntity<>(taskDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //TODO
    // + 1) Если tasks равен null, вызов tasks.isEmpty() выбросит NullPointerException. Разделить проверки
    // + для пустого списка он возвращает 200 и пустой список. 2) 404 для пустого списка не совсем корректно. Лучше вернуть 200 с пустым списком.
    // - здесь его может не быть и тогда он вернет весь список. поэтому не понятно что валидировать 3) Валидация filterSort - не равен null?
    // + 4) Логи тоже стоит добавить.
    // +- тут не понял, вроде все для этого есть orElseGet 5) taskService.getTasks(filterSort) должно возвращать Optional со всеми вытекающими.
    // - тут пока не понял, ибо исключений не может быть. Ну БД упадет если только. 6) Исключения можно обрабатывать через @ControllerAdvice или @ExceptionHandler. Подумай как удобнее.

    @LoggingAround
    @Override
    public ResponseEntity<List<TaskDto>> getTasks(FilterSortDto filterSort) {

        Optional<List<TaskDto>> tasks = taskService.getTasks(filterSort);
        return tasks
                .map(taskDtos -> new ResponseEntity<>(taskDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //TODO
    // + 1) taskService.getTask(id) может выбросить исключение
    // + 2) taskService.getTask(id) возвращает Optional<TaskDto>
    // + 3) Валидация входных данных
    // + 4) В сервисе есть такой же метод, лучше там кастомизировать нейминг. Типа, findTaskById
    // + вообще по заданию логи нужны были для создания, обновления, удаления задачи 5) Логов тоже нет
    // 6)

    @LoggingAround
    @Override
    public ResponseEntity<TaskDto> getTask(Long id) throws IllegalArgumentException {

        Optional<TaskDto> task = taskService.getTaskById(id);
        return task
                .map(taskDto -> new ResponseEntity<>(taskDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    //TODO
    // + 1) ResponseEntity<Long> подразумевает, что в теле ответа будет идентификатор созданной задачи (taskId). ResponseEntity.created(...) не добавляет тело ответа.
    //    ему нужен URI созданного ресурса в заголовке Location. КОроче сложно)) Я бы рассмотрел вот такой вариант ResponseEntity.status(HttpStatus.CREATED).body(taskId);
    // + 2) Валидация входных данных
    // + 3) В сервисе createTask, а тут addTask. Запутаемся.

    @Override
    @LoggingAround
    public ResponseEntity<TaskDto> createTask(TaskDto task) {

        TaskDto createdTaskDto = taskService.createTask(task).orElseThrow(() -> new IllegalArgumentException("Задача не создана"));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDto);
    }

    //TODO
    // + 1) isUpdated — это обновленный объект задачи, лучше переименовать в updatedTask, логичнее и понятнее
    // + 2) HttpStatus.NOT_MODIFIED обычно используют в контексте HTTP-кеширования, когда ресурс не изменился.
    //    В твоем случае, если задача не была обновлена (например, потому что её не нашли), более подходящим статусом может быть HttpStatus.NOT_FOUND.
    // ? 3) ResponseEntity<TaskDto> лучше ResponseEntity<?>.
    // + 4) updateTask в сервисе и контроллере одинаково называются. Можно уточнить имя метода в сервисе, например, updateTaskIfExists.

    @Override
    @LoggingAround
    public ResponseEntity<TaskDto> updateTask(TaskDto task) throws IllegalArgumentException {

        TaskDto updatedTask = taskService.updateTaskIfExists(task).orElseThrow(() -> new IllegalArgumentException("Задача не обновлена"));
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    //TODO
    // + 1) Использование ResponseEntity<?> допустимо, но лучше ResponseEntity<Void>
    // + 2) Если вернется 200 или 404 все должно работать корректно, а что будет если taskService.deleteTask выкинет исключение?
    // + 3) Нужна проверка входных параметров

    @Override
    @LoggingAround
    public ResponseEntity<Void> deleteTask(Long taskId) throws IllegalArgumentException {

        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
