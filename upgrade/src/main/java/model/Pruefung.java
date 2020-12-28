package model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class Pruefung {
    private Date pruefungsZeit;
    private int ID;
    private String name;


    public Pruefung (String name, int ID, Date datum){
        this.name=name;
        this.pruefungsZeit=datum;
                this.ID=ID;
    }

//checkt, ob es aktuell weniger als 24h vor der Prüfung sind
public boolean abmeldbarCheck(){
    Instant now = Instant.now();
    Boolean zuspät =
            ( ! pruefungsZeit.toInstant().isBefore( now.minus( 24 , ChronoUnit.HOURS) ) )
                    &&
                    ( pruefungsZeit.toInstant().isBefore( now )
                    ) ;
    if(zuspät){
        return false;
    }
    return true;
}




}
