package com.sse.upgrade.services;

import com.sse.upgrade.model.User;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import java.sql.PreparedStatement;

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
        String sql = "select * from hs_user where username = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), username);
        String pwHash = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        for ( User user : users) {
            if (user.getPassword().equals(pwHash) && user.getUsername().equals(username))
                return user;
        }
        throw new BadCredentialsException("Username or Password incorrect");
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
            String pw = resultSet.getString(4);
            switch (role) {
                case "pruefungsamt":
                    return new User(username, pw, User.Role.PRUEFUNGSAMT);
                case "professor":
                    return new User(username, pw, User.Role.PROFESSOR);
                default:
                    return new User(username, pw, User.Role.STUDENT);
            }
        }
    }
}
