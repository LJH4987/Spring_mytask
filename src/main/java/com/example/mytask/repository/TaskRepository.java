package com.example.mytask.repository;

import com.example.mytask.model.Task;
import org.springframework.data.domain.Pageable;
import java.util.*;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAll(Pageable pageable);

    List<Task> findAllByAssigneeId(Long assigneeId);

    void update(Task task);

    void deleteById(Long id);

    List<Task> findAllByAssigneeNameAndModifiedDate(String assigneeName, String modifiedDate , Pageable pageable);

    List<Task> findAllByAssigneeName(String assigneeName, Pageable pageable);

    List<Task> findAllByModifiedDate(String modifiedDate, Pageable pageable);

}