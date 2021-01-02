-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- CREATE TABLE pruefung (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(250) NOT NULL
-- );

-- CREATE TABLE note (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     pruefung_id INT,
--     note INT NOT NULL,
--     comment VARCHAR(500),
--     FOREIGN KEY(pruefung_id) REFERENCES pruefung(id)
-- );


CREATE TABLE hs_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hs_id VARCHAR(3),
    username VARCHAR(25),
    password_hash VARCHAR(128)
);


CREATE TABLE pruefung (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kurs VARCHAR(5),
    dozent INT,
    due_date TIMESTAMP,
    FOREIGN KEY(dozent) REFERENCES hs_user(id)
);


CREATE TABLE teilnehmer (
    pruefung_id INT,
    user_id INT,
    note DECIMAL ,
    FOREIGN KEY(pruefung_id) REFERENCES pruefung(id),
    FOREIGN KEY(user_id) REFERENCES hs_user(id)
);




