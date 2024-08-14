package com.example.mytask.repository;

import com.example.mytask.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public abstract class JdbcTaskRepository implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Task save(Task task) {
        String sql = "INSERT INTO Task (task_name, assignee_id, password, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, task.getTaskName(), task.getAssigneeId(), task.getPassword(), task.getCreatedAt(), task.getUpdatedAt());
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM Task WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new TaskRowMapper())
                .stream()
                .findFirst();
    }

    @Override
    public List<Task> findAllByAssigneeIdAndModifiedDate(Long assigneeId, String modifiedDate) {
        String sql = "SELECT * FROM Task WHERE assignee_id = ? AND DATE(updated_at) = ? ORDER BY updated_at DESC";
        return jdbcTemplate.query(sql, new Object[]{assigneeId, modifiedDate}, new TaskRowMapper());
    }

    @Override
    public void update(Task task) {
        String sql = "UPDATE Task SET task_name = ?, assignee_id = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, task.getTaskName(), task.getAssigneeId(), task.getUpdatedAt(), task.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Task WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            Task task = new Task();
            task.setId(rs.getLong("id"));
            task.setTaskName(rs.getString("task_name"));
            task.setAssigneeId(rs.getLong("assignee_id"));
            task.setPassword(rs.getString("password"));
            task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            task.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return task;
        }
    }
}