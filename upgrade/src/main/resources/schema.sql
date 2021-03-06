-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile


CREATE TABLE hs_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hs_id VARCHAR(25),
    username VARCHAR(25),
    password_hash VARCHAR(256),
    role VARCHAR(15)
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
    comment VARCHAR(250),
    FOREIGN KEY(pruefung_id) REFERENCES pruefung(id),
    FOREIGN KEY(user_id) REFERENCES hs_user(id)
);


-- select hs_user.username from teilnehmer left join pruefung on teilnehmer.pruefung_id=pruefung.id
