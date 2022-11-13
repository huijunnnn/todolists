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

    // review：
    //  1. 如果构造函数比较多，那么建议使用静态构造方法进行创建对象，这样的好处就是可以通过方法签名来表明更加明确的函数内容，构造函数不能自定义名称并且 Java 语言基本类型又是否分隔不开
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
