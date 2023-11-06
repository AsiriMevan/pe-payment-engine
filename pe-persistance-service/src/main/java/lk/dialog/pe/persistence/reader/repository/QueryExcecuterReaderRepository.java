package lk.dialog.pe.persistence.reader.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import lk.dialog.pe.common.util.PayMode;
import lk.dialog.pe.persistence.util.PayModeRowMapper;

public interface QueryExcecuterReaderRepository {

    public List<Map<String, Object>> getData(String sqlQuery, Object[] parameters, String traceId) throws SQLException;

    public Object getSingleObject(String sqlQuery, Object[] parameters, String traceId) throws SQLException;

    public List<Map<String, Object>> getDataByQuery(String sqlQuery,String traceId) throws SQLException;
    
    public PayMode getSinglePayModeObject(String sqlQuery, Object[] parameters,PayModeRowMapper rowMapper,String traceId)throws SQLException;

  
}
