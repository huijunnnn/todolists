package com.example.todolists_springboot.repository;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RepositoryTest {
    @MockBean
    UserRepository userRepository;
    @MockBean
    TaskRepository taskRepository;

    @Test
    public void should_add_an_user_and_return_the_user() {
        User userOne = new User("huijun");
        when(userRepository.save(userOne)).thenReturn(new User(1L, "huijun"));
        User expected = new User(1L, "huijun");
        assertEquals(expected, userRepository.save(userOne));
    }

    @Test
    public void should_add_many_tasks_and_return_the_tasklists() {
        Task task1 = new Task("task1");
        Task task2 = new Task("task2");
        Task savedTask1 = new Task(1L, "task1", false);
        Task savedTask2 = new Task(2L, "task2", false);
        List<Task> taskList = List.of(new Task(1L, "task1", false),
                new Task(2L, "task2", false));
        when(taskRepository.save(task1)).thenReturn(savedTask1);
        when(taskRepository.save(task2)).thenReturn(savedTask2);
        when(taskRepository.findAll()).thenReturn(taskList);
        assertEquals(2, taskRepository.findAll().size());
    }

    @Test
    public void should_select_task_by_task_id_and_return_the_task() {
        Task savedTask1 = new Task(1L, "task1", false);
        Task savedTask2 = new Task(2L, "task2", false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(savedTask1));
        when(taskRepository.findById(2L)).thenReturn(Optional.of(savedTask2));
        assertEquals("task1", taskRepository.findById(1L).get().getTaskName());
        assertEquals("task2", taskRepository.findById(2L).get().getTaskName());
    }

    @Test
    public void should_select_task_by_task_name_and_return_the_task() {
        Task task1 = new Task(1L, "task1", false);
        Task task2 = new Task(2L, "task1", false);
        List<Task> taskList = Arrays.asList(task1, task2);
        when(taskRepository.findByTaskName("task1")).thenReturn(taskList);
        assertEquals(taskList, taskRepository.findByTaskName("task1"));
    }

    @Test
    public void should_select_tasks_by_user_id_and_return_the_tasks() {
        Task task1 = new Task(1L, "task1", false);
        Task task2 = new Task(2L, "task2", false);
        List<Task> taskList = Arrays.asList(task1, task2);
        when(userRepository.findTasksByUserId(1L)).thenReturn(taskList);
        assertEquals(Arrays.asList(task1, task2), userRepository.findTasksByUserId(1L));
    }

    @Test
    public void should_select_tasks_by_user_name_and_return_the_tasks() {
        Task task1 = new Task(1L, "task1", false);
        Task task2 = new Task(2L, "task2", false);
        List<Task> taskList = Arrays.asList(task1, task2);
        when(taskRepository.findByUserName("小明")).thenReturn(taskList);
        assertEquals(Arrays.asList(task1, task2), taskRepository.findByUserName("小明"));
    }

    @Test
    public void should_select_tasks_by_user_and_return_the_tasks() {
        Task task1 = new Task(1L, "task1", false);
        Task task2 = new Task(2L, "task2", false);
        User userOne = new User(1L, "小明");
        List<Task> taskList = Arrays.asList(task1, task2);
        when(taskRepository.findByUser(userOne)).thenReturn(taskList);
        assertEquals(Arrays.asList(task1, task2), taskRepository.findByUser(userOne));
    }

    @Test
    public void should_select_user_by_user_id_and_return_the_user() {
        User savedUser1 = new User(1L, "小明");
        User savedUser2 = new User(2L, "小兰");
        User savedUser3 = new User(3L, "小花");
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(savedUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.of(savedUser3));
        assertEquals("小明", userRepository.findById(1L).get().getUserName());
        assertEquals("小兰", userRepository.findById(2L).get().getUserName());
        assertEquals("小花", userRepository.findById(3L).get().getUserName());
    }

    @Test
    public void should_select_users_by_task_id_and_return_the_users() {
        User userOne = new User(1L, "小明");
        User userThree = new User(3L, "小花");
        when(userRepository.findByTaskId(1L)).thenReturn(Arrays.asList(userOne, userThree));
        assertEquals(Arrays.asList(userOne, userThree), userRepository.findByTaskId(1L));
    }

    @Test
    public void should_select_users_by_task_name_and_return_the_users() {
        User userOne = new User(1L, "小明");
        User userThree = new User(3L, "小花");
        when(userRepository.findByTaskName("task1")).thenReturn(Arrays.asList(userOne, userThree));
        assertEquals(Arrays.asList(userOne, userThree), userRepository.findByTaskName("task1"));
    }

    @Test
    public void should_select_users_by_task_and_return_the_users() {
        User userOne = new User(1L, "小明");
        User userThree = new User(3L, "小花");
        Task task1 = new Task(1L, "task1", false);
        when(userRepository.findByTask(task1)).thenReturn(Arrays.asList(userOne, userThree));
        assertEquals(Arrays.asList(userOne, userThree), userRepository.findByTask(task1));
    }

    @Test
    public void should_delete_all_tasks_by_user_id_and_return_empty_list() {
        List<Task> tasks = taskRepository.findByUserId(1L);
        doNothing().when(taskRepository).deleteAll();
        assertEquals(0, taskRepository.count());
    }

    @Test
    public void select_task_by_keyword() {
        Task task1 = new Task(1L, "task", false);
        Task task2 = new Task(2L, "task", false);
        List<Task> taskList = Arrays.asList(task1, task2);
        when(taskRepository.findByTaskName("task")).thenReturn(taskList);
        assertEquals(taskList, taskRepository.findByTaskName("task"));
    }
}
