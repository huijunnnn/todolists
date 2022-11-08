package com.example.todolists_springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_tasks")
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name="task_id")
    private Long taskId;
    private boolean completed;

    @Column(name = "task_completed")
    private boolean taskCompleted;

}

