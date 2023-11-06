package lk.dialog.pe.ccbs.service.impl;

import java.time.Instant;
import java.util.Map;

import lk.dialog.pe.ccbs.util.SQLQueryEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;

import lk.dialog.pe.ccbs.service.PaymodeMapService;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.common.exception.DCPEException;

@Service
public class PaymodeMapServiceImpl implements PaymodeMapService{

	@Autowired
	private QueryExecuterRepository queryExcecuterRepository;
	
    @Autowired
    @Qualifier("cacheMap")
    private Map<String, String> map;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymodeMapServiceImpl.class);

	@Override
	public Object getPayModeMap(String traceId,String cposId)  throws DCPEException{

		LOGGER.info("getPayModeMapRequest : traceId={}|cposId={}", traceId , cposId);
		Instant start= Instant.now();

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getPayModeMapResponse : traceId={}|cposId={}|timeTaken={}", traceId,cposId,timeTaken);
		return getPayModeMaps(cposId,traceId);

	}
	
    public Object getPayModeMaps(String cposId, String traceId) throws DCPEException {
    	Instant start = Instant.now();
        Object paymodeMap = null;
        LOGGER.info("getPayModeMapRequest : traceId={}|cposId={}", traceId, paymodeMap);
        try {
            paymodeMap = queryExcecuterRepository.getSingleObject(map.get(SQLQueryEnum.SQL_MAP_CPOS_PAYMODE.getValue()), new Object[]{cposId}, traceId);
        } catch (DataAccessException e) {
            LOGGER.error("getPayModeMapResponse error occured: errorMessage={}|error={}|traceId={}", e.getMessage(), e, traceId);
            return null;
        }
        
        Long timeTaken = DCPEUtil.getTimeTaken(start);
        LOGGER.info("getPayModeMapResponse : mobile={}|timeTaken={}|traceId={}", paymodeMap, timeTaken, traceId);
        return paymodeMap;
    }
}

