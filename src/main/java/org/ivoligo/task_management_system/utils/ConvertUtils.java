package org.ivoligo.task_management_system.utils;

import org.apache.commons.lang3.StringUtils;
import org.ivoligo.task_management_system.model.dto.TaskDto;
import org.ivoligo.task_management_system.model.entity.Task;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertUtils {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static TaskDto convertTaskToDto(Task task) {

        if (task == null) {
            return null;
        }
        var taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus().getName());
        taskDto.setCreatedDate(convertTimestampToStringDate(task.getCreatedDate().getTime()));
        taskDto.setUpdatedDate(convertTimestampToStringDate(task.getUpdatedDate().getTime()));

        return taskDto;
    }

    public static Task convertTaskDtoToTask(TaskDto taskDto) {

        var task = new Task();
        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setCreatedDate(convertStringDateToTimestamp(taskDto.getCreatedDate()));
        task.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        return task;
    }

    public static String convertTimestampToStringDate(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

    public static Timestamp convertStringDateToTimestamp(String date) {


        Timestamp timestamp = null;
        try {
            if (StringUtils.isNotEmpty(date)) {
                timestamp = new Timestamp(dateFormat.parse(date).getTime());
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return timestamp;
    }
}
