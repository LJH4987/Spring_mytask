package com.example.mytask.service;

import com.example.mytask.dto.AssigneeDto;
import com.example.mytask.model.Assignee;
import com.example.mytask.exception.ResourceNotFoundException;
import com.example.mytask.repository.AssigneeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AssigneeService {

    private final AssigneeRepository assigneeRepository;

    public AssigneeService(AssigneeRepository assigneeRepository) {
        this.assigneeRepository = assigneeRepository;
    }

    public List<Assignee> getAllAssignees() {
        return assigneeRepository.findAll();
    }

    public Assignee getAssigneeById(Long id) {
        return assigneeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("담당자를 찾을 수 없습니다."));
    }

    public Assignee createAssignee(AssigneeDto assigneeDto) {
        Assignee assignee = new Assignee();
        assignee.setName(assigneeDto.getName());
        assignee.setEmail(assigneeDto.getEmail());
        assignee.setCreatedAt(LocalDateTime.now());
        assignee.setUpdatedAt(LocalDateTime.now());
        return assigneeRepository.save(assignee);
    }

    public Assignee updateAssignee(Long id, AssigneeDto assigneeDto) {
        Assignee assignee = assigneeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("담당자를 찾을 수 없습니다."));
        assignee.setName(assigneeDto.getName());
        assignee.setEmail(assigneeDto.getEmail());
        assignee.setUpdatedAt(LocalDateTime.now());
        return assigneeRepository.save(assignee);
    }

    public void deleteAssignee(Long id) {
        Assignee assignee = assigneeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("삭제할 담당자를 찾을 수 없습니다."));
        assigneeRepository.deleteById(id);
    }
}