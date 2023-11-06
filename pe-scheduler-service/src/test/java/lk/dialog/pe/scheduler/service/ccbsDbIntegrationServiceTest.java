package lk.dialog.pe.scheduler.service;

import lk.dialog.pe.scheduler.config.prop.QueryConfig;
import lk.dialog.pe.scheduler.util.SchUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ccbsDbIntegrationServiceTest {
    @Autowired
    @Qualifier("ccbsJdbcTemplate")
    JdbcTemplate ccbsJdbcTemplate;


    @Test
    public void noResultResponse(){
        List<Map<String, Object>> rows = ccbsJdbcTemplate.queryForList(QueryConfig.getQueryByKey("SQL_WIFI_CONTRACT_BY_MOBILE"), new Object[]{"mobile", "contract"});
        System.out.println(SchUtil.convertToString(rows));
    }

}
