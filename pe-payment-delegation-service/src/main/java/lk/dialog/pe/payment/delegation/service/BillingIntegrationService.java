package lk.dialog.pe.payment.delegation.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.PaymentPostResponse;

import lk.dialog.pe.common.util.ChequeRealizeResponse;
import lk.dialog.pe.payment.delegation.dto.ChequeRealizeRequest;
import lk.dialog.pe.payment.delegation.exception.ConnectionStatusException;
import lk.dialog.pe.payment.delegation.exception.PaymentPostException;

public interface BillingIntegrationService {

	public ChequeRealizeResponse forcefulChqRealize(ChequeRealizeRequest chequeRealizeRequest, String traceId) throws DCPEException,ConnectionStatusException;

	public PaymentPostResponse postChequePayment(PaymentPostRequest payamentRequest, String traceId) throws DCPEException;

	public PaymentPostResponse postOtherPayment(PaymentPostRequest payamentRequest, String traceId) throws DCPEException,PaymentPostException;

}
