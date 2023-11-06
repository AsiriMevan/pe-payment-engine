package lk.dialog.pe.scheduler.config.prop;

import lk.dialog.pe.scheduler.util.SMS_MESSAGE;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "ccbs.sms")
@Getter @Setter
public class SmsConfig {

    private String moduleId;
    private String readStatus;
    private String surchargeTranType;
    private String surchargeInsertedBy;
    private String surchargeReasonCode;
    private String surchargeDcsReasonCode;
    private double surchargeAmount;

    private String[] excludeMobileCreditTypes;
    private String[] excludeDtvCreditTypes;
    private String[] excludeDbnCreditTypes;
    private String[] excludePaymentModes;

    private String[] dtvDisconReason;
    private String[] gsmCleDisconReason;
    private String[] gsmNpDisconReason;
    private String[] fixedCleDisconReason;
    private String[] fixedNpDisconReason;

    private String messageForDiscount;
    private String isSendDiscountSMS;
    private String[] discountBranch;
    private String[] discountIdType;

    private String messageForActiveGsmCxValidPay;
    private String messageForInactiveGsmCxSufficientPay;
    private String messageForInactiveGsmCxInSufficientPay;
    private String messageForUnsuccessfulGsmPay;

    private String messageForActiveDtvCxValidPay;
    private String messageForInactiveDtvCxSufficientPay;
    private String messageForInactiveDtvCxInSufficientPay;

    private String messageForActiveDBNCxValidPay;
    private String messageForInactiveDBNCxSufficientPay;
    private String messageForInactiveDBNCxInSufficientPay;
    private String messageForInactiveVOLTECxInSufficientPay;

    private String switchStatus;
    private Boolean switchIsActive=null;

    private List<String> messages;

    public String getMessage(SMS_MESSAGE messageType){
                return messages.get(messageType.getValue());
    }

    public Boolean isSwitchActive() {
        if(switchIsActive==null)switchIsActive=switchStatus.equalsIgnoreCase("Y");
        return switchIsActive;
    }

}
