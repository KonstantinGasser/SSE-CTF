package com.sse.upgrade.services;

import com.sse.upgrade.model.Note;
import com.sse.upgrade.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotenService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NotenService(JdbcTemplate temp) {this.jdbcTemplate = temp;}

    @Transactional(readOnly = true)
    public List<String> getNotenByProf(String ID) {
        String sql = "select pruefung.kurs from pruefung where pruefung.dozent=?";
        return this.jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString(1);
            }
        }, ID);
    }
    /*
    Fetch Noten based on user ID
    returns List<Noten>
     */
    @Transactional(readOnly = true)
    public List<Note> getUserNoten(String ID) {
        String sql = "select pruefung.kurs, hs_user.username, teilnehmer.note, teilnehmer.comment " +
                "from teilnehmer " +
                "inner join pruefung on teilnehmer.pruefung_id=pruefung.id " +
                "inner join hs_user on pruefung.dozent=hs_user.id " +
                "where teilnehmer.user_id=?";

        return this.jdbcTemplate.query(sql, new NoteRowMapper(), ID);
    }

    /*
    mapper for this sql:
    select pruefung.kurs, pruefung.dozent, teilnehmer.note, teilnehmer.comment
        from teilnehmer
        inner join pruefung on teilnehmer.pruefung_id=pruefng.id
        where teilnehmer.user_id=3;
     */
    class NoteRowMapper implements RowMapper {
        @Override
        public Note mapRow(ResultSet res, int i) throws SQLException {
            return new Note(
                    res.getString(1),
                    res.getString(2),
                    res.getDouble(3),
                    res.getString(4)
            );

        }
    }

    public Boolean updateNote(String student, String comment, double note, String kurs) {
        String sql = "update teilnehmer set note=?, comment=? where user_id=? and pruefung_id=?";

        try {
            this.jdbcTemplate.update(sql, note, comment, student, kurs);
        } catch(org.springframework.dao.DataAccessException err) {
            System.out.println(err);
            return false;
        }
        return true;
    }

    /*
    Returns list of all students part of a pruefung
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPruefunAndStuden(String ID) {
        String sql = "select hs_user.username as u, hs_user.id as uID, teilnehmer.comment as c, teilnehmer.note as n from teilnehmer" +
                " inner join hs_user on teilnehmer.user_id=hs_user.id" +
                " where teilnehmer.pruefung_id=?";
        return this.jdbcTemplate.queryForList(sql, ID);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPruefungProf(int ID) {
        System.out.println(ID);
        String sql = "select * from pruefung where dozent=?";
        return this.jdbcTemplate.queryForList(sql, ID);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getQuery(String query, int uuid) {
        String sql = "select hs_user.username as user, pruefung.kurs as name, t.user_id as teilID, t.note as note, pruefung.id as pruefung_id from pruefung " +
                "inner join hs_user on pruefung.dozent=hs_user.id " +
                "left join (select  user_id, note, pruefung_id from teilnehmer where user_id = "+uuid+") as t on pruefung.id=t.pruefung_id " +
                "where hs_user.username = '"+query+"' or pruefung.kurs = '"+query+"'";
        List<Map<String, Object>> res = new ArrayList<>();
        for(Map<String, Object> m : this.jdbcTemplate.queryForList(sql)) {
            if (m.get("teilID") == null) {
                m.put("isAngemeldet", "0");
            } else if(m.get("note") == null){
                m.put("isAngemeldet", "1");
            } else {
                m.put("isAngemeldet", "2");
            }
            res.add(m);
        }
        return res;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPruefungAndAngemelded(int ID) {
        String sql = "select hs_user.username as user, pruefung.kurs as name, t.user_id as teilID, t.note as note, pruefung.id as pruefung_id from pruefung " +
                "inner join hs_user on pruefung.dozent=hs_user.id " +
                "left join (select  user_id, note, pruefung_id from teilnehmer where user_id = "+ID+") as t on pruefung.id=t.pruefung_id ";
        List<Map<String, Object>> res = new ArrayList<>();
        for(Map<String, Object> m : this.jdbcTemplate.queryForList(sql)) {
            if (m.get("teilID") == null) {
                m.put("isAngemeldet", "0");
            } else if(m.get("note") == null){
                m.put("isAngemeldet", "1");
            } else {
                m.put("isAngemeldet", "2");
            }
            res.add(m);
        }
        return res;
    }
}
