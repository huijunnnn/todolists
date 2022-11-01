package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.repository.TaskRepository;
import com.example.todolists_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskUserService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    public List<User> getUsersOfTaskByTaskId(Long id) {
        return userRepository.findByTaskId(id);
    }

    public List<User> getUsersOfTaskByTaskName(String name) {
        return userRepository.findByTaskName(name);
    }

    public List<Task> getTasksOfUserByUserId(Long id) {
        return taskRepository.findByUserId(id);
    }

    public List<Task> getTasksOfUserByUserName(String name) {
        return taskRepository.findByUserName(name);
    }

    public Optional<User> deleteAllTasksOfUserByUserId(Long id) {
        List<Task> tasks = userRepository.findById(id).get().getTasks();
        if (tasks == null) {
            return userRepository.findById(id);
        } else {
            userRepository.deleteAllTasks();
            return userRepository.findById(id);
        }
    }

}
