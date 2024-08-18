package com.example.mytask.controller;

import com.example.mytask.dto.AssigneeDto;
import com.example.mytask.model.Assignee;
import com.example.mytask.service.AssigneeService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/assignees")
public class AssigneeController {

    private final AssigneeService assigneeService;

    public AssigneeController(AssigneeService assigneeService) {
        this.assigneeService = assigneeService;
    }

    @GetMapping
    public List<Assignee> getAllAssignees() {
        return assigneeService.getAllAssignees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignee> getAssigneeById(@PathVariable Long id) {
        Assignee assignee = assigneeService.getAssigneeById(id);
        return ResponseEntity.ok(assignee);
    }

    @PostMapping
    public ResponseEntity<Assignee> createAssignee(@Valid @RequestBody AssigneeDto assigneeDto) {
        Assignee createdAssignee = assigneeService.createAssignee(assigneeDto);
        return ResponseEntity.ok(createdAssignee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assignee> updateAssignee(@PathVariable Long id, @Valid @RequestBody AssigneeDto assigneeDto) {
        Assignee updatedAssignee = assigneeService.updateAssignee(id, assigneeDto);
        return ResponseEntity.ok(updatedAssignee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAssignee(@PathVariable Long id) {
        assigneeService.deleteAssignee(id);
        return ResponseEntity.ok("담당자가 성공적으로 삭제되었습니다.");
    }

}