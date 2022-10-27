package com.example.todolists_springboot.repository;

import com.example.todolists_springboot.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task,Long> {


}
