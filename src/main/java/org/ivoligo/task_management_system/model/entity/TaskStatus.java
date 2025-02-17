package org.ivoligo.task_management_system.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "task_status")
//@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatus {

    @Id
    @NotNull
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "status_order")
    private int statusOrder;
}
