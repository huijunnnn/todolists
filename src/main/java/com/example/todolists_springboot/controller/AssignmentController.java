package com.example.todolists_springboot.controller;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AssignmentController {
    @Autowired
    AssignmentService assignmentService;

    @PostMapping("/assignment/{id}/tasks")
    public Task createTaskForUser(@PathVariable("id") Long id, @RequestBody Task task) {
        return assignmentService.createTaskForUser(id, task);
    }

    @GetMapping("/assignment/{id}/tasks")
    public List<Task> getTasksByUserId(@PathVariable("id") Long id) {
        return assignmentService.getTasksOfUserByUserId(id);
    }

    @DeleteMapping("/assignment/{id}/tasks")
    public Object deleteAllTasksOfUser(@PathVariable("id") Long id) {
        return assignmentService.deleteAllTasksOfUserByUserId(id);
    }

    @PutMapping("/assignment/{user_id}/tasks/{task_id}")
    public Task updateTasksForUser(@PathVariable("user_id") Long user_id, @PathVariable("task_id") Long task_id, @RequestBody Task task) {
        return assignmentService.updateTaskForUser(user_id, task_id, task);
    }

    @PostMapping("/assignment/{id}/tasks/addition/{sharedTaskId}")
    public Task sharedTaskToUser(@PathVariable(value = "sharedTaskId") Long sharedTaskId, @PathVariable("id") Long id) {
        return assignmentService.addSharedTask(sharedTaskId, id);
    }

    @GetMapping("/assignment/{name}/users")
    public List<User> getAllUsersOfTaskByTaskName(@PathVariable("name") String name) {
        return assignmentService.getUsersOfTaskByTaskName(name);
    }

}
