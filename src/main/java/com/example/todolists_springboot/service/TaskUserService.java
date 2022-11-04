package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.handler.exception.TaskNotFoundException;
import com.example.todolists_springboot.handler.exception.UserNotFoundException;
import com.example.todolists_springboot.repository.TaskRepository;
import com.example.todolists_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;

@Service
public class TaskUserService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    public List<User> getUsersOfTaskByTaskId(Long id) {
        return userRepository.findByTaskId(id);
    }

    public List<User> getUsersOfTaskByTaskName(String name) {
        return userRepository.findByTaskName(name);
    }

    public List<Task> getTasksOfUserByUserId(Long id) {
        //return taskRepository.findByUserId(id);
        return userRepository.findTasksByUserId(id);
    }

    public List<Task> getTasksOfUserByUserName(String name) {

        return userRepository.findTasksByUserName(name);
    }

    public List<Task> createTaskForUser(Long id, Task task) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        Long taskId = task.getTaskId();
        if(taskId != null){
            Optional<Task> findTask = taskRepository.findById(taskId);
           if(findTask.isEmpty()){
               throw new TaskNotFoundException();
           }
           user.get().addTask(task);
           userRepository.save(user.get());
           return userRepository.findById(id).get().getTasks();
        }
           taskRepository.save(task);
           Task savedTask = taskRepository.findLastTask();
           user.get().addTask(savedTask);
           userRepository.save(user.get());
           return userRepository.findById(id).get().getTasks();
    }

    public Optional<User> deleteAllTasksOfUserByUserId(Long id) {
        Optional<User> oldUser = userRepository.findById(id);
        User user = oldUser.orElseThrow(UserNotFoundException::new);
        List<Task> tasks = user.getTasks();
        if (tasks == null) {
            return oldUser;
        } else {
            user.setTasks(null);
            userRepository.save(user);
            return userRepository.findById(id);
        }
    }

    public List<Task> addSharedTasks(Long sharedId, Long id) {
        Optional<User> userOne = userRepository.findById(id);
        Optional<User> userTwo = userRepository.findById(sharedId);
        if (userOne.isEmpty() || userTwo.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<Task> tasks1 = userRepository.findById(id).get().getTasks();
        List<Task> tasks2 = userRepository.findById(sharedId).get().getTasks();
        if (tasks2==null||tasks2.isEmpty()) {
            return tasks1;
        }else{
            List<Task> tasks = of(tasks1, tasks2)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());

            userOne.get().setTasks(tasks);
            userRepository.save(userOne.get());
            return tasks;
        }
    }
    public Task updateTaskForUser(Long user_id, Long task_id,Task newTask) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            Task oldTask = taskRepository.findById(task_id).orElseThrow(TaskNotFoundException::new);
            newTask.setTaskId(oldTask.getTaskId());
            taskRepository.save(newTask);
            user.get().addTask(newTask);
            userRepository.save(user.get());
            return newTask;
        }
    }

}
