package com.example.mytask.service;

import com.example.mytask.dto.TaskDto;
import com.example.mytask.exception.PasswordMismatchException;
import com.example.mytask.exception.ResourceNotFoundException;
import com.example.mytask.exception.TasksNotFoundException;
import com.example.mytask.model.Task;
import com.example.mytask.repository.JdbcTaskRepository;
import com.example.mytask.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

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
            throw new TasksNotFoundException("해당 담당자의 일정을 찾을 수 없습니다.");
        }
        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public List<TaskDto> getTasksByAssigneeNameAndModifiedDate(String assigneeName, String modifiedDate, int page, int size) {
        List<Task> tasks = taskRepository.findAllByAssigneeNameAndModifiedDate(assigneeName, modifiedDate);
        if (tasks.isEmpty()) {
            throw new TasksNotFoundException("해당 담당자의 일정을 찾을 수 없습니다.");
        }
        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public List<TaskDto> getTasksByAssigneeName(String assigneeName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Task> tasks = taskRepository.findAllByAssigneeName(assigneeName, pageable);
        if (tasks.isEmpty()) {
            throw new TasksNotFoundException("해당 담당자의 일정을 찾을 수 없습니다.");
        }
        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByModifiedDate(String modifiedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Task> tasks = taskRepository.findAllByModifiedDate(modifiedDate, pageable);
        if (tasks.isEmpty()) {
            throw new TasksNotFoundException("해당 수정일의 일정을 찾을 수 없습니다.");
        }
        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public TaskDto createTask(TaskDto taskDto) {
        Task task = mapToEntity(taskDto);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    public Task updateTask(Long id, TaskDto taskDetails, String password) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("일정을 찾을 수 없습니다."));

        if (!task.getPassword().equals(password)) {
            throw new PasswordMismatchException("잘못된 비밀번호입니다.");
        }

        task.setTaskName(taskDetails.getTaskName());
        task.setAssigneeId(taskDetails.getAssigneeId());
        task.setPassword(taskDetails.getPassword());
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
        task.setUpdatedAt(LocalDateTime.now());
        return task;
    }

    private TaskDto mapToDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskName(task.getTaskName());
        taskDto.setAssigneeId(task.getAssigneeId());
        taskDto.setCreatedAt(task.getCreatedAt());
        taskDto.setUpdatedAt(task.getUpdatedAt());
        return taskDto;
    }

}
