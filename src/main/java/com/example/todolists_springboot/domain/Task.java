package com.example.todolists_springboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "tasks")
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public Task(Long taskId, String taskName, Boolean taskCompleted) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskCompleted = taskCompleted;
    }

    public Task(Long taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
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

    public List<User> getUsers() {
        return users;
    }

}
