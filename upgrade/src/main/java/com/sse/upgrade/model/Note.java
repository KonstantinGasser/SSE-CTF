package com.sse.upgrade.model;

import com.sse.upgrade.services.NotenService;

public class Note {
    private String kurs;
    private String prof;
    private double note;
    private String comment;

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getKurs() {
        return kurs;
    }

    public void setKurs(String kurs) {
        this.kurs = kurs;
    }

    public Note(String kurs, String prof, double note, String comment) {
        this.kurs = kurs;
        this.prof = prof;
        this.note = note;
        this.comment = comment;
    }
    public Note() {}

}
