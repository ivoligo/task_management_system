package org.ivoligo.task_management_system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Сущность задачи")
public class TaskDto {

    @Schema(description = "Уникальный идентификатор задачи", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Название задачи", example = "Настроить swagger")
    private String name;

    @Schema(description = "Описание задачи", example = "Задокументировать при помощи swagger контроллеры, сущности, настроить swagger")
    private String description;

    @Schema(description = "Статус задачи", allowableValues = {"Новая", "В работе", "Завершена"})
    private String status;

    @Schema(description = "Дата и время заведения задачи", example = "08/02/2025 16:38:21", accessMode = Schema.AccessMode.READ_ONLY)
    private String createdDate;

    @Schema(description = "Дата и время изменения задачи", example = "08/02/2025 16:38:21", accessMode = Schema.AccessMode.READ_ONLY)
    private String updatedDate;

    //добавить того кто создал, исполнителя, тот кто завершил или проверил, т.е. подтвердил статус завершено или добавить статус "на проверке".
}
