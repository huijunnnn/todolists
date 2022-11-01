package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.repository.TaskRepository;
import com.example.todolists_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.*;

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

    public List<Task> addSharedTasks(Long sharedId, Long id) {
        List<Task> tasks1 = userRepository.findById(id).get().getTasks();
        List<Task> tasks2 = userRepository.findById(sharedId).get().getTasks();
        if (tasks2==null){
            return taskRepository.findByUserId(id);
        }else {
            List<Task> tasks= of(tasks1, tasks2)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
            userRepository.findById(id).get().setTasks(tasks);
            return taskRepository.findByUserId(id);
        }

    }
}
