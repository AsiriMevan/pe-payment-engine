package lk.dialog.pe.scheduler.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.scheduler.config.prop.SmsConfig;
import lk.dialog.pe.scheduler.util.SchUtil;
import lk.dialog.pe.credit.cam.dto.CcbsMinPayResponse;
import lk.dialog.pe.credit.cam.dto.MiscellaneousChargeRequest;
import lk.dialog.pe.credit.cam.dto.MiscellaneousChargeResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MifeIntergrationTest {

    @Autowired
    MifeIntegrationService mifeIntegrationService;

    @Autowired
    SmsConfig config;
//    TestDataGenerator testDataGenerator = new TestDataGenerator();
//    @Test
//    public void submitOcsTransactionRequest(){
//        Payment paymentDTO = testDataGenerator.getDtvCashPaymentWithSContatusDAndIsReconChargeExistYAndInvalidDisconReason();
//        OCSTransaction ocsTrx = new OCSTransaction();
//        ocsTrx.setSerialNo("CCBS".concat(String.format("%015d", paymentDTO.getTrxID())));
//        String connRef=paymentDTO.getConnRef();
//        ocsTrx.setMsisdn(connRef.startsWith("0")?connRef.substring(1) : connRef);
//        ocsTrx.setAction(paymentDTO.getTranType() == 2 ? "1" : "0");
//        ocsTrx.setAmount(String.valueOf((long) (paymentDTO.getAccountPaymentMny() * Constants.OCS_AMOUNT_MULTIPLIER)));
//        ocsTrx.setReasonCode(paymentDTO.getOcsReasonId());
//        ocsTrx.setDescription(paymentDTO.getOcsReasonCode().concat("-").concat(paymentDTO.getReceiptNo()));
//
//        String ocsResponse = mifeIntegrationService.submitOcsTransactionRequest(ocsTrx, String.valueOf(paymentDTO.getTrxID()));
//    }

    @Test
    public void ordinalSplitTest(){
        String ocsResponse="|1|2|3|{status}|5";
        int statusPOS = ordinalIndexOf(ocsResponse, "|", 4, false);
        int endPOS = ordinalIndexOf(ocsResponse, "|", 5, false);
        int seqPOS = ordinalIndexOf(ocsResponse, "|", 1, false);
        int seqPOS2 = ordinalIndexOf(ocsResponse, "|", 2, false);
        System.out.println(ocsResponse.substring(statusPOS + 1, endPOS));
//        ocsTrxStatus = Integer.parseInt(ocsResponse.substring(statusPOS + 1, endPOS));


    }

    @Test
    public void minPay() throws DCPEException {
        CcbsMinPayResponse response =  mifeIntegrationService.getCcbsMinPayment("60714801","123435");
        System.out.println(SchUtil.convertToStringPretty(response));
        Assert.assertNotNull(response);
    }

    @Test
    public void miscellaneousCharge() throws DCPEException {
        MiscellaneousChargeRequest request = new MiscellaneousChargeRequest();
        request.setAccountNo("Z1007016");
        request.setAmount(config.getSurchargeAmount());
        request.setReasonCode(config.getSurchargeDcsReasonCode());
        request.setTransactionDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date()));
        MiscellaneousChargeResponse response =  mifeIntegrationService.addMiscellaneousCharge(request,"123435");
        System.out.println(SchUtil.convertToStringPretty(response));
        Assert.assertNotNull(response);
    }

    private static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal, boolean lastIndex) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        int index = lastIndex ? str.length() : -1;
        do {
            if (lastIndex) {
                index = str.toString().lastIndexOf(searchStr.toString(),  index - 1);

            } else {
                index = str.toString().indexOf(searchStr.toString(), index + 1);

            }
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }
}
