package com.example.todolists_springboot.domain;

import lombok.*;

import javax.persistence.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_completed")
    private Boolean taskCompleted;

    //多对一
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinColumn(name = "user_id")
    private User user;

}
