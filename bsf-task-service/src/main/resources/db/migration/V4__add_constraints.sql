ALTER TABLE IF EXISTS bsf.account
    add constraint balance_non_negative check (bsf.account.balance_sum >= 0);

ALTER TABLE IF EXISTS bsf.transaction
    add constraint sum_non_negative check (bsf.transaction.sum >= 0);