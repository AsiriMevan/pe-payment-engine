package lk.dialog.pe.ccbs.service.impl;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lk.dialog.pe.ccbs.util.SQLQueryEnum;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.GetMobileOfContractIdService;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.common.exception.DCPEException;

@Service
public class GetMobileOfContractIdServiceImpl implements GetMobileOfContractIdService{

	private static final Logger LOGGER = LoggerFactory.getLogger(GetMobileOfContractIdServiceImpl.class);
	
	@Autowired
	private QueryExecuterRepository queryExecuterRepository;
    
	@Autowired
	@Qualifier("queryMap")
	private  Map<String, String>  map;
	
	@Override
	public String getMobileOfContractId(String contractNo, String traceId) throws DCPEException {
		Instant start = Instant.now();	
		LOGGER.info("getMobileOfContractIdRequest : traceId={}|contractNo={}",traceId, contractNo);
		String mobileNo = null; 
		mobileNo = getMobileDataOfContractId(contractNo, traceId);
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getMobileOfContractIdResponse : mobile={}|timeTaken={}|traceId={}", mobileNo, timeTaken, traceId);
		return mobileNo;
	}

	public String getMobileDataOfContractId(String contractID, String traceId) throws DCPEException {
		Instant start = Instant.now();	
		LOGGER.info("getMobileOfContractIdRequest : contractID={}|traceId={}", contractID, traceId);
		String mobile = null;
		try {	
			mobile = (String) queryExecuterRepository.getSingleObject(map.get(SQLQueryEnum.SQL_MOBILE_BY_CONTRACT.getValue()), new Object[] { contractID }, traceId);
		} catch (DataAccessException e) {
			LOGGER.error("getMobileOfContractIdResponse error occured: errorMessage={}|error={}|traceId={}", e.getMessage(), e, traceId);
			throw new DCPEException();
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getMobileOfContractIdResponse : mobile={}|timeTaken={}|traceId={}", mobile, timeTaken, traceId);
		return mobile;
	}
}
