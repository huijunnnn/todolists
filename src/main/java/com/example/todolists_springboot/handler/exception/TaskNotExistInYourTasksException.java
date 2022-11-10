package com.example.todolists_springboot.handler.exception;

public class TaskNotExistInYourTasksException extends RuntimeException{
    public TaskNotExistInYourTasksException() {
        super("Task not found in your tasks");
    }
}
