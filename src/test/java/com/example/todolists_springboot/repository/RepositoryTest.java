package com.example.todolists_springboot.repository;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest//注解的测试使用潜入式内存数据库
public class RepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        User userOne = new User("小明");
        User userTwo = new User("小兰");
        User userThree = new User("小花");

        Task task1 = new Task("task1");
        Task task2 = new Task("task2");
        Task task3 = new Task("task3");
        taskRepository.saveAll(Arrays.asList(task1, task2, task3));

        userOne.setTasks(Arrays.asList(task1, task2));
        userTwo.setTasks(Arrays.asList(task2, task3));
        userThree.setTasks(Arrays.asList(task1, task3));
        userRepository.saveAll(Arrays.asList(userOne, userTwo, userThree));
    }

    @Test
    public void should_add_an_user_with_no_tasks_and_return_the_user() {
        User userOne = new User("huijun");
        entityManager.persist(userOne);
        //userRepository.save(newUser);
        assertEquals(4, userRepository.findAll().size());
        assertTrue(userRepository.findByUserName("huijun").contains(userOne));
        assertEquals(userOne, userRepository.findById(4L).get());
    }

    @Test
    public void should_add_an_user_with_some_tasks_and_return_the_user() {
        User user1 = new User("zhizhi");
        Task task1 = new Task("task1", false);
        Task task2 = new Task("task2", false);
        user1.setTasks(Arrays.asList(task1, task2));
        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.persist(user1);
        assertEquals(4, userRepository.count());
        assertEquals(5, taskRepository.count());
    }

    @Test
    public void should_add_many_tasks_and_return_the_tasklists() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("task1"));
        tasks.add(new Task("task2"));
        taskRepository.saveAll(tasks);
        assertEquals(5, taskRepository.count());
    }

    @Test
    public void should_select_task_by_task_id_and_return_the_task() {
        assertEquals("task1", taskRepository.findById(1L).get().getTaskName());
        assertEquals("task2", taskRepository.findById(2L).get().getTaskName());
        assertEquals("task3", taskRepository.findById(3L).get().getTaskName());
    }

    @Test
    public void should_select_task_by_task_name_and_return_the_task() {

        Task task1 = new Task(1L, "task1", false);
        Task task3 = new Task("task1");
        taskRepository.save(task3);
        assertEquals(Arrays.asList(task1, task3), taskRepository.findByTaskName("task1"));
    }

    @Test
    public void should_select_tasks_by_user_id_and_return_the_tasks() {
        Task task1 = new Task(1L, "task1", false);
        Task task2 = new Task(2L, "task2", false);
        assertEquals(Arrays.asList(task1, task2), taskRepository.findByUserId(1L));
    }

    @Test
    public void should_select_tasks_by_user_name_and_return_the_tasks() {
        Task task1 = new Task(1L, "task1", false);
        Task task2 = new Task(2L, "task2", false);
        assertEquals(Arrays.asList(task1, task2), taskRepository.findByUserName("小明"));
    }

    @Test
    public void should_select_tasks_by_user_and_return_the_tasks() {
        Task task1 = new Task(1L, "task1", false);
        Task task2 = new Task(2L, "task2", false);
        User userOne = new User(1L, "小明");
        assertEquals(Arrays.asList(task1, task2), taskRepository.findByUser(userOne));
    }

    @Test
    public void should_select_user_by_user_id_and_return_the_user() {
        assertEquals("小明", userRepository.findById(1L).get().getUserName());
        assertEquals("小兰", userRepository.findById(2L).get().getUserName());
        assertEquals("小花", userRepository.findById(3L).get().getUserName());
    }

    @Test
    public void should_select_users_by_task_id_and_return_the_users() {
        User userOne = new User(1L, "小明");
        User userThree = new User(3L, "小花");
        assertEquals(Arrays.asList(userOne, userThree), userRepository.findByTaskId(1L));
    }

    @Test
    public void should_select_users_by_task_name_and_return_the_users() {
        User userOne = new User(1L, "小明");
        User userThree = new User(3L, "小花");
        assertEquals(Arrays.asList(userOne, userThree), userRepository.findByTaskName("task1"));
    }

    @Test
    public void should_select_users_by_task_and_return_the_users() {
        User userOne = new User(1L, "小明");
        User userThree = new User(3L, "小花");
        Task task1 = new Task(1L, "task1", false);
        assertEquals(Arrays.asList(userOne, userThree), userRepository.findByTask(task1));

    }

    @Test
    public void should_delete_all_tasks_by_user_id_and_return_empty_list() {
        List<Task> tasks = taskRepository.findByUserId(1L);
        taskRepository.deleteAll(tasks);
        assertEquals(3, taskRepository.count());
    }

    @Test
    public void updtae_by_id() {
        Optional<User> user = userRepository.findById(2L);
        user.get().setUserName("zhizhi");
        assertEquals("zhizhi", userRepository.findById(2L).get().getUserName());
    }

    @Test
    public void select_task_by_keyword() {
        List<Task> tasks = taskRepository.findAll();
        List<Task> result = taskRepository.findByTaskKeyword("task");
        assertEquals(tasks, result);
    }
}
