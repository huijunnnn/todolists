package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.controller.handler.exception.UserNotFoundException;
import com.example.todolists_springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
