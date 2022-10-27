package com.example.todolists_springboot.repository;


import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User,Long > {

    //List<User> findByTask(Task task);

    //List<User> findByName(String name);
}
