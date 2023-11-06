package lk.dialog.pe.scheduler.util;

import java.util.Arrays;

public enum CONNECTION_STATUS {
    C(1),
    D(2),
    B(3),
    T(4),
    NC(5);

    private int value;

    CONNECTION_STATUS(int value){
        this.value =value;
    }

    public CONNECTION_STATUS getType(String stringValue) {
        CONNECTION_STATUS rt = null;
        String upperCaseValue = stringValue.toUpperCase();
        for (CONNECTION_STATUS e : CONNECTION_STATUS.values()) {
            if (e.toString() == upperCaseValue) {
                rt = e;
                break;
            }
        }
        return rt;
    }

    public boolean orEqual(CONNECTION_STATUS input ,CONNECTION_STATUS[] compareWith){

        return Arrays.stream(compareWith).anyMatch(val-> val == input);
    }
}
