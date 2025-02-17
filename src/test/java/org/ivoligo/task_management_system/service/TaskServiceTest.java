package org.ivoligo.task_management_system.service;

import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.ivoligo.task_management_system.model.entity.Task;
import org.ivoligo.task_management_system.model.entity.TaskStatus;
import org.ivoligo.task_management_system.repository.TaskRepository;
import org.ivoligo.task_management_system.repository.TaskRepositoryCustom;
import org.ivoligo.task_management_system.repository.TaskStatusRepository;
import org.ivoligo.task_management_system.service.impl.TaskServiceImpl;
import org.ivoligo.task_management_system.utils.ConvertUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private final static Long ID_TEST_TASK = 1L;
    private final static String STATUS_NEW = "Новая";
    private final static String STATUS_ACTIVE = "В работе";
    private final static String STATUS_DONE = "Завершена";

    private final TaskDto testTaskDto1 = new TaskDto();
    private final TaskDto testTaskDto2 = new TaskDto();

    private final TaskStatus statusNew = new TaskStatus();
    private final TaskStatus statusActive = new TaskStatus();
    private final TaskStatus statusDone = new TaskStatus();

    private final Task testTask = new Task();
    private final Task testTask1 = new Task();
    private final Task testTask2 = new Task();

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskRepositoryCustom taskRepositoryCustom;

    @Mock
    private TaskStatusRepository taskStatusRepository;

    @Mock
    private TaskStatusService taskStatusService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {

        testTaskDto1
                .setName("testTask1")
                .setDescription("description for testTask1")
                .setStatus(STATUS_NEW);
//        testTaskDto1.setCreatedDate("");

        testTaskDto2
                .setName("testTask2")
                .setDescription("description for testTask2 for update method")
                .setStatus(STATUS_ACTIVE);

        statusNew
                .setId(1)
                .setName(STATUS_NEW)
                .setStatusOrder(1);
        statusActive
                .setId(2)
                .setName(STATUS_ACTIVE)
                .setStatusOrder(2);

        statusDone
                .setId(3)
                .setName(STATUS_DONE)
                .setStatusOrder(3);

        testTask
                .setId(ID_TEST_TASK)
                .setName("testTask")
                .setDescription("description for testTask")
                .setStatus(statusNew)
                .setCreatedDate(new Timestamp(System.currentTimeMillis()))
                .setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        testTask1
                .setId(ID_TEST_TASK)
                .setName("testTask")
                .setDescription("description for testTask")
                .setStatus(statusActive)
                .setCreatedDate(new Timestamp(System.currentTimeMillis()))
                .setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        testTask2
                .setId(ID_TEST_TASK)
                .setName("testTask")
                .setDescription("description for testTask")
                .setStatus(statusDone)
                .setCreatedDate(new Timestamp(System.currentTimeMillis()))
                .setUpdatedDate(new Timestamp(System.currentTimeMillis()));

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @DisplayName("Тестирование метода создания задачи.")
    void createTask() {

        // @todo: не соответствует методу create() в сервисе? что нужно поменять? в какой момент записывать время???
        var date = new Timestamp(System.currentTimeMillis()).getTime();
        testTaskDto1.setCreatedDate(ConvertUtils.convertTimestampToStringDate(date));
        testTaskDto1.setUpdatedDate(ConvertUtils.convertTimestampToStringDate(date));
        var testTask = ConvertUtils.convertTaskDtoToTask(testTaskDto1);

//        when(taskStatusRepository.findTaskStatusByName(any())).thenReturn(status);
        // @todo: что делать со статусом? нужно куда перенести?
//        when(taskStatusService.getTaskStatus(testTaskDto1.getName())).thenReturn(statusTest);
        testTask.setStatus(statusNew);
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        final var resultTask = taskService.createTask(testTaskDto1);

        assertNotNull(resultTask);
        assertEquals(testTaskDto1.getName(), resultTask.getName());
        assertEquals(testTaskDto1.getDescription(), resultTask.getDescription());
        /* @todo: не понимаю пока как сделать поправить тест и сервис, чтобы работала проверка статуса
            А ВООБЩЕ НУЖНО проверять статусы и то, что находится в других сервисах и репо?
         */
//        assertEquals(testTaskDto1.getStatus(), resultTask.getStatus());
    }

    @Test
    void getTasks() {
    }

    @Test
    void testGetTasks() {

        var params = new FilterSortDto();
        final var actualTasks = List.of(testTask, testTask1, testTask2);
        when(taskRepositoryCustom.findByParam(params)).thenReturn(actualTasks);

        final var resultTasks = taskService.getTasks(params);

        assertTrue(resultTasks.isPresent());
        assertEquals(actualTasks.size(), resultTasks.get().size());

    }

    @Test
    @DisplayName("Тестирование метода получения задачи по id.")
    void getTask() {

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(testTask));
//        doReturn(testTask).when(taskRepository).findById(any()).get();

        final var resultTask = taskService.getTask(ID_TEST_TASK);
        final var actualTestTaskDto = ConvertUtils.convertTaskToDto(testTask);

        assertNotNull(resultTask);
        assertEquals(resultTask.getId(), actualTestTaskDto.getId());
        assertEquals(resultTask.getName(), actualTestTaskDto.getName());
        assertEquals(resultTask.getDescription(), actualTestTaskDto.getDescription());
        assertEquals(resultTask.getStatus(), actualTestTaskDto.getStatus());
        assertEquals(resultTask.getCreatedDate(), actualTestTaskDto.getCreatedDate());
        assertEquals(resultTask.getUpdatedDate(), actualTestTaskDto.getUpdatedDate());

    }

    @Test
    @DisplayName("Тестирование метода изменения задачи.")
    void updateTask() {

        testTaskDto2.setId(1L);
        testTask.setStatus(statusActive);
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(testTask));
        when(taskStatusRepository.findTaskStatusByName(any())).thenReturn(statusActive);
        when(taskRepository.save(any(Task.class))).thenReturn(ConvertUtils.convertTaskDtoToTask(testTaskDto2));

        // @todo: почему когда заходит в сервисе в метод convertTaskToDto(Task task) status = null? он же везде есть там.
        final var result = taskService.updateTaskIfExists(testTaskDto2);

        assertEquals(testTaskDto2, result);
    }

    @Test
    @DisplayName("Тестирование метода удаления задачи по id.")
    void deleteTask() {

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(testTask));
        taskService.deleteTask(ID_TEST_TASK);

        verify(taskRepository, times(1)).deleteById(ID_TEST_TASK);
    }
}
