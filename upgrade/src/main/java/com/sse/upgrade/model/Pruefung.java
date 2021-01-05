package com.sse.upgrade.model;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.sse.upgrade.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pruefung {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Id
    private final int ID;
    private final String kurs;
    private final int dozent;
    private final Timestamp pruefungsZeit;



    public Pruefung( int ID, String kurs, int dozent, Timestamp pruefungsZeit){
        this.kurs=kurs;
        this.pruefungsZeit=pruefungsZeit;
        this.ID=ID;
        this.dozent=dozent;
    }

    @Transactional(readOnly = true)
    public Pruefung getPruefung(final int id) {
        String sql = "select * from pruefung where id = '" + ID + "'";
        return jdbcTemplate.queryForObject(sql, Pruefung.class);
    }



    //checkt, ob es aktuell mehr oder gleich 24 Std vor der Prüfung sind
    public boolean abmeldbarCheck(){
        Instant now = Instant.now();
//    boolean zuspaet =
//            (pruefungsZeit.toInstant().isBefore( now.minus( 24 , ChronoUnit.HOURS) ));
        return (pruefungsZeit.toInstant().isBefore( now.minus( 24 , ChronoUnit.HOURS)));
    }





    @Override
    public String toString(){
        return this.kurs + this.dozent + this.pruefungsZeit;
    }


    //getters


    public int getID() {
        return ID;
    }

    public String getKurs() {
        return kurs;
    }

    public int getDozent() {
        return dozent;
    }

    public Timestamp getPruefungsZeit() {
        return pruefungsZeit;
    }
}
