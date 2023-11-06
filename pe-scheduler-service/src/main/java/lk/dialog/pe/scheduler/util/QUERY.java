package lk.dialog.pe.scheduler.util;

public enum QUERY {
    SQL_SEL_RBM_PAYMENT(1),
    SQL_UPDATE_RBM_PAYMENT(2),
    SQL_UPDATE_RBM_PAYMENT_RES(3),
    SQL_UPDATE_RBM_NO_CX_FOUND(4),

    SQL_SEL_RBM_FAIL_PAYMENT(24),
    SQL_UPDATE_RBM_FAIL_PAYMENT(25),
    SQL_UPDATE_RBM_PAYMENT_FAIL_RES(27),

    SQL_SEL_OCS_PAYMENT(5),
    SQL_UPDATE_OCS_PAYMENT(6),
    SQL_UPDATE_OCS_PAYMENT_RES(7),
    SQL_DEL_OCS_PAYMENT(30),

    SQL_SEL_DBN_PAYMENT(8),
    SQL_UPDATE_DBN_PAYMENT(9),
    SQL_UPDATE_DBN_PAYMENT_RES(10),
    SQL_UPDATE_DBN_PAYMENT_FAIL_RES(11),

    SQL_SEL_DBN_FAIL_PAYMENT(26),

    SQL_SEL_FORCEFUL_CHQ_PARTIAL(13),
    SQL_SEL_FORCEFUL_PEND_SUS_CHQ(14),
    SQL_SEL_FORCEFUL_CHQ_FULL(15),
    SQL_UPDATE_FORCEFUL_CHQ_READ(16),
    SQL_UPDATE_FORCEFUL_CHQ(17),
    SQL_UPDATE_FORCEFUL_CHQ_NO_CX_FOUND(18),
    SQL_UPDATE_FORCEFUL_CHQ_FAIL_RES(19),

    SQL_SEL_FAIL_FORCEFUL_PEND_SUS_CHQ(24),
    SQL_UPDATE_FAIL_FORCEFUL_CHQ(25),

    SQL_SEL_CANCEL_PAYMENT(20),
    SQL_UPDATE_CANCEL_PAYMENT_READ(21),
    SQL_UPDATE_CANCEL_PAYMENT_RES(22),
    SQL_UPDATE_CANCEL_FAIL_PAYMENT_RES(23),

    SQL_SEL_KAFKA_PAYMENT(28),
    SQL_UPDATE_KAFKA_PAYMENT(29);

    private int value;

    private QUERY(int val) {
        this.value = val;
    }

    public int valueOf() {
        return this.value;
    }

    public static QUERY getQUERY(int value) {
        QUERY rt = null;
        for (QUERY e : QUERY.values()) {
            if (e.valueOf() == value) {
                rt = e;
                break;
            }
        }
        return rt;
    }

    public String getKey() {
        return this.toString();
    }
}
