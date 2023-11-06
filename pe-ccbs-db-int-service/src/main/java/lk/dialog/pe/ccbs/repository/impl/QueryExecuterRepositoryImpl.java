package lk.dialog.pe.ccbs.repository.impl;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;

import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.common.exception.DCPEException;

@Component
public class QueryExecuterRepositoryImpl implements QueryExecuterRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryExecuterRepositoryImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
   	public List<Map<String, Object>> getData(String sqlQuery, Object[] parameters, String traceId) throws DCPEException{
   		Instant start = Instant.now();
		LOGGER.info("getDataRequest : sqlQuery={} parameters={} |traceId={}",sqlQuery, parameters, traceId);
		
		List<Map<String, Object>>  result = jdbcTemplate.queryForList(sqlQuery, parameters);
		String getResult = DCPEUtil.convertToString(result);		
				
		Long timeTaken = DCPEUtil.getTimeTaken(start);	
		
		LOGGER.info("getDataResponse : getResult={} |timeTaken={} |traceId={}", getResult, timeTaken, traceId);
		return result;

	}

	public Object getSingleObject(String sqlQuery, Object[] parameters, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getSingleObjectRequest : sqlQuery={} parameters={} |traceId={}",sqlQuery, parameters, traceId);

		Object result =  jdbcTemplate.queryForObject(sqlQuery, parameters,Object.class);
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		LOGGER.info("getSingleObjectResponse :timeTaken={} |traceId={}", timeTaken, traceId);
		return result; 
		
	}
	
	public Object insertData(String sqlQuery, Object[]  parameters, String traceId) throws DCPEException{
		Instant start = Instant.now();
		LOGGER.info("insertDataRequest : sqlQuery={} parameters={} |traceId={}",sqlQuery, parameters, traceId);

		Object result = jdbcTemplate.update(sqlQuery,parameters);		
		Long timeTaken = DCPEUtil.getTimeTaken(start);	
		
		LOGGER.info("insertDataResponse :timeTaken={} |traceId={}", timeTaken, traceId);
		return result;
	}

	//todo - no time log
	public List<Map<String, Object>> getDataByQuery(String sqlQuery, String traceId) throws DCPEException {
		Instant start = Instant.now();
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getDataByQueryponse :timeTaken={} |traceId={}|sqlQuery={}", timeTaken, traceId, sqlQuery);
		return jdbcTemplate.queryForList(sqlQuery);
	}
		
    public Map<String, Object> procCall(CallableStatementCreator csc, List<SqlParameter> declaredParameters, String traceId)
            throws DCPEException{
    	Instant start = Instant.now();
        LOGGER.info("procCallRequest : sqlQuery={} parameters={} |traceId={}",csc, declaredParameters, traceId);

        Map<String, Object> result = jdbcTemplate.call(csc,declaredParameters);
        
        Long timeTaken = DCPEUtil.getTimeTaken(start);

        LOGGER.info("procCallResponse :timeTaken={} |traceId={}", timeTaken, traceId);
        return result;
    }
}
