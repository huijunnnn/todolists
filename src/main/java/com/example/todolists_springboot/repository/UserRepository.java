package com.example.todolists_springboot.repository;


import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u WHERE u.userName = :name")
    List<User> findByUserName(@Param("name") String name);

    @Query(value = "SELECT u FROM User u JOIN u.tasks r WHERE r.taskId = :id")
    List<User> findByTaskId(@Param("id") Long id);

    @Query(value = "SELECT u FROM User u JOIN u.tasks r WHERE r.taskName = :name")
    List<User> findByTaskName(@Param("name") String name);

    @Query(value = "SELECT u FROM User u JOIN u.tasks r WHERE r = :task")
    List<User> findByTask(@Param("task") Task task);

    @Query(value = "SELECT DISTINCT u.tasks FROM User u WHERE u.userId = :id")
    List<Task> findTasksByUserId(@Param("id") Long id);

    @Query(value = "SELECT u.tasks FROM User u WHERE u.userName = :name")
    List<Task> findTasksByUserName(@Param("name") String name);
}
