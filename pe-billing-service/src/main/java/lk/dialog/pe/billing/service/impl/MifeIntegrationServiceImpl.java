package lk.dialog.pe.billing.service.impl;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lk.dialog.pe.credit.cam.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.dialog.pe.billing.domain.RBMAccountDTO;
import lk.dialog.pe.billing.exception.InvalidContractNoException;
import lk.dialog.pe.billing.service.MifeIntegrationService;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.billing.util.NEConstants;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.credit.cam.service.common.MifeApiService;
import lk.dialog.pe.credit.cam.util.MifeUrlEnum;

@Service
public class MifeIntegrationServiceImpl implements MifeIntegrationService {

	@Autowired
	private MifeApiService mifeApiService;

	private static final Logger LOGGER = LoggerFactory.getLogger(MifeIntegrationServiceImpl.class);

	@Override
	public RBMAccountDTO queryBalanceForAccount(String accountNum, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("queryBalanceForAccount : traceId={}|accountNum={}", traceId, accountNum);
		QueryBalanceForAccountResponse queryBalanceForAccountResponse = null;
		if (accountNum == null || accountNum.isEmpty()) {
			LOGGER.error("queryOustandingBalance invalid accountRef : traceId={}|accountNum={}", traceId, accountNum);
			throw new DCPEException("Query Error :: > parameter is invalid");
		}
		//todo - where are time logs
		try {

			Map<String, String> map = new HashMap<>();
			map.put("contractId", accountNum);
			String exemptionDaysCount = String.valueOf(NEConstants.QUERY_BAL_EXEMPT_DAYS);
			map.put("exemptionDaysCount", exemptionDaysCount);
			queryBalanceForAccountResponse = mifeApiService.getWithUriParams(MifeUrlEnum.DBT_QUERY_BALANCE_FOR_ACCOUNT,
					map, traceId);

		} catch (Exception ex) {
			throw new DCPEException(ex.getMessage());
		}
		RBMAccountDTO rbmAccountDTO = new RBMAccountDTO();
		if (queryBalanceForAccountResponse != null) {
			rbmAccountDTO
					.setTotalOust(queryBalanceForAccountResponse.getResponse().getAccountOutstandingBalanceAsOnDate()
							/ NEConstants.RBM_AMOUNT_DIVISER);
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(rbmAccountDTO);
		LOGGER.info("queryBalanceForAccountResponse : traceId={}|timeTaken={}|{}", traceId, timeTaken, responseString);

		return rbmAccountDTO;
	}

	@Override
	public AccountBalanceDTO queryOustandingBalance(String accountRef, String lob, String isHybrid, String traceId)
			throws DCPEException,InvalidContractNoException{
		Instant start = Instant.now();
		LOGGER.info("queryOustandingBalanceRequest : traceId={}|accountRef={}|lob={}|isHybrid={}", traceId, accountRef,
				lob, isHybrid);

		Map<String, String> map = new HashMap<>();
		if (accountRef == null || accountRef.isEmpty()) {
			LOGGER.error("queryOustandingBalance invalid accountRef : traceId={}|accountRef={}", traceId, accountRef);
			throw new InvalidContractNoException();
		}

		map.put("refAccount", accountRef);
		map.put("lob", lob);
		map.put("isHybrid", isHybrid);
		OustResponse oustResponse = null;
		try {
			oustResponse = mifeApiService.getWithUriParams(MifeUrlEnum.BILLING_INFO, map, traceId);
		} catch (Exception ex) {
			throw new DCPEException(ex.getMessage());
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(oustResponse);
		LOGGER.info("queryOustandingBalanceResponse : traceId={}|timeTaken={}|{}", traceId, timeTaken, responseString);
		return oustResponse.getResponse().getAllBillingInfoResponse;
	}

	@Override
	public OCSStatusResponse queryRealTimeBalance(String accountRef, String traceId) throws DCPEException {
		OCSStatusResponse ocsStatusResponse = null;
		Instant start = Instant.now();
		LOGGER.info("queryRealTimeBalanceRequest : traceId={}|accountRef={}", traceId, accountRef);

		try {
			ocsStatusResponse = mifeApiService.getWithUriParams(MifeUrlEnum.OCS_STATUS, Collections.singletonMap("accountRef", accountRef), traceId);
		} catch (Exception ex) {
			throw new DCPEException(ex.getMessage());
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(ocsStatusResponse);
		//todo - logs incorrect
		LOGGER.info("queryRealTimeBalanceResponse : traceId={}|timeTaken={}|{}", traceId, timeTaken, responseString);

		return ocsStatusResponse;
	}
	
	@Override
	public RBMAccountDTO rbmQueryBalanceForAccount(String accountNum,String mobile,SBUEnum sbu, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("queryBalanceForAccount : traceId={}|accountNum={}", traceId, accountNum);
		QueryBalanceForAccountResponse queryBalanceForAccountResponse = null;
		if (accountNum == null || accountNum.isEmpty()) {
			LOGGER.error("queryOustandingBalance invalid accountRef : traceId={}|accountNum={}", traceId, accountNum);
			throw new DCPEException("Query Error :: > parameter is invalid");
		}
		try {

			Map<String, String> map = new HashMap<>();
			map.put("contractId", accountNum);
			String exemptionDaysCount = String.valueOf(NEConstants.QUERY_BAL_EXEMPT_DAYS);
			map.put("exemptionDaysCount", exemptionDaysCount);
			queryBalanceForAccountResponse = mifeApiService.getWithUriParams(MifeUrlEnum.DBT_QUERY_BALANCE_FOR_ACCOUNT,
					map, traceId);

		} catch (Exception ex) {
			throw new DCPEException(ex.getMessage());
		}
		
		Map<String, String> map = new HashMap<>();
		if (mobile == null || mobile.isEmpty()) {
			LOGGER.error("queryOustandingBalance invalid mobile : traceId={}|mobile={}", traceId, mobile);
			throw new DCPEException();
		}

		map.put("refAccount", mobile);
		map.put("channel", "PE");
		map.put("application", "PE");
		map.put("user", "PE");
		map.put("traceId",traceId);

		OustResponse oustResponse = null;
		RawResponse rawResponse = null;
		try {
	//		oustResponse = mifeApiService.getWithUriParams(MifeUrlEnum.BILLING_INFO, map, traceId);
			rawResponse = mifeApiService.getWithUriParams(MifeUrlEnum.LAST_BILL_BALANCE_INFO, map, traceId);
		} catch (Exception ex) {
			throw new DCPEException(ex.getMessage());
		}
		RBMAccountDTO rbmAccountDTO = new RBMAccountDTO();
		
		if (rawResponse != null) {
			rbmAccountDTO.setLastBill(rawResponse.getData().getLastBillValue());
		}
		
		
		if (queryBalanceForAccountResponse != null) {
			rbmAccountDTO
					.setTotalOust(queryBalanceForAccountResponse.getResponse().getAccountOutstandingBalanceAsOnDate()
							/ NEConstants.RBM_AMOUNT_DIVISER);
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(rbmAccountDTO);
		LOGGER.info("queryBalanceForAccountResponse : traceId={}|timeTaken={}|{}", traceId, timeTaken, responseString);

		return rbmAccountDTO;
	}

	@Override
	public MinimumPayResponse queryMinimumPayment(String connRefType, String connRefValue, String traceId) throws DCPEException {
		MinimumPayResponse minimumPayResponse;
		Instant start = Instant.now();
		LOGGER.info("queryMinimumPayment Request : traceId=[{}] | connRefType=[{}] | connRefValue=[{}]", traceId, connRefType, connRefValue);

		Map<String, String> map = new HashMap<>();
		map.put("type", connRefType);
		map.put("value", connRefValue);

		try {
			minimumPayResponse = mifeApiService.getWithUriParams(MifeUrlEnum.MIN_PAYMENT, map, traceId);
		} catch (Exception ex) {
			throw new DCPEException(ex.getMessage());
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(minimumPayResponse);
		LOGGER.info("queryMinimumPayment Response : traceId=[{}] | timeTaken=[{}] | responseString=[{}]", traceId, timeTaken, responseString);

		return minimumPayResponse;
	}

	@Override
	public FixedNumberDTO validateSystemAvailability(String accountNum, String connectionRefType, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("validateSystemAvailabilityRequest : traceId=[{}]|accountNum=[{}]", traceId, accountNum);

		FixedNumberDTO fixedNumberDTO;

		try {
			Map<String, String> map = new HashMap<>();
			map.put("connectionRef", accountNum);
			map.put("connectionRefType", connectionRefType);
			fixedNumberDTO = mifeApiService.getWithUriParams(MifeUrlEnum.SYS_VALIDATOR,
					map, traceId);
		} catch (Exception e) {
			Long timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.error("validateSystemAvailability - ERROR : traceId=[{}]|timeTaken=[{}]|error=[{}]", traceId, timeTaken, e.getMessage(), e);
			throw new DCPEException(e.getMessage());
		}

		String fixedNumberDtoToString = DCPEUtil.convertToString(fixedNumberDTO);
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("validateSystemAvailabilityResponse : traceId=[{}]|timeTaken=[{}]|fixedNumberDto=[{}]", traceId, timeTaken, fixedNumberDtoToString);

		return fixedNumberDTO;
	}

}
