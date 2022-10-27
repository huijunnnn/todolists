package com.example.todolists_springboot;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.repository.TaskRepository;
import com.example.todolists_springboot.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void should_add_an_user_with_no_tasks_and_return_the_user(){

        User newUser = new User(null,"huijun",null);
        entityManager.persist(newUser);
        assertEquals(1,userRepository.findAll().size());

    }




}
