package com.example.todolists_springboot.handler;

import com.example.todolists_springboot.handler.exception.TaskNotExistInYourTasksException;
import com.example.todolists_springboot.handler.exception.TaskNotFoundException;
import com.example.todolists_springboot.handler.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResult handle(TaskNotFoundException exception) {
        return new ErrorResult(exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResult handle(UserNotFoundException exception) {
        return new ErrorResult(exception.getMessage());
    }

    @ExceptionHandler(TaskNotExistInYourTasksException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResult handle(TaskNotExistInYourTasksException exception) {
        return new ErrorResult(exception.getMessage());
    }
    // review：
    //  1. 上边所有的函数可以合并为一个函数，需要重新设计异常的层次，可以尝试一下，看看如何做会更好
    //  2. 这主要的问题是每当增加一个异常都需要在这里增加一个对应的异常的处理函数


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResult handle(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return new ErrorResult(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResult handle(Exception exception) {
        return new ErrorResult(exception.getMessage());
    }
}
