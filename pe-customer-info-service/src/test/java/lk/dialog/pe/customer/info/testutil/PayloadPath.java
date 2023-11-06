package lk.dialog.pe.customer.info.testutil;

public enum PayloadPath {
    GET_CCBS_PROFILE("query-profile-controller", "get-ccbs-profile.json");

    private String[] path;

    PayloadPath(String... path) {
        this.path = path;
    }

    public String[] getPath() {
        return path;
    }
}
