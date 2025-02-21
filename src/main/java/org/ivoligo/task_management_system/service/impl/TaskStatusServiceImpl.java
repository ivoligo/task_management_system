package org.ivoligo.task_management_system.service.impl;

import org.ivoligo.task_management_system.model.entity.TaskStatus;
import org.ivoligo.task_management_system.repository.TaskStatusRepository;
import org.ivoligo.task_management_system.service.TaskStatusService;
import org.springframework.stereotype.Service;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    public TaskStatusServiceImpl(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    @Override
    public TaskStatus getTaskStatus(String statusName) {

        return taskStatusRepository.findTaskStatusByName(statusName);
    }
}
