package com.example.todolists_springboot.handler.exception;
// review：
//  1. 下一行的风格不对
public class TaskNotExistInYourTasksException extends RuntimeException{
    public TaskNotExistInYourTasksException() {
        super("Task not found in your tasks");
    }
}
