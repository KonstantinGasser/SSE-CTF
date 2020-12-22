INSERT INTO pruefung (id, name) VALUES (0, 'PR1'), (1, 'PR2'), (2, 'SSE');

INSERT INTO note (pruefung_id, note, comment) VALUES
(0, 1, 'Sehr gut!'),
(1, 4, 'Threads müssen Sie nochmal üben.'),
(1, 2, null),
(2, 1, 'Das ist ein Comment');
