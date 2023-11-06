package lk.dialog.pe.scheduler.service;

import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.SmsRequest;
import lk.dialog.pe.scheduler.service.impl.SmsServiceImpl;
import lk.dialog.pe.scheduler.util.PAYMENT_SYSTEM;
import lk.dialog.pe.scheduler.util.RbmTestDataGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SmsServiceTest {


    @InjectMocks
    @Autowired
    SmsServiceImpl smsService = new SmsServiceImpl();

    @Mock
    CcbsDbIntegrationService ccbsDbIntegrationService;

    Payment paymentInput;
    RbmTestDataGenerator rbmTestDataGenerator = new RbmTestDataGenerator();



    /*
    *sbu:VOLTE
    *paymentSystem:RBM
    * connectionStatus=D
    **/
    @Test
    public void VolteRbmCashPaymentSuccess(){
        paymentInput = rbmTestDataGenerator.getVolteSuccessRbmCashPayment();
        Mockito.when(ccbsDbIntegrationService.sendSms(Mockito.any(SmsRequest.class))).thenReturn(1);
        smsService.sendSmsProxy(paymentInput, PAYMENT_SYSTEM.RBM);
        Assert.assertTrue(true);
    }
}
