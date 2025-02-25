package org.ivoligo.task_management_system.service;

import org.ivoligo.task_management_system.model.entity.TaskStatus;

public interface TaskStatusService {

    TaskStatus getTaskStatus(String statusName);
}
