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
        validateAssigneeDto(assigneeDto);
        Assignee assignee = new Assignee();
        assignee.setName(assigneeDto.getName());
        assignee.setEmail(assigneeDto.getEmail());
        assignee.setCreatedAt(LocalDateTime.now());
        assignee.setUpdatedAt(LocalDateTime.now());
        return assigneeRepository.save(assignee);
    }

    public Assignee updateAssignee(Long id, AssigneeDto assigneeDto) {
        validateAssigneeDto(assigneeDto);
        Assignee assignee = assigneeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("담당자를 찾을 수 없습니다."));
        assignee.setName(assigneeDto.getName());
        assignee.setEmail(assigneeDto.getEmail());
        assignee.setUpdatedAt(LocalDateTime.now());
        return assigneeRepository.update(assignee);
    }

    public void deleteAssignee(Long id) {
        Assignee assignee = assigneeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("삭제할 담당자를 찾을 수 없습니다."));
        assigneeRepository.deleteById(id);
    }

    private void validateAssigneeDto(AssigneeDto assigneeDto) {
        if (assigneeDto.getName() == null || assigneeDto.getName().trim().isEmpty() ||
                assigneeDto.getEmail() == null || assigneeDto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("담당자 이름과 이메일을 입력해주세요.");
        }
        if (!assigneeDto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("유효한 이메일 주소를 입력해주세요.");
        }
    }

}