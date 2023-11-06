package lk.dialog.pe.persistence.reader.repository.impl;

import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.PayMode;
import lk.dialog.pe.persistence.reader.repository.QueryExcecuterReaderRepository;
import lk.dialog.pe.persistence.util.PayModeRowMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Repository
public class QueryExcecuterReaderRepositoryImpl implements QueryExcecuterReaderRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryExcecuterReaderRepositoryImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getData(String sqlQuery, Object[] parameters, String traceId) throws SQLException {

		LOGGER.info("getDataRequest : sqlQuery={} parameters={} |traceId={}", sqlQuery, parameters, traceId);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sqlQuery, parameters);
		String getResult = DCPEUtil.convertToString(result);

		Instant start = Instant.now();
		Long timeTaken = DCPEUtil.getTimeTaken(start);

		LOGGER.info("getDataRequest : getResult={} |timeTaken={} |traceId={}", getResult, timeTaken, traceId);
		return result;

	}

	public Object getSingleObject(String sqlQuery, Object[] parameters, String traceId) throws SQLException {

		LOGGER.info("getSingleObjectRequest : sqlQuery={} parameters={} |traceId={}", sqlQuery, parameters, traceId);

		Object result = jdbcTemplate.queryForObject(sqlQuery, parameters, Object.class);
		Instant start = Instant.now();
		Long timeTaken = DCPEUtil.getTimeTaken(start);

		LOGGER.info("getSingleObjectResponse :timeTaken={} |traceId={}", timeTaken, traceId);
		return result;

	}

	

	public List<Map<String, Object>> getDataByQuery(String sqlQuery,String traceId) throws SQLException {
		LOGGER.info("getDataByQueryRequest : sqlQuery={} | traceId={} ", sqlQuery, traceId);
		
		List<Map<String, Object>> res=jdbcTemplate.queryForList(sqlQuery);
		
		LOGGER.info("getDataByQueryResponse : resultSize={} | traceId={} ",res.size(), traceId);
		
		return res;
		
	}

	
	public PayMode getSinglePayModeObject(String sqlQuery, Object[] parameters,PayModeRowMapper rowMapper,String traceId) throws SQLException {

		LOGGER.info("getSingleObjectRequest : sqlQuery={} parameters={} |traceId={}", sqlQuery, parameters, traceId);

		PayMode result = jdbcTemplate.queryForObject(sqlQuery, parameters, rowMapper);
		Instant start = Instant.now();
		Long timeTaken = DCPEUtil.getTimeTaken(start);

		LOGGER.info("getSingleObjectResponse :timeTaken={} |traceId={}", timeTaken, traceId);
		return result;

	}
	
}
