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


}
