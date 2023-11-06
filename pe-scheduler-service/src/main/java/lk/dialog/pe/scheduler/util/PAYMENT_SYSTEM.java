package lk.dialog.pe.scheduler.util;

public enum PAYMENT_SYSTEM {
    RBM,
    RBM_CHQ,
    TELBIZ,
    GSM;

    PAYMENT_SYSTEM(){}

    public PAYMENT_SYSTEM getType(String stringValue){
        PAYMENT_SYSTEM rt = null;
        String upperCaseValue = stringValue.toUpperCase();
        for (PAYMENT_SYSTEM e : PAYMENT_SYSTEM.values()) {
            if (e.toString() == upperCaseValue) {
                rt = e;
                break;
            }
        }
        return rt;
    }



}
