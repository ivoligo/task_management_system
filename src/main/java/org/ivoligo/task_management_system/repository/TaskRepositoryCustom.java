package org.ivoligo.task_management_system.repository;

import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.entity.Task;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskRepositoryCustom {

    List<Task> findByParam(FilterSortDto filterSort);

    List<Task> findByParam(FilterSortDto filterSort, Pageable pageable);

}
