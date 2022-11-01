package com.example.todolists_springboot.controller;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.service.TaskUserService;
import com.example.todolists_springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    TaskUserService taskUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserByUserId(@PathVariable("id") Long id) {
        return userService.getUserById(id).get();
    }

    @GetMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/tasks/{id}")
    public List<Task> getTasksByUserId(@PathVariable("id") Long id) {
        return taskUserService.getTasksOfUserByUserId(id);
    }

    @GetMapping("/tasks/{name}")
    public List<Task> getTasksByUserName(@PathVariable("name") String name) {
        return taskUserService.getTasksOfUserByUserName(name);
    }

    @DeleteMapping("/tasks/{id}")
    public Optional<User> deleteAllTasksOfUser(@PathVariable("id") Long id) {
        return taskUserService.deleteAllTasksOfUserByUserId(id);
    }


}
