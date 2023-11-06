package lk.dialog.pe.ccbs.service.impl;

import java.util.List;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lk.dialog.pe.ccbs.dto.Account;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.AccountService;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.ccbs.util.SQLQueryEnum;
import lk.dialog.pe.common.exception.DCPEException;

@Service
public class AccountServiceImpl implements AccountService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired 
	private QueryExecuterRepository queryExecuterRepository;
	
	@Autowired
	@Qualifier("queryMap")
	private  Map<String, String>  map;
	
	@Override
	public List<Account> getAccontNoByInvoiceNo(String invoiceNo, String traceId) throws DCPEException {

		Instant start = Instant.now();
		LOGGER.info("getAccontNoByInvoiceNoRequest : traceId={}|invoiceNo={}",traceId,invoiceNo);
		
		List<Account> accountList = getAccountNoByInvoiceNumber(invoiceNo,traceId);
					
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getAccountNoByInvoiceNoResponse : timeTaken={}|traceId={}", timeTaken, traceId);
	
		return accountList;
		
	}

	@Override
	public List<Account> getDBNAccountNoByInvoiceNo(String invoiceNo, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getDBNAccountNoByInvoiceNoRequest : traceId={}|invoiceNo={}",traceId,invoiceNo);

		List<Account> accountNo = getDBNAccountNoByInvoiceNumber(invoiceNo, traceId);
			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getDBNAccountNoByInvoiceNoResponse : timeTaken={}|traceId={}", timeTaken, traceId);

		return accountNo;

	}

	private List<Account> getAccountNoByInvoiceNumber(String billInvoiceNo, String traceId) throws DCPEException {
		Instant start = Instant.now();	
		LOGGER.info("getAccountNoByInvoiceNoRequest : traceId={}|billInvoiceNo={}", traceId, billInvoiceNo);
		
		List<Map<String, Object>> rows = null;
		try {
			rows = queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_CCBS_ACCOUNT_NO_BY_INVOICE_NO.getValue()), new Object[] {billInvoiceNo}, traceId);
		} catch (DataAccessException e) {
			LOGGER.error("getAccountNoByInvoiceNoResponse error occured: errorMessage={}|error={}traceId={}", e.getMessage(), e,traceId);
			return Collections.emptyList();
		}
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getAccountNoByInvoiceNoResponse : timeTaken={}|traceId={}", timeTaken, traceId);	
		return processAccountRows(rows);
	}

	private List<Account> getDBNAccountNoByInvoiceNumber(String billInvoiceNo, String traceId) throws DCPEException  {
		Instant start = Instant.now();	
		LOGGER.info("getDBNAccountNoByInvoiceNoRequest : traceId={}|billInvoiceNo={}", traceId, billInvoiceNo);

		List<Map<String, Object>> results = null;
		try {
			results =queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_DBN_ACCOUNT_NUM_BY_INVOICE_NO.getValue()), new Object[] {billInvoiceNo}, traceId);
		} catch (DataAccessException e) {
			LOGGER.error("getDBNAccountNoByInvoiceNoResponse error occured: errorMessage={}|error={}traceId={}", e.getMessage(), e,traceId);
			return Collections.emptyList();
		}
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getDBNAccountNoByInvoiceNoResponse : timeTaken={}|traceId={}", timeTaken, traceId);
		return processAccountRows(results); 
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
