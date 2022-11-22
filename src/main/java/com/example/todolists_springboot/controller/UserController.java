package com.example.todolists_springboot.controller;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.service.AssignmentService;
import com.example.todolists_springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AssignmentService assignmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserByUserId(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/tasks")
    public Task createTaskForUser(@PathVariable("id") Long id, @RequestBody Task task) {
        return assignmentService.createTaskForUser(id, task);
    }

    @GetMapping("/{id}/tasks")
    public List<Task> getTasksByUserId(@PathVariable("id") Long id) {
        return assignmentService.getTasksOfUserByUserId(id);
    }

    @DeleteMapping("/{id}/tasks")
    public Object deleteAllTasksOfUser(@PathVariable("id") Long id) {
        return assignmentService.deleteAllTasksOfUserByUserId(id);
    }

    @PutMapping("/{user_id}/tasks/{task_id}")
    public Task updateTasksForUser(@PathVariable("user_id") Long user_id, @PathVariable("task_id") Long task_id, @RequestBody Task task) {
        return assignmentService.updateTaskForUser(user_id, task_id, task);
    }

    @PostMapping("/{id}/tasks/addition/{sharedTaskId}")
    public Task sharedTaskToUser(@PathVariable(value = "sharedTaskId") Long sharedTaskId, @PathVariable("id") Long id) {
        return assignmentService.addSharedTask(sharedTaskId, id);
    }
}
