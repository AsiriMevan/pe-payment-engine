package lk.dialog.pe.persistence.service;

import java.util.List;

import lk.dialog.pe.common.dto.PendingPayment;
import lk.dialog.pe.common.dto.QueryPaymentRequest;
import lk.dialog.pe.common.exception.DCPEException;

public interface PendingPaymentService {
	public List<PendingPayment> getPendingPaymentsByBranchAndReceptNo(QueryPaymentRequest jsonInput, String traceId)throws  DCPEException;
	public List<PendingPayment> getPendingPaymentsByReceiptNoAndDate(QueryPaymentRequest jsonInput, String traceId)throws DCPEException;
	public List<PendingPayment> getPendingPaymentsByBranchAndCounter(QueryPaymentRequest jsonInput, String traceId)throws DCPEException;	
	public List<PendingPayment> getPendingPaymentsByChqBranchAndChqNo(QueryPaymentRequest jsonInput, String traceId)throws  DCPEException;
	public List<PendingPayment> getPendingPaymentsByChqNumber(QueryPaymentRequest jsonInput, String traceId)throws DCPEException;	
	public List<PendingPayment> getPendingPaymentsByFromDateToDate(QueryPaymentRequest jsonInput, String traceId)throws DCPEException;
	public List<PendingPayment> getPendingPaymentsByUserBranch(QueryPaymentRequest jsonInput, String traceId) throws DCPEException;
	public List<PendingPayment> getPendingPaymentsByUserDate(QueryPaymentRequest jsonInput, String traceId)throws  DCPEException;
	public List<PendingPayment> getPendingPaymentsByBranchfromToDate(QueryPaymentRequest jsonInput, String traceId)throws DCPEException;	

}


