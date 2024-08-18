package com.example.mytask.service;

import com.example.mytask.dto.TaskDto;
import com.example.mytask.exception.PasswordMismatchException;
import com.example.mytask.exception.ResourceNotFoundException;
import com.example.mytask.exception.NoTasksFoundException;
import com.example.mytask.model.Task;
import com.example.mytask.repository.TaskRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer;

    public List<TaskDto> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("일정을 찾을 수 없습니다."));
        return mapToDto(task);
    }

    public List<TaskDto> getTasksByAssigneeId(Long assigneeId, int page, int size) {
        List<Task> tasks = taskRepository.findAllByAssigneeId(assigneeId);
        if (tasks.isEmpty()) {
            throw new NoTasksFoundException("해당 담당자의 일정을 찾을 수 없습니다.");
        }
        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByAssigneeNameAndModifiedDate(String assigneeName, String modifiedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        List<Task> tasks;

        if (assigneeName != null && modifiedDate != null) {
            tasks = taskRepository.findAllByAssigneeNameAndModifiedDate(assigneeName, modifiedDate, pageable);
        } else if (assigneeName != null) {
            tasks = taskRepository.findAllByAssigneeName(assigneeName, pageable);
        } else if (modifiedDate != null) {
            tasks = taskRepository.findAllByModifiedDate(modifiedDate, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }

        if (tasks.isEmpty()) {
            throw new NoTasksFoundException("해당 조건에 맞는 일정을 찾을 수 없습니다.");
        }

        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByAssigneeName(String assigneeName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Task> tasks = taskRepository.findAllByAssigneeName(assigneeName, pageable);
        if (tasks.isEmpty()) {
            throw new NoTasksFoundException("해당 담당자의 일정을 찾을 수 없습니다.");
        }
        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByModifiedDate(String modifiedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Task> tasks = taskRepository.findAllByModifiedDate(modifiedDate, pageable);
        if (tasks.isEmpty()) {
            throw new NoTasksFoundException("해당 수정일의 일정을 찾을 수 없습니다.");
        }
        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TaskDto createTask(TaskDto taskDto) {
        validateTask(taskDto);
        Task task = mapToEntity(taskDto);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    private void validateTask(TaskDto taskDto) {
        if (taskDto.getTaskName() == null || taskDto.getTaskName().length() < 3) {
            throw new IllegalArgumentException("일정 이름은 최소 3자 이상이어야 합니다.");
        }
        if (taskDto.getPassword() == null || taskDto.getPassword().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 합니다.");
        }
    }

    public Task updateTask(Long id, TaskDto taskDetails, String password) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("일정을 찾을 수 없습니다."));

        if (!task.getPassword().equals(password)) {
            throw new PasswordMismatchException("잘못된 비밀번호입니다.");
        }

        task.setAssigneeId(taskDetails.getAssigneeId());
        task.setTaskName(taskDetails.getTaskName());
        task.setUpdatedAt(LocalDateTime.now());

        taskRepository.update(task);

        return task;
    }

    public void deleteTask(Long id, String password) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("일정을 찾을 수 없습니다."));

        if (!task.getPassword().equals(password)) {
            throw new PasswordMismatchException("잘못된 비밀번호입니다.");
        }

        taskRepository.deleteById(id);
    }

    private Task mapToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setTaskName(taskDto.getTaskName());
        task.setAssigneeId(taskDto.getAssigneeId());
        task.setPassword(taskDto.getPassword());
        task.setCreatedAt(taskDto.getCreatedAt());
        task.setUpdatedAt(taskDto.getUpdatedAt());
        return task;
    }

    private TaskDto mapToDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskName(task.getTaskName());
        taskDto.setAssigneeId(task.getAssigneeId());
        taskDto.setPassword(task.getPassword());
        taskDto.setCreatedAt(task.getCreatedAt());
        taskDto.setUpdatedAt(task.getUpdatedAt());
        return taskDto;
    }

}