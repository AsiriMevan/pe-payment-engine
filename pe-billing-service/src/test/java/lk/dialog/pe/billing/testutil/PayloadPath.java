package lk.dialog.pe.billing.testutil;

public enum PayloadPath {
    GET_PE_PROFILE("query-pe-profile-controller", "get-pe-profile.json"),
    QUERY_RBM_PROFILE("query-rbm-profile-controller", "query-rbm-profile.json"),
    GET_OCS_PROFILE("query-ocs-profile-controller", "get-ocs-profile.json"),
    GET_HOTLINE_REMARKS("hotline-remarks-controller", "get-hotline-remarks.json");

    private String[] path;

    PayloadPath(String... path) {
        this.path = path;
    }

    public String[] getPath() {
        return path;
    }
}
