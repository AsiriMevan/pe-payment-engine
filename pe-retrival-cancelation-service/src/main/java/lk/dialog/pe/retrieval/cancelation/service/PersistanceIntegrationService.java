package lk.dialog.pe.retrieval.cancelation.service;

import java.util.List;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.retrieval.cancelation.domain.PaymentDTO;
import lk.dialog.pe.retrieval.cancelation.util.QueryPaymentsSummery;

public interface PersistanceIntegrationService {

	String findContractSubsidiaryTypeById(String contractId, boolean dcsOnly, String traceId) throws DCPEException;

	String getPayCposModeToRBMdata(String contractID, String traceId) throws DCPEException;

	List<PaymentDTO> getPaymentsSummery(QueryPaymentsSummery queryPaymentsSummery, String traceId) throws DCPEException;

}
