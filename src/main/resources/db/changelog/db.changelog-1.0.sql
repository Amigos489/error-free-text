--changeset balabol:1
CREATE TABLE IF NOT EXISTS task (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    language CHAR(2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    error_message VARCHAR(255) NOT NULL
)