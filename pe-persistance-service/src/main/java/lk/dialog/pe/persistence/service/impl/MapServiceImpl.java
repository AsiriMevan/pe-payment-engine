package lk.dialog.pe.persistence.service.impl;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.PayMode;
import lk.dialog.pe.common.util.SQLQueryEnum;
import lk.dialog.pe.persistence.reader.repository.QueryExcecuterReaderRepository;
import lk.dialog.pe.persistence.service.MapService;
import lk.dialog.pe.persistence.util.PayModeRowMapper;

@Service
public class MapServiceImpl implements MapService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapServiceImpl.class);
	
	@Autowired
	private QueryExcecuterReaderRepository queryExcecuterReaderRepository;
	
	@Autowired
	@Qualifier("cacheMap")
	private  Map<String, String>  map;
	
	@Override
	public String findCposIdFromPaymodeMap(String id, Boolean isRbm, String traceId) throws DCPEException {
		boolean rbm = isRbm;
		LOGGER.info("findCposIdFromPaymodeMapRequest : {}|{}|traceId={}",id,rbm?"true":"false", traceId);
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
	
	private String findCposIdByRbmIdFromPaymodeMap(String rbmId, String traceId) {
		LOGGER.info("findCposIdByRbmIdFromPaymodeMapRequest. PE rbmId :traceId={}|rbmId={}" , traceId, rbmId);
		List<Map<String, Object>> result =null;
		try {
		result = queryExcecuterReaderRepository.getData(map.get(SQLQueryEnum.SQL_MAP_CPOS_PAYMODE_FOR_RBM.getValue()), new Object[] { rbmId }, traceId);
	} catch (Exception e) {
		LOGGER.error("findCposIdByRbmIdFromPaymodeMapResponse error occured: traceId={}|errorMessage={}|error={}",traceId, e.getMessage(), e);
		return null;
	}
		if(!result.isEmpty()) {
		return 	result.get(0).get("cposid").toString();
		}
		Instant start = Instant.now();	
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("findCposIdByRbmIdFromPaymodeMapResponse. PE rbmId : traceId={}|timeTaken={}|rbmId={}" ,traceId, timeTaken, rbmId);
		return null;
	}
	
	private String findCposIdByTbizIdFromPaymodeMap(String telbizId, String traceId){
		LOGGER.info("findCposIdByTbizIdFromPaymodeMap. pe telbizId :traceId={}|telbizId={}" , traceId, telbizId);
		String result = null;
		try {
			result =(String) queryExcecuterReaderRepository.getSingleObject(map.get(SQLQueryEnum.SQL_MAP_CPOS_PAYMODE.getValue()), new Object[] {Integer.parseInt(telbizId) }, traceId);
		} catch (Exception e) {
			LOGGER.error("findCposIdByTbizIdFromPaymodeMapResponse error occured: traceId={}|errorMessage={}|error={}", traceId,e.getMessage(), e);
			return null;
		}
		Instant start = Instant.now();	
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("findCposIdByTbizIdFromPaymodeMapResponse :traceId={}|timeTaken={}|telbizId={}" ,traceId, timeTaken, telbizId);
		return result;
	}
	
	public PayMode getPayModeMap(String cposPayMode,String traceId) throws SQLException{
		LOGGER.info("Loading PayMode from Mapping. CPOS paymode : {}",cposPayMode);
		try {
			
			return queryExcecuterReaderRepository.getSinglePayModeObject(map.get(SQLQueryEnum.SQL_MAP_PAYMODE.getValue()), new Object[] {cposPayMode},new PayModeRowMapper(),traceId);
		} catch (EmptyResultDataAccessException dataEx) {
			LOGGER.error("No mapping found for given payment mode: {} | error: {}" ,cposPayMode, dataEx);
			throw new SQLException("No mapping found for given payment mode. Error:" + dataEx.getMessage());
		}
	}

}