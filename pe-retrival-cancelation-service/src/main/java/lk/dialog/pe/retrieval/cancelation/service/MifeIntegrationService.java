package lk.dialog.pe.retrieval.cancelation.service;

import java.text.ParseException;
import java.util.List;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.retrieval.cancelation.domain.PaymentDTO;
import lk.dialog.pe.retrieval.cancelation.util.QueryPaymentsSummery;

public interface MifeIntegrationService {

	public List<PaymentDTO> queryPaymentsSummery(QueryPaymentsSummery queryPaymentsSummery,String receiptNo,String traceId)throws DCPEException, ParseException;

}
