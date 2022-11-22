package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.controller.handler.exception.TaskNotFoundException;
import com.example.todolists_springboot.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task addTask(Task task) {
        task.setTaskCompleted(false);
        return taskRepository.save(task);
    }

    public Task getTaskByTaskId(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        return task;
    }

    public List<Task> getTasks(Boolean completed, String keyword) {
        if(Objects.isNull(completed)&&Objects.isNull(keyword)){
            return getAllTasks();
        }
        if(!Objects.isNull(completed)&&Objects.isNull(keyword)){
            return getTasksByStatus(completed);
        }
        if(Objects.isNull(completed)&&!Objects.isNull(keyword)){
            return getTasksByKeyword(keyword);
        }
        return getTasksByStatusAndKeyword(completed,keyword);
    }

    private List<Task> getTasksByStatusAndKeyword(Boolean completed, String keyword) {
        return taskRepository.findByTaskCompletedAndKeyword(completed,keyword);
    }

    private List<Task> getTasksByKeyword(String keyword) {

        return taskRepository.findByTaskKeyword(keyword);
    }

    private List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    private List<Task> getTasksByStatus(Boolean completed) {
        return taskRepository.findByTaskCompleted(completed);
    }

    public Task updateTask(Long id, Task newTask) {
        Optional<Task> oldTask = taskRepository.findById(id);
        Task task = oldTask.orElseThrow(TaskNotFoundException::new);
        newTask.setTaskId(task.getTaskId());
        return taskRepository.save(newTask);
    }

    public void deleteTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new TaskNotFoundException();
        } else {
            taskRepository.delete(task.get());
        }
    }

}
