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

    //TODO
    // 1) !(tasks == null && tasks.isEmpty()) всегда будет true, если tasks не null, даже если список пустой.
    // 2) tasks лучше использовать прямое указание типа без var более читаемо.
    // 3) Page<TaskDto> — это не список, а объект Page. Метод isEmpty() немного не то, что нужно. Лучше проверять наличие элементов. Типа, hasContent().
    // 4) tasks == null && tasks.hasContent() - если task будет null, tasks.isEmpty() вернет NullPointerException. Разделить проверки
    // 5) Надо добавить обработку исключений и логирование тоже.
    // 6) Валидация входных параметров

    @Override
    public ResponseEntity<Page<TaskDto>> getTasks(int page, int size, FilterSortDto filterSort) {

        Pageable pageable = PageRequest.of(page, size);
        var tasks = taskService.getTasks(filterSort, pageable);
        return tasks
                .map(taskDtos -> new ResponseEntity<>(taskDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //TODO
    // 1) Если tasks равен null, вызов tasks.isEmpty() выбросит NullPointerException. Разделить проверки
    // 2) 404 для пустого списка не совсем корректно. Лучше вернуть 200 с пустым списком.
    // 3) Валидация filterSort - не равен null?
    // 4) Логи тоже стоит добавить.
    // 5) taskService.getTasks(filterSort) должно возвращать Optional со всеми вытекающими.
    // 6) Исключения можно обрабатывать через @ControllerAdvice или @ExceptionHandler. Подумай как удобнее.

    @Override
    public ResponseEntity<List<TaskDto>> getTasks(FilterSortDto filterSort) {

        var tasks = taskService.getTasks(filterSort);
        return tasks
                .map(taskDtos -> new ResponseEntity<>(taskDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //TODO
    // 1) taskService.getTask(id) может выбросить исключение
    // 2) taskService.getTask(id) возвращает Optional<TaskDto>
    // 3) Валидация входных данных
    // 4) В сервисе есть такой же метод, лучше там кастомизировать нейминг. Типа, findTaskById
    // 5) Логов тоже нет
    // 6)

    @Override
    public ResponseEntity<TaskDto> getTask(Long id) {

        var task = taskService.getTask(id);
        return task != null
            ? ResponseEntity.ok(task)
            : ResponseEntity.notFound().build();
    }

    //TODO
    // 1) ResponseEntity<Long> подразумевает, что в теле ответа будет идентификатор созданной задачи (taskId). ResponseEntity.created(...) не добавляет тело ответа.
    //    ему нужен URI созданного ресурса в заголовке Location. КОроче сложно)) Я бы рассмотрел вот такой вариант ResponseEntity.status(HttpStatus.CREATED).body(taskId);
    // 2) Валидация входных данных
    // 3) В сервисе createTask, а тут addTask. Запутаемся.

    @Override
    @LoggingAround
    public ResponseEntity<Long> addTask(TaskDto task) {

        var taskId = taskService.createTask(task);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build(taskId)).build();
//        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    //TODO
    // 1) isUpdated — это обновленный объект задачи, лучше переименовать в updatedTask, логичнее и понятнее
    // 2) HttpStatus.NOT_MODIFIED обычно используют в контексте HTTP-кеширования, когда ресурс не изменился.
    //    В твоем случае, если задача не была обновлена (например, потому что её не нашли), более подходящим статусом может быть HttpStatus.NOT_FOUND.
    // 3) ResponseEntity<TaskDto> лучше ResponseEntity<?>.
    // 4) updateTask в сервисе и контроллере одинаково называются. Можно уточнить имя метода в сервисе, например, updateTaskIfExists.

    @Override
    @LoggingAround
    public ResponseEntity<TaskDto> updateTask(TaskDto task) {

        /*
         @todo: здесь указывать еще Not_found?
           или то, что выбросится исключение этого достаточно?
         */
        return new ResponseEntity<>(taskService.updateTaskIfExists(task), HttpStatus.OK);
    }

    //TODO
    // 1) Использование ResponseEntity<?> допустимо, но лучше ResponseEntity<Void>
    // 2) Если вернется 200 или 404 все должно работать корректно, а что будет если taskService.deleteTask выкинет исключение?
    // 3) Нужна проверка входных параметров

    @Override
    @LoggingAround
    public ResponseEntity<?> deleteTask(Long id) {

        return taskService.deleteTask(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
