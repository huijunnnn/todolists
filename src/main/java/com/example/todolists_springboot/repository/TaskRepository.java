package com.example.todolists_springboot.repository;

import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT t FROM Task t WHERE t.taskName = :name")
    List<Task> findByTaskName(@Param("name") String name);

    @Query(value = "SELECT t FROM Task t JOIN t.users r WHERE r = :user")
    List<Task> findByUser(@Param("user") User user);

    @Query(value = "SELECT t FROM Task t JOIN t.users r WHERE r.userId = :Id")
    List<Task> findByUserId(@Param("Id") Long Id);

    @Query(value = "SELECT t FROM Task t JOIN t.users r WHERE r.userName = :name")
    List<Task> findByUserName(@Param("name") String name);

    List<Task> findByTaskCompleted(Boolean completed);

    @Query(value = "SELECT t FROM Task t WHERE t.taskName like %keyword%")
    List<Task> findByTaskKeyword(String keyword);
}
