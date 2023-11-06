package lk.dialog.pe.scheduler.service.impl;

import lk.dialog.pe.scheduler.config.prop.QueryConfig;
import lk.dialog.pe.scheduler.dto.SmsRequest;
import lk.dialog.pe.scheduler.service.CcbsDbIntegrationService;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@Service
public class ccbsDbIntegrationServiceImpl  implements CcbsDbIntegrationService {

    @Autowired
    @Qualifier("ccbsJdbcTemplate")
    private JdbcTemplate ccbsJdbcTemplate;

    @Override
    public int sendSms(SmsRequest smsRequest){
        String smsRequestString= SchUtil.convertToString(smsRequest);
        log.info("sendSmsRequest inserting smsRequest to API.KEY_SMS_MESSAGE_LIST_HP table smsRequestString={}");

        String sqlSendSmsStr = QueryConfig.getQueryByKey("SQL_PE_SEND_SMS");
        int response = ccbsJdbcTemplate.update(sqlSendSmsStr, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {

                ps.setString(1, smsRequest.getModuleId());
                ps.setString(2, smsRequest.getPhoneNo());
                ps.setString(3, smsRequest.getMessage());
                ps.setString(4, smsRequest.getReadStatus());
            }
        });

        log.info("sendSmsResponse success response={}",response);
        return response;
    }

    @Override
    public String queryForStarNomineeNumberCcbs(Long contractId) {

        String starNomineeNum = null;

        try {

            String sql = "SELECT star_nominate_no FROM cam_contract WHERE contract_id=?";
            starNomineeNum = ccbsJdbcTemplate.queryForObject(sql, new Object[] { contractId }, String.class);

        } catch (Exception e) {
            //LOGGER.error(null, e);
        }

        return starNomineeNum;
    }

    @Override
    public String queryForStarNomineeNumberTelbiz(String contractId) {

        String starNomineeNum = null;

        try {

            String sql = "SELECT ba.star_nominate_no FROM dcs.billing_account ba WHERE ba.account_num = ?";
            starNomineeNum = ccbsJdbcTemplate.queryForObject(sql, new Object[] { contractId }, String.class);

        } catch (Exception e) {
            //LOGGER.error(null, e);
        }

        return starNomineeNum;
    }
}
