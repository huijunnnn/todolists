package com.example.todolists_springboot.controller;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.service.TaskService;
import com.example.todolists_springboot.service.TaskUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    TaskUserService taskUserService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task addTask(@RequestBody Task task) {
        return taskService.addTask(task);
    }

    @GetMapping
    public List<Task> getTasks(@RequestParam(value = "completed", required = false) Boolean completed) {
        return taskService.getTasks(completed);
    }

    @GetMapping("/{id}")
    public Task updateTask(@PathVariable("id") Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/{keyword}")
    public List<Task> getTasksByKeyword(@PathVariable("keyword") String keyword) {
        return taskService.getTasksByKeyword(keyword);
    }

    @GetMapping("/users/{id}")
    public List<User> getAllUsersOfTaskByTaskId(@PathVariable("id") Long id) {
        return taskUserService.getUsersOfTaskByTaskId(id);
    }

    @GetMapping("/users/{name}")
    public List<User> getAllUsersOfTaskByTaskName(@PathVariable("name") String name) {
        return taskUserService.getUsersOfTaskByTaskName(name);
    }

}
