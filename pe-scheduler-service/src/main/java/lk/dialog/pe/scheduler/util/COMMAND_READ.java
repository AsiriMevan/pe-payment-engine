package lk.dialog.pe.scheduler.util;

public enum COMMAND_READ {
    S("1"),
    P("1"),
    N("2"),
    F("2"),
    W("2");

    private String errorCode;

    private COMMAND_READ(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public boolean equals(COMMAND_READ commandRead){
        return this.name() == commandRead.name();
    }

}
