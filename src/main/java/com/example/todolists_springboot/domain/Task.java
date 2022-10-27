package com.example.todolists_springboot.domain;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tb_tasks")
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

    //多对一
//    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
//    @JoinColumn(name = "user_id")
//    private User user;


    public Task() {
    }

    public Task(String taskName, Boolean taskCompleted) {
        this.taskName = taskName;
        this.taskCompleted = taskCompleted;

    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Boolean getTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(Boolean taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskCompleted=" + taskCompleted +
                ", users=" + users +
                '}';
    }
}
