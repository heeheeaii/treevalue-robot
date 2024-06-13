CREATE TABLE IF NOT EXISTS tvr_resource_description
(
    id         BIGSERIAL PRIMARY KEY,
    resourceId VARCHAR(24)      NOT NULL,
    condition  TEXT[]           NOT NULL,
    result     TEXT             NOT NULL,
    price      DOUBLE PRECISION NOT NULL,
    valuation  DOUBLE PRECISION NOT NULL
);



INSERT INTO tvr_resource_description (resourceId, condition, result, price, valuation)
VALUES ('001', ARRAY ['condition1', 'condition2'], 'result1', 100.50, 150.75),
       ('002', ARRAY ['condition3', 'condition4'], 'result2', 200.75, 300.25),
       ('003', ARRAY ['condition5', 'condition6'], 'result3', 300.25, 400.50);

CREATE TABLE IF NOT EXISTS tvr_users
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL CHECK (name <> ''),
    pwd   VARCHAR(50)  NOT NULL CHECK (pwd <> ''),
    email varchar(30)  NOT NULL CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$')
);

CREATE TABLE IF NOT EXISTS tvr_resource_feedback
(
    id           BIGSERIAL PRIMARY KEY,
    resource_id  VARCHAR(24) NOT NULL,
    feedback     TEXT        NOT NULL,
    from_user_id BIGINT      NOT NULL,
    FOREIGN KEY (from_user_id) REFERENCES tvr_users (id)
);
