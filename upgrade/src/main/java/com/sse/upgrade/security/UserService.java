package com.sse.upgrade.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public User getUser(final int id) {
        String sql = "select * from user where id = '" + id + "'";
        return jdbcTemplate.queryForObject(sql, User.class);
    }

    @Transactional(readOnly = true)
    public User login(String username, String password) {
        // TODO: proper credentials check
        return new User("max", User.Role.PROFESSOR);
    }
}
