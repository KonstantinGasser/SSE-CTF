-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlNoDataSourceInspection,SqlDialectInspection

-- INSERT INTO pruefung (id, name) VALUES (0, 'PR1'), (1, 'PR2'), (2, 'SSE');
--
-- INSERT INTO note (pruefung_id, note, comment) VALUES
-- (0, 1, 'Sehr gut!'),
-- (1, 4, 'Threads müssen Sie nochmal üben.'),
-- (1, 2, null),
-- (2, 1, 'Das ist ein Comment');


INSERT INTO hs_user (id, username, hs_id, password_hash) VALUES
    (0, 'Niklas', 'ST0', 'fc5e038d38a57032085441e7fe7010b0'),
    (1, 'Luis', 'ST1', 'fc5e038d38a57032085441e7fe7010b0'),
    (2, 'Joni', 'PR0', 'fc5e038d38a57032085441e7fe7010b0'),
    (3, 'Tino', 'PR1', 'fc5e038d38a57032085441e7fe7010b0');



INSERT INTO pruefung (id, kurs, dozent, due_date) VALUES
    (0, 'SSE', 2, '2021-02-24'),
    (1, 'PR2', 3, '2021-02-24'),
    (2, 'BDEA', 2, '2021-02-24'),
    (3, 'VS', 3, '2021-02-24');

INSERT INTO teilnehmer (pruefung_id, user_id, note) VALUES
    (0, 0, NULL),
    (0, 1, NULL),
    (3, 0, 2.7),
    (3, 1, 1.3),
    (1, 0, 3.7),
    (2, 1, 2.0);


