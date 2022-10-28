package com.example.todolists_springboot;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.repository.TaskRepository;
import com.example.todolists_springboot.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest//注解的测试使用潜入式内存数据库
public class RepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void testC() {
        User user1 = new User("zhizhi");
        Task task1 = new Task("task1", false);
        Task task2 = new Task("task2", false);
        user1.setTasks(Arrays.asList(task1, task2));
        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.persist(user1);
        System.out.println(userRepository.findAll());
        System.out.println(taskRepository.findAll());
    }

    @Test
    public void should_add_an_user_with_no_tasks_and_return_the_user() {
        User userOne = new User("huijun");
        entityManager.persist(userOne);
        //userRepository.save(newUser);
        assertEquals(1, userRepository.findAll().size());
        System.out.println(userOne);
        assertTrue(userRepository.findByName("huijun").contains(userOne));
        assertEquals(userOne, userRepository.findById(1L).get());
    }


    //多对一插入：插入多数据
    @Test
    public void should_add_an_user_with_tasks_and_return_the_user() {
        //一
        User user = new User();
        user.setUserName("张三");
        //多任务
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("task1", false));
        tasks.add(new Task("task2", false));
        taskRepository.saveAll(tasks);
        assertEquals(4, taskRepository.count());

    }
//    //另一种方式
//    @Test
//    public void should_add_an_user_with_tasks_and_return_the_user_method2(){
//        //一
//        User user = new User();
//        user.setUserName("张三");
//        entityManager.persist(new Task("task1",false));
//        entityManager.persist(new Task("task2",false));
//        assertEquals(4,taskRepository.count());
//
//    }
    //多对一：根据user_id 查询对应的所有信息
//    @Test
//    public void should_select_tasks_by_user_id_and_return_the_tasks(){
//
//        User user = new User();
//        user.setUserName("小明");
//        List<Task> tasks = new ArrayList<>();
//        tasks.add(new Task("task1",false));
//        tasks.add(new Task("task2",false));
//        taskRepository.saveAll(tasks);
//        List<Task> lists = taskRepository.findByUser(user);
//        System.out.println(lists);
//        assertEquals(2,lists.size());
//
//    }
//    //删除所有信息
//    @Test
//    public void should_delete_all_tasks_and_return_empty_list(){
//        User user = new User();
//        user.setUserId(1L);
//        List<Task> tasks = taskRepository.findByUser(user);
//        taskRepository.deleteAll(tasks);
//        assertEquals(2,taskRepository.count());
//    }
//
//    @Test
//    public void should_add_tasks(){
//        List<Task> taskList = new ArrayList<>();
//        Task task1 = new Task();
//        task1.setTaskName("task1");
//        task1.setTaskCompleted(false);
//        taskList.add(task1);
//
//        User user = new User();
//        user.setUserName("huijun");
//        user.setTasks(taskList);
//        entityManager.persist(user);
//        System.out.println(user);
//        System.out.println(taskRepository.findByUser(user));
//    }
//    @Test
//    public void test_by_id(){
//        //懒加载过程：
//        //1.findById 只查询User和其他关联立即加载
//        Optional<User> user = userRepository.findById(1L);
//        System.out.println(user);
//    }
//    @Test
//    public void delete_by_id(){
//        List<Task> taskList = new ArrayList<>();
//        Task task1 = new Task();
//        task1.setTaskName("task1");
//        task1.setTaskCompleted(false);
//        taskList.add(task1);
//
//        User user = new User();
//        user.setUserName("huijun");
//        user.setTasks(taskList);
//        entityManager.persist(user);
//        userRepository.deleteById(3L);
//
//        System.out.println(userRepository.findAll());
//
//    }
//    @Test
//    @Transactional
//    @Commit
//    public void updtae_by_id(){
//
//        Optional<User> user = userRepository.findById(2L);
//        user.get().setUserName("zhizhi");
//        System.out.println(userRepository.findAll());
//
//    }

}
