package com.example.mytask.repository;

import com.example.mytask.model.Assignee;

import java.util.*;

public interface AssigneeRepository {

    Assignee save(Assignee assignee);

    Optional<Assignee> findById(Long id);

    List<Assignee> findAll();

    void update(Assignee assignee);

    void deleteById(Long id);

}