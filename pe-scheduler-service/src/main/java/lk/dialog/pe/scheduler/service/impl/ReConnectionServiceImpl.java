package lk.dialog.pe.scheduler.service.impl;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.scheduler.config.prop.QueryConfig;
import lk.dialog.pe.scheduler.service.ReconnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Types;

/**
 * use to call reconnection logic.
 * @author Mohan_02392
 */
@Service
public class ReConnectionServiceImpl implements ReconnectionService {
	private static final Logger log = LoggerFactory.getLogger(ReConnectionServiceImpl.class);
	@Autowired
	@Qualifier("ccbsJdbcTemplate")
	public JdbcTemplate ccbsJdbcTemplate;
	@Autowired
	@Qualifier("peJdbcTemplate")
	public JdbcTemplate peJdbcTemplate;
	
	@Override
	public void reconnect(String contractId, double paymentMny) {
		log.info("reconnectRequest duplicate check -reconnect- contractId={}|paymentAmount={}",contractId,paymentMny);
		try {
			String subsidiary;
			String finalSQL;
			if (contractId.matches(Constants.INTEGER_VALIDATOR)) {
				finalSQL = QueryConfig.getQueryByKey("SQL_SEL_SUBSIDIARY")+" UNION "+ QueryConfig.getQueryByKey("SQL_SEL_DCS_SUBSIDIARY");
				subsidiary = ccbsJdbcTemplate.queryForObject(finalSQL, new Object[]{contractId,contractId}, String.class);
			}else{
				finalSQL = QueryConfig.getQueryByKey("SQL_SEL_DCS_SUBSIDIARY");
				subsidiary = ccbsJdbcTemplate.queryForObject(finalSQL, new Object[]{contractId}, String.class);
			}
			log.info("reconnectExecute: duplicate check of subsidiary(={})",subsidiary);
			if (subsidiary == null || subsidiary.isEmpty())
				log.info("reconnectExecute: No valid subsidiary found. payment ignored. contractId={}|paymentAmount={}",contractId,paymentMny);
			else {
				// define query arguments
				// insert DCS numbers to AUTO_RECON_CXS table which allows alfa numeric characters, other numbers will be inserted to rbm_payment_auto_recon_cxs
				if("DCS".equals(subsidiary)){
					subsidiary= SBUEnum.getSBU(SBUEnum.DBN.valueOf()).name();
					Object[] params = new Object[]{contractId, subsidiary, paymentMny, "Insert by payment posting application. Pending to re-connect"};
					// define SQL types of the arguments
					int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.DOUBLE, Types.VARCHAR};
					peJdbcTemplate.update(QueryConfig.getQueryByKey("SQL_INS_DCS_RECONNECTION"), params, types);
				}else {
					Object[] params = new Object[]{contractId, subsidiary, paymentMny, "Insert by payment posting application. Pending to re-connect"};
					// define SQL types of the arguments
					int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.DOUBLE, Types.VARCHAR};
					ccbsJdbcTemplate.update(QueryConfig.getQueryByKey("SQL_INS_RECONNECTION"), params, types);
				}
				log.info("reconnectResponse success Reconnection logic successfully called for contractId={}|amount={}",contractId,paymentMny);
			}
		} catch (Exception ex) {
			log.error("reconnectResponse Reconnection logic calling failed for contractId={}|amount={}|error={}",contractId,paymentMny,ex.getMessage(),ex);
		}
	}
}
