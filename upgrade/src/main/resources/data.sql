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

-- password is a md5 hash of 'helloworld' :)
INSERT INTO hs_user (id, username, hs_id, password_hash, role) VALUES
    (0, 'Niklas', 'ST0', 'fc5e038d38a57032085441e7fe7010b0', 'student'),
    (1, 'Luis', 'ST1', 'fc5e038d38a57032085441e7fe7010b0', 'student'),
    (2, 'Joni', 'PR0', 'fc5e038d38a57032085441e7fe7010b0', 'professor'),
    (3, 'Tino', 'PR1', 'fc5e038d38a57032085441e7fe7010b0', 'professor');



INSERT INTO pruefung (id, kurs, dozent, due_date) VALUES
    (0, 'SSE', 2, '2021-02-24'),
    (1, 'PR2', 3, '2021-02-24'),
    (2, 'BDEA', 2, '2021-02-24'),
    (3, 'VS', 3, '2021-02-24');

INSERT INTO teilnehmer (pruefung_id, user_id, note, comment) VALUES
    (0, 0, NULL, NULL),
    (0, 1, NULL, NULL),
    (3, 0, 2.7, 'good one'),
    (3, 1, 1.3, 'nice job'),
    (1, 0, 3.7, 'well well well'),
    (2, 1, 2.0, 'okaisch lol');


