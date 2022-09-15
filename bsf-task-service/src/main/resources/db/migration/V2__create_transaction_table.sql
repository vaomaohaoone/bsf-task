CREATE TABLE IF NOT EXISTS bsf.transaction
(
    id                      uuid            PRIMARY KEY,
    sum                     bigint          NOT NULL,
    type                    varchar(30),
    account_receiver_id     uuid,
    account_sender_id       uuid,
    created_on              timestamp(6),
    CONSTRAINT fk_receiver_account FOREIGN KEY (account_receiver_id) REFERENCES bsf.account (id) ON DELETE CASCADE,
    CONSTRAINT fk_sender_account FOREIGN KEY (account_sender_id) REFERENCES bsf.account (id) ON DELETE CASCADE
);