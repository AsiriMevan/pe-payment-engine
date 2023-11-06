package lk.dialog.pe.ccbs.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lk.dialog.pe.ccbs.dto.Account;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.NFCService;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.ccbs.util.SQLQueryEnum;

@Service
public class NFCServiceImpl implements NFCService{

	private static final Logger LOGGER = LoggerFactory.getLogger(NFCServiceImpl.class);

	@Autowired
	private QueryExecuterRepository queryExecuterRepository;
    
	@Autowired
	@Qualifier("queryMap")
	private  Map<String, String>  map;
	
	@Override
	public String getNfcEmailByContract(String contractNo, String traceId) throws DCPEException{
		Instant start = Instant.now();
		LOGGER.info("getNfcEmailByContractRequest : traceId={}|contractNo={}",traceId,contractNo);

		String email = getNfcEmailDataByContract(contractNo, traceId);
	
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getNfcEmailByContractResponse : traceId={}|timeTaken={}|email={}",traceId,timeTaken, email);
		return email;
	}

	@Override
	public List<Account> getNfcWifiAccountNoByInvoiceNo(String invoiceNo, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getNfcWifiAccountNoByInvoiceNoRequest : traceId={}|invoiceNo={}",traceId,invoiceNo);
		List<Account> accountList = null; 
		accountList = getCcbsWifiAccountsNoByInvoiceNo(invoiceNo, traceId);
			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getNfcWifiAccountNoByInvoiceNoResponse : traceId={}|timeTaken={}|accountList={}",traceId,timeTaken, accountList);
		return accountList;
	}

	public String getNfcEmailDataByContract(String contractId, String traceId)  throws DCPEException{
		Instant start = Instant.now();
		LOGGER.info("getProfilesByInvoiceNoRequest : traceId={}|contractId={}", traceId, contractId);
		Object[] params= {contractId};
		String result = null;
		try {
			result =  (String) queryExecuterRepository.getSingleObject(map.get(SQLQueryEnum.SQL_NFC_EMAIL_BY_CONTRACT.getValue()), params, traceId);
		} catch (DataAccessException e) {
			return null;
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByInvoiceNoResponse : timeTaken={}|traceId={}|result={}", timeTaken, traceId, result);
		return result;
	}

	//todo - why is this public?
	//todo - why not throw exception? why passing a empty list instead? look like the logic is different compared to old code
	public List<Account> getCcbsWifiAccountsNoByInvoiceNo(String invoiceNo, String traceId)  throws DCPEException{
		Instant start = Instant.now();
		LOGGER.info("getProfilesByInvoiceNoRequest : traceId={}|invoiceNo={}", traceId, invoiceNo);
		 
		List<Map<String, Object>> rows = null;
		try {
			rows = queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_NFC_WIFI_ACCOUNT_NO_BY_INVOICE_NO.getValue()), new Object[] {invoiceNo}, traceId);
		} catch (DataAccessException e) {
			LOGGER.error("getProfilesByInvoiceNoResponse error occured: errorMessage={}|error={}|traceId={}", e.getMessage(), e, traceId);
			return Collections.emptyList();
		}
			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByInvoiceNoResponse : timeTaken={}|traceId={}", timeTaken, traceId);
	
		return processAccountRows(rows);
	
	}

	
	private List<Account> processAccountRows(List<Map<String, Object>> rows) {
		List<Account> profileList = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			Account account = new Account();
			String accountNo = (String) row.get("ACCOUNT_NUM");
			account.setContractNo(accountNo != null ? accountNo : null);
			profileList.add(account);
		}
		return profileList;
	}

}
