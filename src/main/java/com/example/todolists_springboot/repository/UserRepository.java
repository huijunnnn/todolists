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


    @Query(value = "SELECT u FROM User u WHERE u.userName = :name")
    List<User> findByName(@Param("name")String name);




}
