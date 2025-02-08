package org.ivoligo.task_management_system.repository;

import org.ivoligo.task_management_system.model.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
