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

@Service
public class PruefungService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PruefungService(JdbcTemplate temp) {
        this.jdbcTemplate = temp;
    }


    @Transactional
    public Pruefung getPruefungByID(int prID){
        String sql= "select (id, kurs, dozent, due_date) from pruefung where id = " + prID;
        System.out.println("DeBug3");
        System.out.println(jdbcTemplate.queryForObject(sql, Pruefung.class).toString());
        return jdbcTemplate.queryForObject(sql, Pruefung.class);

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
        System.out.println("DeBug1");
        String sql = "DELETE FROM teilnehmer WHERE pruefung_id = ? and user_Id=?";
        System.out.println("DeBug2");
        Pruefung pruefung = getPruefungByID(pruefungID);

        if(pruefung.abmeldbarCheck()) {
            //TODO pruefung auf abmeldbarkeit checken
            /* dazu: gmethode getpruefung by ID */
            pruefung.abmeldbarCheck();


            try {
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

            int id= rs.getInt("id");
            String kurs = rs.getString("kurs");
            int dozent = rs.getInt("dozent");

            String dueDate = rs.getString("due_date");
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = dateFormat.parse("pruefungsZeit");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long time = date.getTime();
            Timestamp timestamp = new Timestamp(time);

            return new Pruefung(id,kurs,dozent,timestamp);
        }


    }

}
