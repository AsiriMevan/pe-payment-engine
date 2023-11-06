package lk.dialog.pe.ccbs.repository;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlParameter;

import lk.dialog.pe.common.exception.DCPEException;

public interface QueryExecuterRepository {

   	public List<Map<String, Object>> getData(String sqlQuery, Object[] parameters, String traceId) throws DCPEException;
	public Object getSingleObject(String sqlQuery, Object[] parameters, String traceId) throws DCPEException;
	public Object insertData(String sqlQuery, Object[]  parameters, String traceId) throws DCPEException;
	public List<Map<String, Object>> getDataByQuery(String sqlQuery, String taceId) throws DCPEException;
    public Map<String, Object> procCall(CallableStatementCreator csc, List<SqlParameter> declaredParameters,  String traceId) throws DCPEException;

}
