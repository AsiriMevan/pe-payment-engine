package lk.dialog.pe.persistence.writer.repository.impl;

import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.persistence.writer.repository.QueryExcecuterWriterRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Repository
public class QueryExcecuterWriterRepositoryImpl implements QueryExcecuterWriterRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryExcecuterWriterRepositoryImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;


	public Object insertData(String sqlQuery, Object[] parameters, String traceId) throws SQLException {

		LOGGER.info("insertDataRequest : sqlQuery={} parameters={} |traceId={}", sqlQuery, parameters, traceId);
		Long timeTaken;
		Object result = null;
		try {

			result = jdbcTemplate.update(sqlQuery, parameters);
			Instant start = Instant.now();
			timeTaken = DCPEUtil.getTimeTaken(start);

		} catch (Exception e) {
			throw new SQLException(e.getCause());
		}

		LOGGER.info("insertDataResponse :timeTaken={} |traceId={}", timeTaken, traceId);
		return result;
	}

	

	public Map<String, Object> procCall(CallableStatementCreator csc, List<SqlParameter> declaredParameters,
			String traceId) throws SQLException {
		LOGGER.info("procCallRequest : sqlQuery={} parameters={} |traceId={}", csc, declaredParameters, traceId);

		Map<String, Object> result = jdbcTemplate.call(csc, declaredParameters);
		Instant start = Instant.now();
		Long timeTaken = DCPEUtil.getTimeTaken(start);

		LOGGER.info("procCallResponse :timeTaken={} |traceId={}", timeTaken, traceId);
		return result;
	}
}
