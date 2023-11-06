package lk.dialog.pe.persistence.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import lk.dialog.pe.common.util.PayMode;

public class PayModeRowMapper implements RowMapper<PayMode>{

	@Override
	public PayMode mapRow(ResultSet rs, int rowNum) throws SQLException {
		PayMode payMode = new PayMode();
		payMode.setRbmId(rs.getString("rbmId"));
		payMode.setTbizId(rs.getInt("tbizId"));
		
		return payMode;
	}

}
