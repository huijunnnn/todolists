package com.example.todolists_springboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tb_user")
@ToString(exclude ={"tasks"} )
@EqualsAndHashCode(exclude ={ "tasks"})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "user_tasks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>(8);


    public User(String userName) {
        this.userName = userName;
    }

    public User(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
    public void addTask(Task task){
        this.tasks.add(task);
        task.getUsers().add(this);

    }

}
