package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.handler.exception.TaskNotExistInYourTasksException;
import com.example.todolists_springboot.handler.exception.TaskNotFoundException;
import com.example.todolists_springboot.handler.exception.UserNotFoundException;
import com.example.todolists_springboot.repository.TaskRepository;
import com.example.todolists_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Stream.of;

@Service
public class AssignmentService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

    public List<User> getUsersOfTaskByTaskName(String name) {
        return userRepository.findByTaskName(name);
    }

    public List<Task> getTasksOfUserByUserId(Long id) {
        return userRepository.findTasksByUserId(id);
    }

    public List<Task> getTasksOfUserByUserName(String name) {
        return userRepository.findTasksByUserName(name);
    }

    public Task createTaskForUser(Long id, Task task) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        task.setTaskCompleted(false);
        Task savedTask = taskRepository.save(task);
        user.addTask(savedTask);
        userRepository.save(user);
        return savedTask;
    }

    public Object deleteAllTasksOfUserByUserId(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        List<Task> tasks = user.getTasks();
        if (tasks == null||tasks.isEmpty()) {
            return null;
        } else {
            user.setTasks(null);
            userRepository.save(user);
            return null;
        }
    }

    public Task addSharedTask(Long sharedTaskId, Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Task task = taskRepository.findById(sharedTaskId).orElseThrow(TaskNotFoundException::new);
        String taskName = task.getTaskName();
        Task newTask = new Task(taskName,false);
        Task savedTask = taskRepository.save(newTask);
        user.addTask(savedTask);
        userRepository.save(user);
       return savedTask;
    }
    public Task updateTaskForUser(Long user_id, Long task_id,Task newTask) {
        User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
        Task oldTask = taskRepository.findById(task_id).orElseThrow(TaskNotFoundException::new);
        Boolean isContain = userRepository.findByTaskId(oldTask.getTaskId()).contains(user);
        if (isContain) {
            newTask.setTaskId(oldTask.getTaskId());
            taskRepository.save(newTask);
            user.addTask(newTask);
            userRepository.save(user);
            return newTask;
        }else{
            throw new TaskNotExistInYourTasksException();
        }
    }

}
