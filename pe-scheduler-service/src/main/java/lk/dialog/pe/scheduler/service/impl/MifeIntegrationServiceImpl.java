package lk.dialog.pe.scheduler.service.impl;

import java.io.IOException;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.RestExecutor;
import lk.dialog.pe.scheduler.dto.OCSTransaction;
import lk.dialog.pe.scheduler.service.MifeIntegrationService;
import lk.dialog.pe.credit.cam.dto.CcbsMinPayResponse;
import lk.dialog.pe.credit.cam.dto.MiscellaneousChargeRequest;
import lk.dialog.pe.credit.cam.dto.MiscellaneousChargeResponse;
import lk.dialog.pe.credit.cam.service.common.MifeApiService;
import lk.dialog.pe.credit.cam.util.MifeUrlEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class MifeIntegrationServiceImpl implements MifeIntegrationService {

    @Autowired
    MifeApiService mifeApiService;

    @Autowired
    RestExecutor restExecutor;

    @Value("${ocs.transaction-url}")
    String ocsUrl;
    @Override
    public CcbsMinPayResponse getCcbsMinPayment(String connectionReference, String traceId)
        throws DCPEException, IOException {
        log.info("getCcbsMinPaymentRequest connectionReferance={}|traceId={}",connectionReference,traceId);

        CcbsMinPayResponse ccbsMinPayResponse = mifeApiService.getWithUriParams(MifeUrlEnum.MIN_PAY_Service, Collections.singletonMap("refAccount",connectionReference),traceId);

        if(ccbsMinPayResponse==null){
            log.error("getCcbsMinPaymentResponse Failure connectionReferance={}|traceId={}",connectionReference,traceId);
            return null;
        }
        log.info("getCcbsMinPaymentResponse Success connectionReferance={}|traceId={}",connectionReference,traceId);
        return ccbsMinPayResponse;
    }

    @Override
    public MiscellaneousChargeResponse addMiscellaneousCharge(MiscellaneousChargeRequest miscellaneousChargeRequest, String traceId) throws DCPEException {
        log.info("addMiscellaneousChargeRequest miscellaneousChargeRequest={}|traceId={}",miscellaneousChargeRequest.toString(),traceId);

        MiscellaneousChargeResponse miscellaneousChargeResponse = mifeApiService.post(MifeUrlEnum.ADD_MISCELLANEOUS_CHARGES,miscellaneousChargeRequest,traceId);

        if(miscellaneousChargeResponse==null){
            log.error("addMiscellaneousChargeResponse Failure traceId={}",traceId);
            return null;
        }
        log.info("addMiscellaneousChargeResponse Success miscellaneousChargeResponse={}|traceId={}",miscellaneousChargeResponse.toString(),traceId);
        return miscellaneousChargeResponse;
    }

    @Override
    public String submitOcsTransactionRequest(OCSTransaction ocsRequest, String traceId){
        String url = ocsUrl;
        url = url.replace("{p_serialno}",ocsRequest.getSerialNo())
                .replace("{p_msisdn}",ocsRequest.getMsisdn())
                .replace("{p_requestaction}",ocsRequest.getAction())
                .replace("{p_amount}",ocsRequest.getAmount())
                .replace("{p_reasoncode}",ocsRequest.getReasonCode())
                .replace("{p_reasondescription}",ocsRequest.getDescription());

        log.info("submitOcsTransactionRequest  traceId={}|uri=[{}]",url,traceId);

        String ocsResponse = null;
        Object object=null;
        try {
         object = restExecutor.getRequest(url,traceId,String.class);

        } catch (DCPEException e) {
            log.error("submitRequestResponse Failure traceId={} | error",traceId,e);
            return null;
        }
        log.info("submitRequestResponse Success ocsResponse={}|traceId={}",object,traceId);
        return ocsResponse;


    }
}
