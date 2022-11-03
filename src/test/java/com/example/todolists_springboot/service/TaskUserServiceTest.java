package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.repository.TaskRepository;
import com.example.todolists_springboot.repository.UserRepository;
import com.example.todolists_springboot.service.TaskUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskUserService taskUserService;

    @Test
    public void delete_all_tasks_of_the_user_when_tasks_exist_and_return_the_user() {
        List<Task> savedTasks = List.of(new Task(1L, "task1", false));
        User savedUser = new User(1L, "zhizhi", savedTasks);
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        User result = taskUserService.deleteAllTasksOfUserByUserId(1L).get();
        User finalUser = new User(1L, "zhizhi", null);
        assertEquals(result, finalUser);
        verify(userRepository, times(2)).findById(1L);
    }

    @Test
    public void delete_all_tasks_of_the_user_when_tasks_not_exist_and_return_the_user() {
        User savedUser = new User(1L, "zhizhi");
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        User result = taskUserService.deleteAllTasksOfUserByUserId(1L).get();
        User finalUser = new User(1L, "zhizhi", null);
        assertEquals(result, finalUser);
        verify(userRepository, times(2)).findById(1L);
    }
    @Test
    public void add_shared_tasks_to_the_user_and_when_sharedUser_tasks_exist_return_the_tasks(){
        List<Task> tasks1 = List.of(new Task(1L, "task1", false));
        User userOne = new User(1L,"zhizhi",tasks1);
        List<Task> tasks2 = List.of(new Task(2L, "task2", false));
        User userTwo = new User(2L,"lacy",tasks2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userOne));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userTwo));
        List<Task> tasks = of(tasks1, tasks2)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        List<Task> resultedTasks = taskUserService.addSharedTasks(2L,1L);
        assertEquals(tasks,resultedTasks);
        verify(userRepository,times(4)).findById(1L);
        verify(userRepository,times(2)).findById(2L);

    }
    @Test
    public void add_shared_tasks_to_the_user_when_shardUser_tasks_not_exist_and_return_the_tasks(){
        List<Task> tasks1 = List.of(new Task(1L, "task1", false));
        User userOne = new User(1L,"zhizhi",tasks1);
        User userTwo = new User(2L,"lacy");
        when(userRepository.findById(1L)).thenReturn(Optional.of(userOne));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userTwo));
        List<Task> resultedTasks = taskUserService.addSharedTasks(2L,1L);
        assertEquals(tasks1,resultedTasks);
        verify(userRepository,times(3)).findById(1L);
        verify(userRepository,times(2)).findById(2L);

    }
    @Test
    public void get_all_tasks_by_user_name_and_return_the_tasks(){
        List<Task> taskOne = List.of(new Task(1L,"task1",false)
                ,new Task(2L,"task2",false));
        List<Task> taskTwo = List.of(new Task(2L,"task2",false));
        List<User> users = List.of(new User(1L,"小明",taskOne),
                new User(2L,"小兰",taskTwo));
        when(userRepository.findAll()).thenReturn(users);
        when(taskRepository.findByUserName("小明")).thenReturn(taskOne);
        List<Task> resultTasks = taskUserService.getTasksOfUserByUserName("小明");
        assertEquals(taskOne,resultTasks);
        verify(taskRepository).findByUserName("小明");

    }
    @Test
    public void get_all_tasks_by_user_id_and_return_the_tasks(){

        List<Task> taskOne = List.of(new Task(1L,"task1",false)
                ,new Task(2L,"task2",false));
        List<Task> taskTwo = List.of(new Task(2L,"task2",false));
        List<User> users = List.of(new User(1L,"小明",taskOne),
                new User(2L,"小兰",taskTwo));
        when(userRepository.findAll()).thenReturn(users);
        when(taskRepository.findByUserId(1L)).thenReturn(taskOne);
        List<Task> resultTasks = taskUserService.getTasksOfUserByUserId(1L);
        assertEquals(taskOne,resultTasks);
        verify(taskRepository).findByUserId(1L);
    }
}
