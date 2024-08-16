package com.example.mytask.controller;

import com.example.mytask.dto.TaskDto;
import com.example.mytask.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDto> getAllTasks(
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String assigneeName,
            @RequestParam(required = false) String modifiedDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        if (assigneeName != null && modifiedDate != null) {
            return taskService.getTasksByAssigneeNameAndModifiedDate(assigneeName, modifiedDate, page, size);
        } else if (assigneeId != null) {
            return taskService.getTasksByAssigneeId(assigneeId, page, size);
        }
        return taskService.getAllTasks(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/search/assignee")
    public List<TaskDto> getTasksByAssigneeName(
            @RequestParam String assigneeName,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return taskService.getTasksByAssigneeName(assigneeName, page, size);
    }

    @GetMapping("/search/modified-date")
    public List<TaskDto> getTasksByModifiedDate(
            @RequestParam String modifiedDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return taskService.getTasksByModifiedDate(modifiedDate, page, size);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto) {
        TaskDto task = taskService.createTask(taskDto);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(
            @PathVariable Long id,
            @RequestBody TaskDto taskDto,
            @RequestParam String password
    ) {
        taskService.updateTask(id, taskDto, password);
        return ResponseEntity.ok("수정이 완료되었습니다!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, @RequestParam String password) {
        taskService.deleteTask(id, password);
        return ResponseEntity.ok("삭제가 완료되었습니다!");
    }
}