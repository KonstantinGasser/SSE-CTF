CREATE TABLE pruefung (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL
);

CREATE TABLE note (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pruefung_id INT,
    note INT NOT NULL,
    comment VARCHAR(500),
    FOREIGN KEY(pruefung_id) REFERENCES pruefung(id)
);

