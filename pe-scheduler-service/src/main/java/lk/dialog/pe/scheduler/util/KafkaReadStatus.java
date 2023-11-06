package lk.dialog.pe.scheduler.util;

public enum KafkaReadStatus {
    YES("Y"), NO("N"), FAIL("F"), PROCESSING("P");

    private String codeRef;

    KafkaReadStatus(String code) {
        this.codeRef = code;
    }


    public String getValue() {
        return this.codeRef;
    }

}
