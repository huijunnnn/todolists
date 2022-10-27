package com.example.todolists_springboot.repository;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    //List<Task> findByUser(User user);


}
