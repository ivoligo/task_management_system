package org.ivoligo.task_management_system.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.val;
import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.entity.Task;
import org.ivoligo.task_management_system.repository.TaskRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    private static final String FIND_TASKS_SQL_QUERY = "select t from Task t";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Task> findByParam(FilterSortDto filterSort) {

        Query query = getQuery(filterSort);

        return query.getResultList();
    }

    @Override
    public List<Task> findByParam(FilterSortDto filterSort, Pageable pageable) {

        Query query = getQuery(filterSort);
        val pageNumber = pageable.getPageNumber();
        val pageSize = pageable.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    private Query getQuery(FilterSortDto filterSort) {
        StringBuilder sqlFilter = new StringBuilder();
        Map<String, List<String>> sqlParams = new HashMap<>();

        if (filterSort.getFilterStatusNames() != null && !filterSort.getFilterStatusNames().isEmpty()) {
            sqlFilter.append(" join TaskStatus ts on t.status.id = ts.id where ts.name in (:statuses) ");
            sqlParams.put("statuses", filterSort.getFilterStatusNames());

        }
        if (filterSort.isSortCreatedDate()) {
            //Сделал desc, чтобы было видно как работает.
            sqlFilter.append(" order by t.createdDate desc ");
        } else if (filterSort.isSortModifiedDate()) {
            //Сделал desc, чтобы было видно как работает.
            sqlFilter.append(" order by t.updatedDate desc ");
        }

        Query query = em.createQuery(FIND_TASKS_SQL_QUERY + sqlFilter, Task.class);
        sqlParams.forEach(query::setParameter);

        return query;
    }
}
