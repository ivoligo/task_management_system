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
    private final static Timestamp DATE_TIMESTAMP = new Timestamp(System.currentTimeMillis());

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

        var testTask = ConvertUtils.convertTaskDtoToTask(testTaskDto1, statusNew);

        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        final var resultTask = taskService.createTask(testTaskDto1).get();

        assertNotNull(resultTask);
        assertEquals(testTaskDto1.getId(), resultTask.getId());
        assertEquals(testTaskDto1.getName(), resultTask.getName());
        assertEquals(testTaskDto1.getDescription(), resultTask.getDescription());
        assertNotNull(resultTask.getCreatedDate());
        assertNotNull(resultTask.getUpdatedDate());

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

        final var resultTask = taskService.getTaskDtoByTaskId(ID_TEST_TASK).get();
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
        testTaskDto2.setCreatedDate(ConvertUtils.convertTimestampToStringDate(DATE_TIMESTAMP.getTime()));
        testTaskDto2.setUpdatedDate(ConvertUtils.convertTimestampToStringDate(DATE_TIMESTAMP.getTime()));
        var task = ConvertUtils.convertTaskDtoToTask(testTaskDto2, statusActive);

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn( task);

        final var resultTask = taskService.updateTaskIfExists(testTaskDto2).get();

        assertNotNull(resultTask);
        assertEquals(testTaskDto2.getId(), resultTask.getId());
        assertEquals(testTaskDto2.getCreatedDate(), resultTask.getCreatedDate());
        assertNotEquals(testTaskDto2.getUpdatedDate(), resultTask.getUpdatedDate());

    }

    @Test
    @DisplayName("Тестирование метода удаления задачи по id.")
    void deleteTask() {

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(testTask));
        taskService.deleteTask(ID_TEST_TASK);

        verify(taskRepository, times(1)).deleteById(ID_TEST_TASK);
    }
}
