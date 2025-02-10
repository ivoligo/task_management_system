package org.ivoligo.task_management_system.repository;

import org.ivoligo.task_management_system.model.entity.TaskStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TaskStatusRepository extends CrudRepository<TaskStatus, Integer> {

    @Query("select ts from TaskStatus ts where ts.name = (:name)")
    TaskStatus findTaskStatusByName(@Param("name") String name);
}
