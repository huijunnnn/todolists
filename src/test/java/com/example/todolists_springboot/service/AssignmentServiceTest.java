package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.handler.exception.TaskNotExistInYourTasksException;
import com.example.todolists_springboot.handler.exception.TaskNotFoundException;
import com.example.todolists_springboot.handler.exception.UserNotFoundException;
import com.example.todolists_springboot.repository.TaskRepository;
import com.example.todolists_springboot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AssignmentServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private AssignmentService assignmentService;


    @Test
    public void get_all_tasks_by_user_id_and_return_the_tasks(){
        List<Task> taskOne = List.of(new Task(1L, "task1", false)
                , new Task(2L, "task2", false));
        when(userRepository.findTasksByUserId(1L)).thenReturn(taskOne);
        List<Task> resultTasks = userRepository.findTasksByUserId(1L);
        assertEquals(taskOne, resultTasks);
        verify(userRepository).findTasksByUserId(1L);
    }

    @Test
    public void get_all_users_by_task_name_and_return_the_users() {
        List<User> userOne = List.of(new User(1L, "小明"),
                new User(2L, "小兰"));
        List<User> userTwo = List.of(new User(2L, "小兰"));
        List<Task> tasks = List.of(new Task(1L, "task1", false, userOne),
                new Task(2L, "task2", false, userTwo));
        when(taskRepository.findAll()).thenReturn(tasks);
        when(userRepository.findByTaskName("task1")).thenReturn(userOne);
        List<User> resultUsers = assignmentService.getUsersOfTaskByTaskName("task1");
        assertEquals(userOne, resultUsers);
        verify(userRepository).findByTaskName("task1");
    }

    @Test
    public void should_add_a_task_to_the_user_when_the_user_not_exist() {
        Optional<User> userOptional = Optional.empty();
        Task task = new Task("task1");

        when(userRepository.findById(1L)).thenReturn(userOptional);

        assertThrows(UserNotFoundException.class, () -> assignmentService.createTaskForUser(1L, task));
        verify(userRepository).findById(1L);
    }

    @Test
    public void should_add_a_task_to_the_user_when_the_user_exist(){
        Task task = new Task("task1");
        List<Task> tasks = List.of(task);
        User savedUser = new User(1L, "zhizhi");
        Task savedTask = new Task(1L, "task1", false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(taskRepository.save(task)).thenReturn(savedTask);

        Task result = assignmentService.createTaskForUser(1L, task);
        assertEquals(savedTask, result);
        verify(userRepository).findById(1L);
        verify(taskRepository).save(task);
    }


    @Test
    public void should_delete_all_tasks_of_the_user_when_the_user_not_exist() {
        Optional<User> userOptional = Optional.empty();
        Task task = new Task("task1");

        when(userRepository.findById(1L)).thenReturn(userOptional);

        assertThrows(UserNotFoundException.class, () -> assignmentService.deleteAllTasksOfUserByUserId(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    public void should_delete_all_tasks_of_the_user_when_the_user_exist_and_tasks_is_not_null() {
        Task task = new Task("task1");
        List<Task> tasks = List.of(task);
        User savedUser = new User(1L, "zhizhi", tasks);
        User user = new User(1L, "zhizhi");

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        Object result = assignmentService.deleteAllTasksOfUserByUserId(1L);
        assertNull(result);
        verify(userRepository).findById(1L);
    }

    @Test
    public void should_delete_all_tasks_of_the_user_when_the_user_exist_and_tasks_is_null() {
        User savedUser = new User(1L, "zhizhi", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        Object result = assignmentService.deleteAllTasksOfUserByUserId(1L);
        assertNull(result);
        verify(userRepository).findById(1L);
    }

    @Test
    public void should_share_a_task_to_the_user_when_the_user_not_exist() {
        Optional<User> userOptional = Optional.empty();

        when(userRepository.findById(1L)).thenReturn(userOptional);

        assertThrows(UserNotFoundException.class, () -> assignmentService.addSharedTask(1L, 1L));
        verify(userRepository).findById(1L);
    }

    @Test
    public void should_share_task_to_the_user_when_the_task_not_exist() {
        Optional<Task> taskOptional = Optional.empty();

        when(taskRepository.findById(1L)).thenReturn(taskOptional);

        assertThrows(UserNotFoundException.class, () -> assignmentService.addSharedTask(1L, 2L));
        verify(userRepository).findById(2L);
    }

    @Test
    public void should_share_task_to_the_user_when_the_users_exist_and_shared_task_exist() {
        User savedUser = new User(1L, "zhizhi");
        Task savedTask = new Task(2L, "task1", false);
        Task task = new Task("task1",false);
        Task savedTask3 = new Task(3L, "task1", false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(taskRepository.findById(2L)).thenReturn(Optional.of(savedTask));
        when(taskRepository.save(task)).thenReturn(savedTask3);

        Task result = assignmentService.addSharedTask(2L, 1L);
        assertEquals(savedTask3, result);
        verify(userRepository).findById(1L);
        verify(taskRepository).findById(2L);
        verify(taskRepository).save(task);
    }

    @Test
    public void should_update_task_of_the_user_when_the_user_not_exist() {
        Optional<User> userOptional = Optional.empty();
        Task task = new Task(1L, "task3", false);

        when(userRepository.findById(1L)).thenReturn(userOptional);

        assertThrows(UserNotFoundException.class, () -> assignmentService.updateTaskForUser(1L, 1L, task));
        verify(userRepository).findById(1L);
    }

    @Test
    public void should_update_task_of_the_user_when_the_users_exist_but_task_not_exist() {
        Optional<Task> taskOptional = Optional.empty();
        User userOne = new User(1L, "zhizhi");
        Task task = new Task("task3", false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userOne));
        when(taskRepository.findById(1L)).thenReturn(taskOptional);

        assertThrows(TaskNotFoundException.class, () -> assignmentService.updateTaskForUser(1L, 1L, task));
        verify(userRepository).findById(1L);
    }

    @Test
    public void should_update_task_of_the_user_when_the_users_exist_and_task_exist_but_not_belong_to_the_user() {
        Optional<Task> taskOptional = Optional.empty();
        User userOne = new User(1L, "zhizhi");
        Task task = new Task("task3", false);
        Task returnedTask = new Task(1L, "zhizhi", false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userOne));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(returnedTask));
        when(userRepository.findByTaskId(1L)).thenReturn(new ArrayList<>());

        assertThrows(TaskNotExistInYourTasksException.class, () -> assignmentService.updateTaskForUser(1L, 1L, task));
        verify(userRepository).findById(1L);
        verify(taskRepository).findById(1L);
        verify(userRepository).findByTaskId(1L);
    }

    @Test
    public void should_update_task_of_the_user_when_the_users_exist_and_task_exist_and_belong_to_the_user() {
        Optional<Task> taskOptional = Optional.empty();
        User userOne = new User(1L, "zhizhi");
        Task task = new Task("task3", false);
        Task returnedTask = new Task(1L, "zhizhi", false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userOne));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(returnedTask));
        when(userRepository.findByTaskId(1L)).thenReturn(List.of(userOne));

        Task result = assignmentService.updateTaskForUser(1L, 1L, task);
        Task expected = new Task(1L, "task3", false);

        assertEquals(expected, result);
        verify(userRepository).findById(1L);
        verify(taskRepository).findById(1L);
        verify(userRepository).findByTaskId(1L);
    }
}
