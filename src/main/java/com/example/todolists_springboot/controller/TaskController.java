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
// review:
//  1. API 的设计建议参考 RESTful ，一般都是在类级别使用资源名称
@RequestMapping("/api")
public class TaskController {
    // review: 
    //  1. 不建议使用该注解进行依赖注入，一般使用构造参数进行注入
    //  2. 可见性修饰符确实，默认是包级别的可见性，因此可能被同一个包的其他类使用，一般建议使用最小的可见性
    @Autowired
    TaskService taskService;
    @Autowired
    AssignmentService assignmentService;

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

    // review：
    //  1. 一般搜索 API 建议使用 GET 参数来传递参数，可以参考一下 Github 的 API 设计
    @GetMapping("/tasks/keyword-text/{keyword}")
    public List<Task> getTasksByKeyword(@PathVariable("keyword") String keyword) {
        return taskService.getTasksByKeyword(keyword);
    }
    // review：
    //  1. 代码风格不对，这里原来缺少空格
    @GetMapping("/tasks/{name}/users")
    public List<User> getAllUsersOfTaskByTaskName(@PathVariable("name") String name) {
        return assignmentService.getUsersOfTaskByTaskName(name);
    }
}
