CREATE TABLE appusers(
       username VARCHAR(255) NOT NULL UNIQUE,
       password TEXT NOT NULL
);

CREATE TABLE problem(
       problem_id VARCHAR(255) NOT NULL UNIQUE,
       tests JSONB
);
