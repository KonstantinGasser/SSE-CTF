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
import java.util.List;

@Service
public class NotenService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NotenService(JdbcTemplate temp) {this.jdbcTemplate = temp;}

    /*
    Fetch Noten based on user ID
    returns List<Noten>
     */
    @Transactional(readOnly = true)
    public List<Note> getUserNoten(String ID) {
        String sql = "select pruefung.kurs, pruefung.dozent, teilnehmer.note, teilnehmer.comment " +
                "from teilnehmer " +
                "left join pruefung on teilnehmer.pruefung_id=pruefung.id " +
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
}
