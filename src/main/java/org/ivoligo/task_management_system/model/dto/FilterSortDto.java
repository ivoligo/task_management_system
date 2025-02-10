package org.ivoligo.task_management_system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Сущность для фильтрации и сортировки.")
public class FilterSortDto {

    @Schema(description = "Фильтр для получения списка задач по статусу или статусам", example = "[\"Новая\", \"Завершена\"]", allowableValues = {"Новая", "В работе", "Завершена"})
    private List<String> filterStatusNames;

    @Schema(description = "Включение сортировки по дате создания", example = "true")
    private boolean sortCreatedDate;

    @Schema(description = "Включение сортировки по дате изменения", example = "false")
    private boolean sortModifiedDate;
}
