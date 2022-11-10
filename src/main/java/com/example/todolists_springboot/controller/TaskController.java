package com.example.todolists_springboot.controller;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.service.TaskService;
import com.example.todolists_springboot.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public Task addTask(@RequestBody Task task) {
        return taskService.addTask(task);
    }

    @GetMapping("/tasks")
    public List<Task> getTasks(@RequestParam(value = "completed", required = false) Boolean completed) {
        return taskService.getTasks(completed);
    }

    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable("id") Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @GetMapping("/tasks/{id}")
    public Task getTaskByTaskId(@PathVariable("id") Long id) {
        return taskService.getTaskByTaskId(id);
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/tasks/keyword-text/{keyword}")
    public List<Task> getTasksByKeyword(@PathVariable("keyword") String keyword) {
        return taskService.getTasksByKeyword(keyword);
    }

}
