package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public Task addTask(Task task){
        task.setTaskCompleted(false);
        return taskRepository.save(task);
    }
    public List<Task> getTasks(Boolean completed){
        if (!Objects.isNull(completed)){
            return getTasksByStatus(completed);
        }else{
            return getAllTasks();
        }
    }

    private List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    private List<Task> getTasksByStatus(Boolean completed) {
        return taskRepository.findByTaskCompleted(completed);
    }

}
