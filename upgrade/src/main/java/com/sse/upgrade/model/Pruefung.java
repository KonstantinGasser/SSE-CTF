package com.sse.upgrade.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Pruefung {
    private final GregorianCalendar pruefungsZeit;
    private final int ID;
    private final String name;


    public Pruefung (String name, int ID, GregorianCalendar pruefungsZeit){
        this.name=name;
        this.pruefungsZeit=pruefungsZeit;
        this.ID=ID;
    }

//checkt, ob es aktuell weniger als 24h vor der Prüfung sind
public boolean abmeldbarCheck(){
    Instant now = Instant.now();
    boolean zuspaet =
            ( ! pruefungsZeit.toInstant().isBefore( now.minus( 24 , ChronoUnit.HOURS) ) )
                    &&
                    ( pruefungsZeit.toInstant().isBefore( now )
                    ) ;
    if(zuspaet){
        return false;
    }
    return true;
}

@Override
    public String toString(){
        return this.name;
}




}
