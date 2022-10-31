package com.example.todolists_springboot;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.repository.TaskRepository;
import com.example.todolists_springboot.service.TaskService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;


    @Test
    public void should_add_a_task_and_return_the_task() {
        Task newTask = new Task("task1");
        Task returnedTask = new Task(1L, "task1", false);
        when(taskRepository.save(newTask)).thenReturn(returnedTask);
        Task result = taskService.addTask(newTask);
        assertEquals(result, returnedTask);
        verify(taskRepository).save(newTask);
    }
    @Test
    public void should_get_all_the_tasks_when_there_is_no_parameters(){
        List<Task> returnedTasks = List.of(new Task(1L, "Test", false),
                new Task(2L, "Test", true));
        when(taskRepository.findAll()).thenReturn(returnedTasks);
        List<Task> result = taskService.getTasks(null);
        System.out.println(result);
        assertEquals(result,returnedTasks);
        verify(taskRepository).findAll();
    }

    @Test
    void should_get_all_the_tasks_when_the_parameter_is_assigned_to_true(){
        List<Task> returnedTasks = List.of(new Task(1L, "Test", true));
        when(taskRepository.findByTaskCompleted(true)).thenReturn(returnedTasks);
        List<Task> result = taskService.getTasks(true);
        assertEquals(result,returnedTasks);
        verify(taskRepository).findByTaskCompleted(true);
    }
    @Test
    void should_get_all_the_tasks_when_the_parameter_is_assigned_to_false(){
        List<Task> returnedTasks = List.of(new Task(1L, "Test", false));
        when(taskRepository.findByTaskCompleted(false)).thenReturn(returnedTasks);
        List<Task> result = taskService.getTasks(false);
        assertEquals(result,returnedTasks);
        verify(taskRepository).findByTaskCompleted(false);
    }
    @Test
    void should_update_the_task_by_id_when_the_task_exist(){
        Task requestBody = new Task("task");
        Task newTask = new Task(1L,"task",false);
        Optional<Task> newTaskOptional = Optional.of(newTask);
        when(taskRepository.findById(1L)).thenReturn(newTaskOptional);
        when(taskRepository.save(newTask)).thenReturn(newTask);
        Task result = taskService.updateTask(1L,requestBody);
        assertEquals(result,newTask);
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(requestBody);
    }


}
