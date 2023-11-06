package lk.dialog.pe.customer.info.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.credit.cam.dto.FixedNumberDTO;
import lk.dialog.pe.customer.info.domain.RBMAccount;
import lk.dialog.pe.customer.info.domain.RBMCustomer;

public interface MifeIntegrationService {

	public RBMCustomer findCustomerByAccountRef(String accountNum, String traceId) throws DCPEException;
	
	public RBMAccount readAllAccountAttributes(String accountNum, String traceId) throws DCPEException;

	public RBMCustomer readAllCustomerAttributes(String accountNum, String traceId) throws DCPEException;

	FixedNumberDTO validateSystemAvailability(String accountNum, String connectionRefType, String traceId) throws DCPEException;

}
