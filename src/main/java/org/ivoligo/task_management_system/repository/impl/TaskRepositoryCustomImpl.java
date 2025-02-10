package org.ivoligo.task_management_system.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.ivoligo.task_management_system.model.dto.FilterSortDto;
import org.ivoligo.task_management_system.model.entity.Task;
import org.ivoligo.task_management_system.repository.TaskRepositoryCustom;
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

        StringBuilder sqlFilter = new StringBuilder();
        Map<String, String> sqlParams = new HashMap<String, String>();

        if (filterSort.getFilterStatusNames() != null && !filterSort.getFilterStatusNames().isEmpty()) {
            sqlFilter.append(" join TaskStatus ts on t.status.id = ts.id where ts.name in (:statuses) ");
            sqlParams.put("statuses", StringUtils.join(filterSort.getFilterStatusNames(), ","));
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

        return query.getResultList();
    }
}
