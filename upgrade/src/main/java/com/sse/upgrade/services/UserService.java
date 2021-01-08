package com.sse.upgrade.services;

import com.sse.upgrade.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
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
        String sql = "select * from hs_user where username = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), username);
        String pwHash = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        for (User user : users) {
            if (user.getPassword().equals(pwHash) && user.getUsername().equals(username))
                return user;
        }
        throw new BadCredentialsException("Username or Password incorrect");
    }

    @Transactional
    public Boolean register(String username, String hs_id, String role, String password) {
        try {
            String pwHash = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
            jdbcTemplate.update("insert into hs_user(hs_id, username, password_hash, role) values(?,?,?,?)", hs_id, username, pwHash, role);
        } catch (org.springframework.dao.DataAccessException err) {
            return false;
        }
        return true;
    }

    @Transactional
    public boolean changePassword(String oldPw, String newPw, int userID) {
        String sql = "SELECT * FROM hs_user WHERE id=?";
        try {
            List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), userID);
            if (DigestUtils.md5DigestAsHex(oldPw.getBytes(StandardCharsets.UTF_8)).equals(users.get(0).getPassword())) {
                String newPwHash = DigestUtils.md5DigestAsHex(newPw.getBytes(StandardCharsets.UTF_8));
                jdbcTemplate.update("UPDATE hs_user SET password_hash = ? WHERE id=?", newPwHash, userID);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return jdbcTemplate.query("select * from hs_user", new UserRowMapper());
    }

    class UserRowMapper implements RowMapper {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt(1);

            String username = resultSet.getString(3);
            String role = resultSet.getString(5);
            String pw = resultSet.getString(4);
            switch (role) {
                case "pruefungsamt":
                    return new User(id, username, pw, User.Role.PRUEFUNGSAMT);
                case "professor":
                    return new User(id, username, pw, User.Role.PROFESSOR);
                default:
                    return new User(id, username, pw, User.Role.STUDENT);
            }
        }
    }


    @Transactional(readOnly = true)
    public List<User> getAllDozenten() {
        return jdbcTemplate.query("select * from hs_user where role = 'professor'", new DozentenRowMapper());
    }

    class DozentenRowMapper implements RowMapper {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            String role = resultSet.getString(5);
            String dozentenName = resultSet.getString(3);
            String pw = resultSet.getString(4);
            int id = resultSet.getInt(1);

            return new User(id, dozentenName, pw, User.Role.PROFESSOR);
        }
    }


    public User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
