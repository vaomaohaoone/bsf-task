CREATE TABLE IF NOT EXISTS bsf.account
(
    id          uuid           PRIMARY KEY,
    version     int            NOT NULL DEFAULT 0,
    name        varchar        NOT NULL,
    balance_sum int            NOT NULL default 0,
    user_id     varchar        NOT NULL,
    UNIQUE (name, user_id)
);