package com.sse.upgrade.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Pruefung {
    private final Timestamp pruefungsZeit;
    private final int ID;
    private final String name;


    public Pruefung (String name, int ID, Timestamp pruefungsZeit){
        this.name=name;
        this.pruefungsZeit=pruefungsZeit;
        this.ID=ID;
    }

//checkt, ob es aktuell mehr oder gleich 24 Std vor der Prüfung sind
public boolean abmeldbarCheck(){
    Instant now = Instant.now();
    return pruefungsZeit.toInstant().isBefore( now.minus( 24 , ChronoUnit.HOURS));
}

@Override
    public String toString(){
        return this.name;
}
}
