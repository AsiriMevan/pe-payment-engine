package lk.dialog.pe.billing.service;

import lk.dialog.pe.billing.domain.RBMAccountDTO;
import lk.dialog.pe.billing.exception.InvalidContractNoException;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.credit.cam.dto.AccountBalanceDTO;
import lk.dialog.pe.credit.cam.dto.FixedNumberDTO;
import lk.dialog.pe.credit.cam.dto.MinimumPayResponse;
import lk.dialog.pe.credit.cam.dto.OCSStatusResponse;

public interface MifeIntegrationService {

	public RBMAccountDTO queryBalanceForAccount(String accountNum, String traceId) throws DCPEException;

	public AccountBalanceDTO queryOustandingBalance(String accountRef, String lob, String isHybrid, String traceId)
			throws DCPEException,InvalidContractNoException;

	public OCSStatusResponse queryRealTimeBalance(String accountRef, String traceId) throws DCPEException;
	
	public RBMAccountDTO rbmQueryBalanceForAccount(String accountNum,String mobile,SBUEnum sbu,String traceId) throws DCPEException;

	MinimumPayResponse queryMinimumPayment(String connRefType, String connRefValue, String traceId) throws DCPEException;

	FixedNumberDTO validateSystemAvailability(String accountNum, String connectionRefType, String traceId) throws DCPEException;
}
