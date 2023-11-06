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
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.GetContractIdOfMobileService;

@Service
public class GetContractIdOfMobileServiceImpl implements GetContractIdOfMobileService {

	@Autowired
	private QueryExecuterRepository queryExecuterRepository;

	@Autowired
	@Qualifier("queryMap")
	private Map<String, String> map;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetContractIdOfMobileServiceImpl.class);
	
	@Override
	public String getContractIdOfMobile (String mobileNo, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getContractIdOfMobileRequest : traceId={}|mobileNo={}",traceId, mobileNo);
		String contractNo = null;

		contractNo = getDataContractIdOfMobile(mobileNo,traceId);
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getContractIdOfMobileResponse : traceId={}|timeTaken={}|contractID={}", traceId, timeTaken, contractNo);
		return contractNo;
	}
	public String getDataContractIdOfMobile(String mobile, String traceId) throws  DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getContractIdOfMobileRequest : traceId={}|mobile={}", traceId, mobile);
		Object[] param = { mobile, mobile };
		String contractID = null;
		try {
			 contractID = (String) queryExecuterRepository
					.getSingleObject(map.get(SQLQueryEnum.SQL_CONTRACT_BY_MOBILE.getValue()), param, traceId);
		} catch (DataAccessException e) {
			throw new DCPEException(e.getMessage());
		}
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getContractIdOfMobileResponse : traceId={}|timeTaken={}|contractID={}", traceId, timeTaken,
				contractID);
		return contractID;
	}


}
