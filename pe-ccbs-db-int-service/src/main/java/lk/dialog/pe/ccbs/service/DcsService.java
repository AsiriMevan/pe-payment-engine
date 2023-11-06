package lk.dialog.pe.ccbs.service;

import lk.dialog.pe.ccbs.dto.DcsProfile;
import lk.dialog.pe.common.exception.DCPEException;

import java.util.List;

public interface DcsService {
    List<DcsProfile> getDcsMobileByContract(String contractId, String traceId) throws DCPEException;
}
