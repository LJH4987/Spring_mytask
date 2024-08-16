package com.example.mytask.repository;

import com.example.mytask.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.*;
import java.util.Optional;

@Repository
public class JdbcTaskRepository implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Task save(Task task) {
        String sql = "INSERT INTO Task (task_name, assignee_id, password, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, task.getTaskName());
            ps.setLong(2, task.getAssigneeId());
            ps.setString(3, task.getPassword());
            ps.setTimestamp(4, Timestamp.valueOf(task.getCreatedAt()));
            ps.setNull(5, java.sql.Types.TIMESTAMP);
            return ps;
        }, keyHolder);

        task.setId(keyHolder.getKey().longValue());

        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM Task WHERE id = ?";
        List<Task> result = jdbcTemplate.query(sql, new Object[]{id}, new TaskRowMapper());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Task> findAll(Pageable pageable) {
        String sql = "SELECT * FROM Task ORDER BY updated_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{pageable.getPageSize(), pageable.getOffset()}, new TaskRowMapper());
    }

    @Override
    public void update(Task task) {
        String sql = "UPDATE Task SET task_name = ?, assignee_id = ?, password = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, task.getTaskName(), task.getAssigneeId(), task.getPassword(), task.getUpdatedAt(), task.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Task WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Task> findAllByAssigneeId(Long assigneeId) {
        String sql = "SELECT * FROM Task WHERE assignee_id = ?";
        return jdbcTemplate.query(sql, new Object[]{assigneeId}, new TaskRowMapper());
    }

    @Override
    public List<Task> findAllByAssigneeNameAndModifiedDate(String assigneeName, String modifiedDate, Pageable pageable) {
        String sql;
        Object[] params;

        if (assigneeName != null && !assigneeName.isEmpty() && modifiedDate != null && !modifiedDate.isEmpty()) {
            sql = "SELECT t.* FROM Task t " +
                    "JOIN Assignee a ON t.assignee_id = a.id " +
                    "WHERE a.name = ? AND DATE(t.updated_at) = ? " +
                    "ORDER BY t.updated_at DESC LIMIT ? OFFSET ?";
            params = new Object[]{assigneeName, modifiedDate, pageable.getPageSize(), pageable.getOffset()};
        } else if (assigneeName != null && !assigneeName.isEmpty()) {
            sql = "SELECT t.* FROM Task t " +
                    "JOIN Assignee a ON t.assignee_id = a.id " +
                    "WHERE a.name = ? " +
                    "ORDER BY t.updated_at DESC LIMIT ? OFFSET ?";
            params = new Object[]{assigneeName, pageable.getPageSize(), pageable.getOffset()};
        } else if (modifiedDate != null && !modifiedDate.isEmpty()) {
            sql = "SELECT t.* FROM Task t " +
                    "WHERE DATE(t.updated_at) = ? " +
                    "ORDER BY t.updated_at DESC LIMIT ? OFFSET ?";
            params = new Object[]{modifiedDate, pageable.getPageSize(), pageable.getOffset()};
        } else {
            sql = "SELECT t.* FROM Task t " +
                    "ORDER BY t.updated_at DESC LIMIT ? OFFSET ?";
            params = new Object[]{pageable.getPageSize(), pageable.getOffset()};
        }

        return jdbcTemplate.query(sql, params, new TaskRowMapper());
    }

    @Override
    public List<Task> findAllByAssigneeName(String assigneeName, Pageable pageable) {
        String sql = "SELECT t.* FROM Task t " +
                "JOIN Assignee a ON t.assignee_id = a.id " +
                "WHERE a.name = ? " +
                "ORDER BY t.updated_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{assigneeName, pageable.getPageSize(), pageable.getOffset()}, new TaskRowMapper());
    }

    @Override
    public List<Task> findAllByModifiedDate(String modifiedDate, Pageable pageable) {
        String sql = "SELECT * FROM Task " +
                "WHERE DATE(updated_at) = ? " +
                "ORDER BY updated_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{modifiedDate, pageable.getPageSize(), pageable.getOffset()}, new TaskRowMapper());
    }

    private static class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            Task task = new Task();
            task.setId(rs.getLong("id"));
            task.setTaskName(rs.getString("task_name"));
            task.setAssigneeId(rs.getLong("assignee_id"));
            task.setPassword(rs.getString("password"));
            Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
            if (createdAtTimestamp != null) {
                task.setCreatedAt(createdAtTimestamp.toLocalDateTime());
            } else {
                task.setCreatedAt(null);
            }
            Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");
            if (updatedAtTimestamp != null) {
                task.setUpdatedAt(updatedAtTimestamp.toLocalDateTime());
            } else {
                task.setUpdatedAt(null);
            }
            return task;
        }
    }
}