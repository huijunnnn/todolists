package com.example.todolists_springboot.domain;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_tasks")
@ToString(exclude ={"users"})
@EqualsAndHashCode(exclude ={ "users"})
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_completed")
    private Boolean taskCompleted;

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.LAZY)
    private List<User> users;

    public Task(String taskName) {
        this.taskName = taskName;
        this.taskCompleted = false;
    }
    public Task(String taskName, Boolean taskCompleted) {
        this.taskName = taskName;
        this.taskCompleted = taskCompleted;

    }


}
