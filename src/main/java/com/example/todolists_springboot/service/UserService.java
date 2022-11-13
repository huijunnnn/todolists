package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.handler.exception.UserNotFoundException;
import com.example.todolists_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return user;
    }
    public User updateUser(Long id, User newUser) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        newUser.setUserId(user.getUserId());
        return userRepository.save(newUser);
    }
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            userRepository.deleteById(id);
        }
    }

}
