package lk.dialog.pe.scheduler.util;

public enum HANDLER {
    RBM_PAY(1),
    OCS_PAY(2),
    DBN_PAY(3),
    CHEQUE_FORCEFUL_PAY(4),
    CANCEL_PAY(5),

    RBM_PAY_RETRY(6),
    DBN_PAY_RETRY(7),
    CHEQUE_FORCEFUL_PAY_RETRY(8);
    private int value;

    private HANDLER(int val) {
        this.value = val;
    }

    public int valueOf() {
        return this.value;
    }

    public static QUERY getJOB(int value) {
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
