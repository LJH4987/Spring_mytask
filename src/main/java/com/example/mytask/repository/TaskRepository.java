package com.example.mytask.repository;

import com.example.mytask.model.Task;
import java.util.*;

public interface TaskRepository {

    // 일정 생성
    Task save(Task task);

    // 특정 ID 일정 조회
    Optional<Task> findById(long id);

    // 모든 일정 조회 (필터링 조건 포함)
    List<Task> findAllByAssigneeIdAndModifiedDate(int assigneeId, String modifiedDate);

    Optional<Task> findById(Long id);

    List<Task> findAllByAssigneeIdAndModifiedDate(Long assigneeId, String modifiedDate);

    // 일정 수정
    void update(Task task);

    // 일정 삭제
    void deleteById(int id);

    void deleteById(Long id);
}
