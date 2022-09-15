CREATE TABLE IF NOT EXISTS bsf.account
(
    id          uuid           PRIMARY KEY,
    name        varchar        NOT NULL,
    balance_sum bigint         NOT NULL default 0,
    user_id     varchar        NOT NULL,
    UNIQUE (name, user_id)
);