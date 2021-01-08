package com.sse.upgrade.model;

import org.springframework.data.annotation.Id;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
        return pruefungsZeit.toInstant().isBefore( now.minus( 24 , ChronoUnit.HOURS));
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

    public String getPruefungsZeit() {
        return pruefungsZeit.toString().split(" ")[0];
    }
}
