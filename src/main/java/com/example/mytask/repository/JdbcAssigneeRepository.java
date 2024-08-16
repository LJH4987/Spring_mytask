package com.example.mytask.repository;

import com.example.mytask.model.Assignee;
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

@Repository
public class JdbcAssigneeRepository implements AssigneeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAssigneeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Assignee save(Assignee assignee) {
        String sql = "INSERT INTO Assignee (name, email, created_at, updated_at) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, assignee.getName());
            ps.setString(2, assignee.getEmail());
            ps.setTimestamp(3, Timestamp.valueOf(assignee.getCreatedAt()));
            ps.setTimestamp(4, Timestamp.valueOf(assignee.getUpdatedAt()));
            return ps;
        }, keyHolder);

        assignee.setId(keyHolder.getKey().longValue());

        return assignee;
    }

    @Override
    public Optional<Assignee> findById(Long id) {
        String sql = "SELECT * FROM Assignee WHERE id = ?";
        List<Assignee> result = jdbcTemplate.query(sql, new Object[]{id}, new AssigneeRowMapper());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Assignee> findAll() {
        String sql = "SELECT * FROM Assignee";
        return jdbcTemplate.query(sql, new AssigneeRowMapper());
    }

    @Override
    public Assignee update(Assignee assignee) {
        String sql = "UPDATE Assignee SET name = ?, email = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, assignee.getName(), assignee.getEmail(), assignee.getUpdatedAt(), assignee.getId());
        return assignee;
    }

    @Override
    public void deleteById(Long id) {
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