package com.example.todolists_springboot.controller.handler.exception;

public class TaskNotExistInYourTasksException extends RuntimeException{
    public TaskNotExistInYourTasksException() {
        super("Task not found in your tasks");
    }
}
