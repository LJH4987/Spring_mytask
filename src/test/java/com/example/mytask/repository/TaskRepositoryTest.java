package com.example.mytask.repository;

import com.example.mytask.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testCreateTask() {
        // 새로운 Task 생성
        Task task = new Task();
        task.setTaskName("Test Task");
        task.setAssigneeId(1L);

        // 저장
        Task savedTask = taskRepository.save(task);

        // 저장된 Task 확인
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTaskName()).isEqualTo("Test Task");
    }

    @Test
    public void testFindTaskById() {
        // 새로운 Task 생성 및 저장
        Task task = new Task();
        task.setTaskName("Another Test Task");
        task.setAssigneeId(1L);
        Task savedTask = taskRepository.save(task);

        // ID로 Task 조회
        Task foundTask = taskRepository.findById(savedTask.getId()).orElse(null);

        // 조회된 Task 확인
        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getTaskName()).isEqualTo("Another Test Task");
    }
}