CREATE TABLE appusers(
       username VARCHAR(255) NOT NULL UNIQUE,
       password TEXT NOT NULL
);

CREATE TABLE problem(
       problem_id VARCHAR(255) NOT NULL UNIQUE,
       t_limit_sec INT NOT NULL,
       m_limit_mb INT NOT NULL,
       tests JSONB
);

CREATE TABLE submit(
       submit_id SERIAL,
       problem_id VARCHAR(255),
       code TEXT NOT NULL,
       lang VARCHAR(31) NOT NULL,
       status VARCHAR(255) NOT NULL,
       score INT NOT NULL,
       CONSTRAINT fk_problem
                   FOREIGN KEY(problem_id)
                           REFERENCES problem(problem_id)
);
