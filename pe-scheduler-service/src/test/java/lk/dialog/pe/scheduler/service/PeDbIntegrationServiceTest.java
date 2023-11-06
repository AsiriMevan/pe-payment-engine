package lk.dialog.pe.scheduler.service;

import lk.dialog.pe.scheduler.config.prop.QueryConfig;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.repository.mapper.ChequePaymentMapper;
import lk.dialog.pe.scheduler.repository.mapper.DBNPaymentMapper;
import lk.dialog.pe.scheduler.repository.mapper.RBMPaymentMapper;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.QUERY;
import lk.dialog.pe.scheduler.util.RbmTestDataGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PeDbIntegrationServiceTest {


    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate peJdbcTemplate;

    RbmTestDataGenerator rbmTestDataGenerator = new RbmTestDataGenerator();


    public  void queryDBN(){
        List<Payment> payments = peJdbcTemplate.query(QueryConfig.getQuery(QUERY.SQL_SEL_DBN_PAYMENT), new DBNPaymentMapper());

        Assert.assertTrue(true);
    }


    @Test
    public  void compareRbmPayments(){
        List<Payment> payments = peJdbcTemplate.query("select * from rbm_payment where req_id='44112899'", new RBMPaymentMapper());
        List<Payment> payments2 = peJdbcTemplate.query("select * from rbm_payment where req_id='988617'", new RBMPaymentMapper());

//        System.out.println(SchUtil.convertToStringPretty(payments2.get(0)));
        assertThat(payments2.get(0)).usingRecursiveComparison().isEqualTo(payments.get(0));
    }

    @Test
    public void printValueSetters(){
//        List<Payment> payments2 = peJdbcTemplate.query("select * from rbm_payment where req_id='112472'", new RBMPaymentMapper());
//          List<Payment> payments2 = peJdbcTemplate.query("SELECT op.*, orc.reason_code as ocs_reason_code, orc.reason_id FROM ocs_payment op, ocs_reason_codes orc WHERE op.tran_type_id = orc.tran_type_id and op.REQ_ID = '2006253'", new OCSPaymentMapper());
//        List<Payment> payments2 = peJdbcTemplate.query("SELECT cp.*  ,(SELECT rmap.description FROM cancel_adj_reason_map rmap WHERE rmap.cpos_id=cp.cancelled_reason) as cancelled_reason_desc FROM cancel_payment  cp WHERE cp.req_id='112646'", new CancelPaymentMapper());
//        List<Payment> payments2 =peJdbcTemplate.query("SELECT * FROM forceful_realize_cheques WHERE req_id='112472'", new ChequePaymentMapper(false));
        List<Payment> payments2 =peJdbcTemplate.query("SELECT * FROM forceful_realize_cheques_h WHERE req_id='526061'", new ChequePaymentMapper(false));

        RbmTestDataGenerator.printObjectValueSetters("payment",payments2.get(0));
        Assert.assertNotNull(payments2.get(0));
    }

    @Test
    public void printDtvFreeBeeOnPayment(){
        peJdbcTemplate.queryForObject("select record_id from DTV_FREEBEE_ON_PAYMENT where record_id='"+13060670+"'", Map.class);
    }

    @Test
    public void readCommandRead(){
        String trxId = "988617";
        String commandRead = peJdbcTemplate.queryForObject("select COMMAND_READ from rbm_payment where req_id='"+trxId+"'",String.class);
        System.out.println("commandRead="+commandRead);
    }

    @Test
    public void loadPaymentsForProcessingRbmRetry(){
        Payment paymentInput = rbmTestDataGenerator.getRbmResubmitFailSelectPayment();
        paymentInput.setErrCode("2");
        paymentInput.setErrDesc("test error");
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput, COMMAND_READ.F,peJdbcTemplate);

    }

}
