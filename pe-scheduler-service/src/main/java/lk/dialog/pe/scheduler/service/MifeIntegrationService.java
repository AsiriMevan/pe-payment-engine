package lk.dialog.pe.scheduler.service;

import java.io.IOException;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.scheduler.dto.OCSTransaction;
import lk.dialog.pe.credit.cam.dto.CcbsMinPayResponse;
import lk.dialog.pe.credit.cam.dto.MiscellaneousChargeRequest;
import lk.dialog.pe.credit.cam.dto.MiscellaneousChargeResponse;

public interface MifeIntegrationService {

public CcbsMinPayResponse getCcbsMinPayment(String connectionReference, String traceId)
    throws DCPEException, IOException;

public MiscellaneousChargeResponse addMiscellaneousCharge(MiscellaneousChargeRequest miscellaneousChargeRequest, String traceId) throws DCPEException;;

public  String submitOcsTransactionRequest(OCSTransaction ocsRequest, String traceId);
}
