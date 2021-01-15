-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlNoDataSourceInspection,SqlDialectInspection

INSERT INTO hs_user (id, username, hs_id, password_hash, role) VALUES
    (0, 'Student', 'ST0', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'student'), -- password
    (1, 'Luis', 'ST1', '936a185caaa266bb9cbe981e9e05cb78cd732b0b3280eb944412bb6f8f8f07af', 'student'), -- helloworld
    (2, 'Joni', 'PR0', '936a185caaa266bb9cbe981e9e05cb78cd732b0b3280eb944412bb6f8f8f07af', 'professor'), -- helloworld
    (3, 'Tino', 'PR1', 'd694f5fddff440905653ebd4569239e67563b01bfaab31ec87f5758efb61523a', 'professor'), -- thisissecure
    (4, 'Dobby', 'PU1', 'd694f5fddff440905653ebd4569239e67563b01bfaab31ec87f5758efb61523a', 'pruefungsamt'), -- thisissecure
    (5, 'user', '000', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'pruefungsamt'); -- password



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
    (2, 1, 2.0, 'okaish lol');


