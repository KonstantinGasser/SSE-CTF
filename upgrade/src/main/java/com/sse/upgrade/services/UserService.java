package com.sse.upgrade.services;

import com.sse.upgrade.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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


    public Boolean register(String username, String hs_id, String role, String password) {
        try {
            jdbcTemplate.update("insert into hs_user(hs_id, username, password_hash, role) values(?,?,?,?)", hs_id, username, password, role);
        } catch(org.springframework.dao.DataAccessException err) {
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return jdbcTemplate.query("select * from hs_user", new UserRowMapper());
    }


    class UserRowMapper implements RowMapper {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            String username = resultSet.getString(3);
            String role = resultSet.getString(5);
            switch (role) {
                case "professor":
                    return new User(username, User.Role.PROFESSOR);
                default:
                    return new User(username, User.Role.STUDENT);
            }
        }
    }
}
