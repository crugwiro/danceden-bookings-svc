CREATE TABLE offering (
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(255)   NOT NULL,
    type          VARCHAR(20)    NOT NULL,
    description   TEXT,
    instructor    VARCHAR(255)   NOT NULL,
    price         NUMERIC(10, 2) NOT NULL,
    capacity      INTEGER        NOT NULL,
    start_date    DATE           NOT NULL,
    end_date      DATE           NOT NULL,
    status        VARCHAR(20)    NOT NULL
);
