package com.example.mytask.repository;

import com.example.mytask.model.Assignee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcAssigneeRepository implements AssigneeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAssigneeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Assignee assignee) {
        String sql = "INSERT INTO Assignee (name, email, created_at, updated_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, assignee.getName(), assignee.getEmail(), assignee.getCreatedAt(), assignee.getUpdatedAt());
    }

    @Override
    public Optional<Assignee> findById(int id) {
        String sql = "SELECT * FROM Assignee WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new AssigneeRowMapper())
                .stream()
                .findFirst();
    }

    @Override
    public List<Assignee> findAll() {
        String sql = "SELECT * FROM Assignee ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new AssigneeRowMapper());
    }

    @Override
    public void update(Assignee assignee) {
        String sql = "UPDATE Assignee SET name = ?, email = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, assignee.getName(), assignee.getEmail(), assignee.getUpdatedAt(), assignee.getId());
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM Assignee WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class AssigneeRowMapper implements RowMapper<Assignee> {
        @Override
        public Assignee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Assignee assignee = new Assignee();
            assignee.setId(rs.getLong("id"));
            assignee.setName(rs.getString("name"));
            assignee.setEmail(rs.getString("email"));
            assignee.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            assignee.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return assignee;
        }
    }
}
