package com.example.todolists_springboot.service;

import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.controller.handler.exception.UserNotFoundException;
import com.example.todolists_springboot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void should_add_an_user_and_return_the_user() {
        User user = new User("zhizhi");
        User returnedUser = new User(1L, "zhizhi");
        when(userRepository.save(user)).thenReturn(returnedUser);
        User result = userService.addUser(user);
        assertEquals(result, returnedUser);
        verify(userRepository).save(user);
    }

    @Test
    public void should_get_all_the_users() {
        List<User> returnedUsers = List.of(new User(1L, "Test1"),
                new User(2L, "Test2"));
        when(userRepository.findAll()).thenReturn(returnedUsers);
        List<User> result = userService.getAllUsers();
        assertEquals(result, returnedUsers);
        verify(userRepository).findAll();
    }

    @Test
    public void should_get_the_user_by_user_id() {
        User user = new User(1L, "小张");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.getUserById(1L);
        assertEquals(user, result);
        verify(userRepository).findById(1L);
    }

    @Test
    public void should_update_the_user_by_id_when_the_user_exists() {
        User requestBody = new User("zhzihi");
        User newUser = new User(1L, "zhuzhu");
        Optional<User> newUserOptional = Optional.of(newUser);
        when(userRepository.findById(1L)).thenReturn(newUserOptional);
        when(userRepository.save(requestBody)).thenReturn(newUser);
        User result = userService.updateUser(1L, requestBody);
        assertEquals(result, newUser);
        verify(userRepository).findById(1L);
        verify(userRepository).save(requestBody);
    }

    @Test
    public void should_update_the_user_by_id_when_the_user_does_not_exist() {
        User requestBody = new User("lili");
        Optional<User> newUserOptional = Optional.empty();
        when(userRepository.findById(1L)).thenReturn(newUserOptional);
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, requestBody));
        verify(userRepository).findById(1L);
    }

    @Test
    public void should_delete_the_user_by_id_when_the_user_exists() {
        User newUser = new User(1L, "zhuzhu");
        Optional<User> newUserOptional = Optional.of(newUser);
        when(userRepository.findById(1L)).thenReturn(newUserOptional);
        userService.deleteUser(1L);
        verify(userRepository).findById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    public void should_delete_the_user_when_the_user_dose_not_exist() {
        Optional<User> userOptional = Optional.empty();
        when(userRepository.findById(1L)).thenReturn(userOptional);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository).findById(1L);
    }
}
