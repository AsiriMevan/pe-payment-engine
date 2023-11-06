package lk.dialog.pe.retrieval.cancelation.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.CancelPaymentRequest;

public interface CancelPaymentService {

	public String getMobileOfContractId(String contractId, String traceId) throws DCPEException;
	public Long getNextSeqStr (long trxId, String traceId) throws DCPEException;
	public void lodgePaymentCancel(CancelPaymentRequest payRequest, long trxId, String traceId) throws DCPEException;
	public void lodgeOCSPayment (CancelPaymentRequest payRequest, long trxId, String traceId) throws DCPEException;

}
