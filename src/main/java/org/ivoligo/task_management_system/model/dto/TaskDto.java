package org.ivoligo.task_management_system.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {

    private Long id;

    private String name;

    private String description;

    private String status;

    private String createdDate;

    private String updatedDate;
}
