package org.ivoligo.task_management_system.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.ivoligo.task_management_system.model.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
