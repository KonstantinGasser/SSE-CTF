package com.sse.upgrade.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Eine Beispiel Component mit Datenbank zugriff
 */
@Component
public class BusinessLogik {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> databaseZugriff(String name) {
        // VORSICHT! hier ist eine SQL Injection möglich
        String sql = "select * from note inner join pruefung on note.pruefung_id = pruefung.id where pruefung.name = '"+name+"'";
        System.out.println(sql);
        //System.out.println(jdbcTemplate.queryForList(sql));
        return jdbcTemplate.queryForList(sql);
    }
}
