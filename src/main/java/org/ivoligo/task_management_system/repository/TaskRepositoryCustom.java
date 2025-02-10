package org.ivoligo.task_management_system.repository;

import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.entity.Task;

import java.util.List;

public interface TaskRepositoryCustom {

    List<Task> findByParam(FilterSortDto filterSort);
}
