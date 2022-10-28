package com.example.todolists_springboot.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_tasks")
@ToString(exclude = {"users"})
@EqualsAndHashCode(exclude = {"users"})
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

    public Task(Long taskId, String taskName, Boolean taskCompleted) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskCompleted = taskCompleted;
    }

    public Task(String taskName) {
        this.taskName = taskName;
        this.taskCompleted = false;
    }

    public Task(String taskName, Boolean taskCompleted) {
        this.taskName = taskName;
        this.taskCompleted = taskCompleted;

    }

    public Task(String taskName, List<User> users) {
        this.taskName = taskName;
        this.users = users;
    }
}
