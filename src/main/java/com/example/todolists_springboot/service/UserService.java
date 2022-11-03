package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.handler.exception.UserNotFoundException;
import com.example.todolists_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    public User addUser(User user) {
        return userRepository.save(user);
    }
    public List<User> getUsers() {
        return getAllUsers();
    }
    private List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsers(String name) {
        return userRepository.findByUserName(name);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public User updateUser(Long id, User newUser) {
        Optional<User> oldUser = userRepository.findById(id);
        User user = oldUser.orElseThrow(UserNotFoundException::new);
        newUser.setUserId(oldUser.get().getUserId());
        return userRepository.save(newUser);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            userRepository.deleteById(id);
        }
    }

    public User createTaskForUser(Long id, Task task) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        } else {
           user.get().setTasks(List.of(task));
           return userRepository.save(user.get());
        }
    }

    public User updateTaskForUser(Long id, Task task) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            user.get().setTasks(List.of(task));
            return userRepository.save(user.get());
        }
    }
}
