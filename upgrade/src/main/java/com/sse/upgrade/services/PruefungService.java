package com.sse.upgrade.services;

import com.sse.upgrade.model.Pruefung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PruefungService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PruefungService(JdbcTemplate temp) {
        this.jdbcTemplate = temp;
    }


    @Transactional(readOnly = true)
    public Pruefung getPruefungById(int id) {
        return jdbcTemplate.queryForObject(
                "select * from pruefung where id=?", new PruefungRowMapper(), id);
    }


    @Transactional
    public boolean anmelden(int studentID, int pruefungID){
       // String sql = "INSERT INTO teilnehmer (pruefung_id, user_id, note, comment) VALUES (pruefungID, studentID, 'NULL','NULL')";
        String sql = "INSERT INTO teilnehmer VALUES (pruefungID, studentID, 'NULL','NULL' where pruefung_id = ? and user_Id=?)";
        try{
            jdbcTemplate.update(sql, Pruefung.class);
            return true;
        } catch(Exception e){
            return false;
        }
    }
    @Transactional
    public boolean abmelden(int studentID, int pruefungID){
        Pruefung pruefung = getPruefungById(pruefungID);

        if(pruefung.abmeldbarCheck()) {
            pruefung.abmeldbarCheck();
            try {
                String sql = "DELETE FROM teilnehmer WHERE pruefung_id = ? and user_Id=?";
                return jdbcTemplate.update(sql, pruefungID, studentID) == 1;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public class PruefungRowMapper implements RowMapper<Pruefung> {
        @Override
        public Pruefung mapRow(ResultSet rs, int rowNum) throws SQLException {


            String dueDate = rs.getString("due_date");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat.parse(dueDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long time = date.getTime();
            Timestamp timestamp = new Timestamp(time);

            return new Pruefung(rs.getInt("id"),rs.getString("kurs"),rs.getInt("dozent"),timestamp);
        }


    }

}
