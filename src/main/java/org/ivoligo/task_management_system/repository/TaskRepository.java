package org.ivoligo.task_management_system.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.ivoligo.task_management_system.model.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

//    default String resultQuery(String query){
//        return query;
//    }
//
//    @Query("select t from Task t join TaskStatus ts on t.status.id = ts.id where ts.name in (:statuses)")
//    List<Task> findByStatus(@Param("statuses") List<String> statuses);


}
