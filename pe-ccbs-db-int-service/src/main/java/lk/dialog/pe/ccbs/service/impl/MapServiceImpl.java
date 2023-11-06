package lk.dialog.pe.ccbs.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lk.dialog.pe.ccbs.util.SQLQueryEnum;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.MapService;
import lk.dialog.pe.ccbs.util.DCPEUtil;

@Service
public class MapServiceImpl implements MapService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapServiceImpl.class);
	@Autowired
	private QueryExecuterRepository queryExecuterRepository;
    
	@Autowired
	@Qualifier("cacheMap")
	private  Map<String, String>  map;
	
	@Override
	public String findCposIdFromPaymodeMap(String id, Boolean isRbm, String traceId) throws DCPEException {
		boolean rbm = isRbm;
		LOGGER.info("findCposIdFromPaymodeMapRequest : id={}|rbm={}|traceId={}",id,rbm?"true":"false", traceId);
		Instant start = Instant.now();
 
		String cposId = null;
		if(rbm) {
			cposId = findCposIdByRbmIdFromPaymodeMap(id, traceId);
		}
		else {
			cposId = findCposIdByTbizIdFromPaymodeMap(id, traceId);
		}
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("findCposIdFromPaymodeMapResponse : {}|traceId={}|timeTaken={}",cposId, traceId, timeTaken);
		return cposId;
	}
	
	public String findCposIdByRbmIdFromPaymodeMap(String rbmId, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("findCposIdByRbmIdFromPaymodeMapRequest. PE rbmId :traceId={}|rbmId={}" , traceId, rbmId);
		List<Map<String, Object>> result =null;
		try {
		result = queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_MAP_CPOS_PAYMODE_FOR_RBM.getValue()), new Object[] { rbmId }, traceId);
	} catch (DataAccessException e) {
		LOGGER.error("findCposIdByRbmIdFromPaymodeMapResponse error occured: traceId={}|errorMessage={}|error={}",traceId, e.getMessage(), e);
		return null;
	}
		if(!result.isEmpty()) {
		return 	result.get(0).get("cposid").toString();
		}
			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("findCposIdByRbmIdFromPaymodeMapResponse. PE rbmId : traceId={}|timeTaken={}|rbmId={}" ,traceId, timeTaken, rbmId);
		return null;
	}
	
	public String findCposIdByTbizIdFromPaymodeMap(String telbizId, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("findCposIdByTbizIdFromPaymodeMap. pe telbizId :traceId={}|telbizId={}" , traceId, telbizId);
		String result = null;
		try {
			result =(String) queryExecuterRepository.getSingleObject(map.get(SQLQueryEnum.SQL_MAP_CPOS_PAYMODE.getValue()), new Object[] {Integer.parseInt(telbizId) }, traceId);
		} catch (DataAccessException e) {
			LOGGER.error("findCposIdByTbizIdFromPaymodeMapResponse error occured: traceId={}|errorMessage={}|error={}", traceId,e.getMessage(), e);
			return null;
		}
			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("findCposIdByTbizIdFromPaymodeMapResponse :traceId={}|timeTaken={}|telbizId={}" ,traceId, timeTaken, telbizId);
		return result;
	}
}