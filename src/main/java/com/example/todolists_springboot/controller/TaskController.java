package com.example.todolists_springboot.controller;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Task addTask(@RequestBody Task task) {
        return taskService.addTask(task);
    }

    @GetMapping
    public List<Task> getTasks(@RequestParam(value = "completed", required = false) Boolean completed) {
        return taskService.getTasks(completed);
    }
    @GetMapping("/{id}")
    public Task updateTask(@PathVariable("id")Long id,@RequestBody Task task){
        return taskService.updateTask(id,task);
    }

}
