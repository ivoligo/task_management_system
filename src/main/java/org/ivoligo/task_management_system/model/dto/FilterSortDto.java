package org.ivoligo.task_management_system.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterSortDto {

    private List<String> filterStatusNames;
    private boolean sortCreatedDate;
    private boolean sortModifiedDate;
}
