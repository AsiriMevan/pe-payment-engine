package lk.dialog.pe.persistence.writer.repository;

import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface QueryExcecuterWriterRepository {

    public Object insertData(String sqlQuery, Object[]  parameters, String traceId) throws SQLException;

    public Map<String, Object> procCall(CallableStatementCreator csc, List<SqlParameter> declaredParameters,  String traceId) throws SQLException;

}
