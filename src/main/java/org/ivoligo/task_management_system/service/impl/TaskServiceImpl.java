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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private final TaskRepository taskRepository;
    private final TaskRepositoryCustom taskRepositoryCustom;
    private final TaskStatusRepository taskStatusRepository;

    public TaskServiceImpl(@Autowired TaskRepository taskRepository, TaskRepositoryCustom taskRepositoryCustom, TaskStatusRepository taskStatusRepository) {
        this.taskRepository = taskRepository;
        this.taskRepositoryCustom = taskRepositoryCustom;
        this.taskStatusRepository = taskStatusRepository;
    }

    @Override
    @LoggingAround
    public Long createTask(TaskDto taskDto) {

        var task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        var date = new Timestamp(System.currentTimeMillis());
        task.setCreatedDate(date);
        var status = taskStatusRepository.findTaskStatusByName(taskDto.getStatus());
        task.setStatus(status);
        task.setUpdatedDate(date);

        var test = taskRepository.save(task);
        return test.getId();
    }

    @Override
    public Page<TaskDto> getTasks(FilterSortDto filterSort, Pageable pageable) {

        var tasks = new ArrayList<TaskDto>();
        taskRepositoryCustom.findByParam(filterSort, pageable).forEach(task -> tasks.add(convert(task)));
        val countAllTasks = taskRepository.count();

        return new PageImpl<>(tasks, pageable, countAllTasks);
    }

    @Override
    public List<TaskDto> getTasks(FilterSortDto filterSort) {

        var tasks = new ArrayList<TaskDto>();
        taskRepositoryCustom.findByParam(filterSort).forEach(task -> tasks.add(convert(task)));

        return tasks;
    }

    @Override
    public TaskDto getTask(Long id) {

        return convert(taskRepository.findById(id).orElse(null));
    }


    @Override
    @LoggingAround
    public boolean updateTask(TaskDto taskDto) {

        val taskOptional = taskRepository.findById(taskDto.getId());
        if (taskOptional.isPresent()) {

            taskRepository.save(convert(taskDto));
            return true;
        }
        return false;

    }

    @Override
    @LoggingAround
    public boolean deleteTask(Long id) {

        if (taskRepository.findById(id).isPresent()) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private TaskDto convert(Task task) {

        if (task == null) {
            return null;
        }
        var taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus().getName());
        taskDto.setCreatedDate(convert(task.getCreatedDate().getTime()));
        taskDto.setUpdatedDate(convert(task.getUpdatedDate().getTime()));

        return taskDto;
    }

    private Task convert(TaskDto taskDto) {

        var task = new Task();
        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskStatusRepository.findTaskStatusByName(taskDto.getStatus()));
        task.setCreatedDate(convert(taskDto.getCreatedDate()));
        task.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        return task;
    }

    private String convert(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

    private Timestamp convert(String date) {


        Timestamp timestamp = null;
        try {
            timestamp = new Timestamp(dateFormat.parse(date).getTime());
        } catch (ParseException e) {
            e.getMessage();
        }
        return timestamp;
    }
}
