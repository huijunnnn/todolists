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

import java.util.List;
import java.util.Optional;

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


}
