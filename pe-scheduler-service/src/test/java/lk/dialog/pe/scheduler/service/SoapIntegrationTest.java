package lk.dialog.pe.scheduler.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.util.SchUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SoapIntegrationTest {
    @Autowired
    SoapIntegrationService soapIntegrationService;

    @Test
    public void queryPaymentsSummery() throws DCPEException {
        PaymentDTO payment = soapIntegrationService.queryPaymentsSummery("XFBBF21818263007",null);
        String  resp= SchUtil.convertToStringPretty(payment);
        System.out.println(resp);
    }
}
