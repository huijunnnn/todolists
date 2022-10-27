package com.example.todolists_springboot;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.repository.TaskRepository;
import com.example.todolists_springboot.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest//注解的测试使用潜入式内存数据库
public class RepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TestEntityManager entityManager;

    @Before
    public void init(){
        User user = new User();
        user.setUserName("小明");
        entityManager.persist(new Task(null,"task1",false,user));
        entityManager.persist(new Task(null,"task2",false,user));

    }
    @Test
    public void should_add_an_user_with_no_tasks_and_return_the_user(){

        User newUser = new User(null,"huijun",null);
        entityManager.persist(newUser);
        assertEquals(2,userRepository.findAll().size());
        System.out.println(userRepository.findAll());
      //  assertEquals(true,userRepository.findAll().contains());


    }
    //多对一插入：插入多数据
    @Test
    public void should_add_an_user_with_tasks_and_return_the_user(){
       //一
        User user = new User();
        user.setUserName("张三");
        //多任务
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(null,"task1",false,user));
        tasks.add(new Task(null,"task2",false,user));
        taskRepository.saveAll(tasks);
        assertEquals(2,taskRepository.count());

    }
    //另一种方式
    @Test
    public void should_add_an_user_with_tasks_and_return_the_user_method2(){
        //一
        User user = new User();
        user.setUserName("张三");
        entityManager.persist(new Task(null,"task1",false,user));
        entityManager.persist(new Task(null,"task2",false,user));
        assertEquals(2,taskRepository.count());

    }
    //多对一：根据user_id 查询对应的所有信息
    @Test
    public void should_select_tasks_by_user_id_and_return_the_tasks(){

        User user = new User();
        user.setUserName("小明");
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(null,"task1",false,user));
        tasks.add(new Task(null,"task2",false,user));
        taskRepository.saveAll(tasks);
        List<Task> lists = taskRepository.findByUser(user);
        System.out.println(lists);
        assertEquals(2,lists.size());

    }
    //删除所有信息
    @Test
    public void should_delete_all_tasks_and_return_empty_list(){
        User user = new User();
        user.setUserId(1L);
        List<Task> tasks = taskRepository.findByUser(user);
        taskRepository.deleteAll(tasks);
        assertEquals(0,taskRepository.count());
    }

    @Test
    public void should_add_tasks(){
        List<Task> taskList = new ArrayList<>();
        Task task1 = new Task();
        task1.setTaskName("task1");
        task1.setTaskCompleted(false);
        taskList.add(task1);

        User user = new User();
        user.setUserName("huijun");
        user.setTasks(taskList);
        entityManager.persist(user);
        System.out.println(user);
        System.out.println(taskRepository.findByUser(user));
    }

}
