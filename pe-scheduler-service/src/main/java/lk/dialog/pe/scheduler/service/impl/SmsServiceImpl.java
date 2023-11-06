package lk.dialog.pe.scheduler.service.impl;

import java.io.IOException;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.ConnectionStatus;
import lk.dialog.pe.common.util.PRODUCT_TYPE;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.scheduler.config.prop.QueryConfig;
import lk.dialog.pe.scheduler.config.prop.SmsConfig;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.SmsRequest;
import lk.dialog.pe.scheduler.service.CcbsDbIntegrationService;
import lk.dialog.pe.scheduler.service.MifeIntegrationService;
import lk.dialog.pe.scheduler.service.SmsService;
import lk.dialog.pe.scheduler.util.PAYMENT_SYSTEM;
import lk.dialog.pe.scheduler.util.SMS_MESSAGE;
import lk.dialog.pe.scheduler.util.SchUtil;
import lk.dialog.pe.credit.cam.dto.CcbsMinPayResponse;
import lk.dialog.pe.credit.cam.dto.MinimumPayInfo;
import lk.dialog.pe.credit.cam.dto.MiscellaneousChargeRequest;
import lk.dialog.pe.credit.cam.dto.MiscellaneousChargeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
    @Autowired
    @Qualifier("ccbsJdbcTemplate")
    private JdbcTemplate ccbsJdbcTemplate;
    @Autowired
    private MifeIntegrationService mifeService;
    @Autowired
    @Qualifier("peJdbcTemplate")
    public JdbcTemplate peJdbcTemplate;
    @Autowired
    SmsConfig config;

    @Autowired
    CcbsDbIntegrationService ccbsDbIntegrationService;


    public String sendSMS(SmsRequest smsRequest) {
        String sendSMSstatus = null;
        smsRequest.setModuleId(config.getModuleId());
        smsRequest.setReadStatus(config.getReadStatus());
        String smsRequestString= SchUtil.convertToString(smsRequest);
        log.info("sendSmsRequest to ccbs started");

        try {
            ccbsDbIntegrationService.sendSms(smsRequest);
            sendSMSstatus = "SUCCESS";
        } catch (Exception ex) {
            log.error("sendSmsResponse Send message for {} failed error={}" ,smsRequest.getPhoneNo(),ex.getMessage(),ex);
            sendSMSstatus = "FAIL";
        }
        log.info("sendSmsResponse sendSmsStatus={}",sendSMSstatus);
        return sendSMSstatus;
    }

    //---------------------adding sms message -----------
    private void setMessage(SMS_MESSAGE messageType, SmsRequest smsRequest, Payment payment , Double reconnectAmount){
        log.info("smsProxy:setMessageRequest messageType={}|paymentAmount={}|contractNo={}|receiptNo={}|connectionReference={}|reconnectAmount={}",messageType,payment.getAccountPaymentMny(),payment.getContractNo(),payment.getReceiptNo(),payment.getConnRef(),reconnectAmount);
        String roundedAmount = new DecimalFormat("#0.00").format(payment.getAccountPaymentMny());
        String rawMessage = config.getMessage(messageType).replace("CURRENT_DATE",dateFormat.format(new Date())).replace("AMOUNT",roundedAmount);
        String messageTypeString = messageType.toString();

        if(messageType != SMS_MESSAGE.UNSUCCESSFUL_GSM_PAY ) {
            rawMessage = rawMessage.replace("RECEIPT_NO", payment.getReceiptNo());
        }

        if(messageTypeString.contains("DTV")){
            rawMessage = rawMessage.replace("ACCOUNT_NO", payment.getContractNo());
        }

        if(messageTypeString.contains("INACTIVE") && messageTypeString.contains("IN_SUFFICIENT")){
            rawMessage = rawMessage.replace("RECONNECT_AMT", new DecimalFormat("#0.00").format(reconnectAmount));
        }

        if(messageTypeString.contains("DBN") || messageTypeString.contains("VOLTE") ){
            rawMessage = rawMessage.replace("CONN_REF", payment.getConnRef());
        }

        log.info("smsProxy:setMessageResponse Generated final Cx Message key={}|message={} ",messageType,rawMessage);
        smsRequest.setMessage(rawMessage);
    }

    private void addMessageText(SMS_MESSAGE messageType,SmsRequest smsRequest,Payment payment){
        setMessage(messageType,smsRequest,payment,null);
    }
    private void addMessageTextWithReconnectAmount(SMS_MESSAGE messageType,SmsRequest smsRequest,Payment payment , Double reconnectAmount){
        setMessage(messageType,smsRequest,payment,reconnectAmount);
    }

//-----------------------------------------------

    //------------------------ccbs-db-calls-------------------
    private static final String PROC_STATUS = "p_status";
    private static final String PROC_OUTPUT="wk_payable";


    private String isReconChargeExist(String contractNo){
        log.info("SMSproxy:isReconChargeExistRequest contractNo={}",contractNo);
        String isReconChargeExist = "Y"; //was set to default true in original code
        String imageIdInit="";

        List<SqlParameter> paramList = new ArrayList<>();
        paramList.add(new SqlOutParameter("wk_image_id", Types.VARCHAR));
        paramList.add(new SqlParameter(Types.VARCHAR));
        paramList.add(new SqlOutParameter(PROC_STATUS, Types.VARCHAR));

        final String functionCall = QueryConfig.getQueryByKey("SQL_GET_IMG_ID_FROM_CONTRACT");
        Map<String, Object> resultMap = ccbsJdbcTemplate.call(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                CallableStatement callableStatement = connection.prepareCall(functionCall);
                callableStatement.registerOutParameter(1, Types.VARCHAR);
                callableStatement.setString(2, contractNo);
                callableStatement.registerOutParameter(3, Types.VARCHAR);
                return callableStatement;
            }
        }, paramList);

        imageIdInit = ((String) resultMap.get("wk_image_id"));
        log.info("SMSproxy:isReconChargeExistResponse for getImageIdFromContract contractNo={}|imageIdInit={}",contractNo,imageIdInit);

        if ( imageIdInit !=null && !imageIdInit.isEmpty()){

            final String imageId = imageIdInit;
            List<SqlParameter> params = new ArrayList<>();
            params.add(new SqlOutParameter("p_recon_charge_needed", Types.VARCHAR));
            params.add(new SqlParameter(Types.VARCHAR));

            final String procCall = QueryConfig.getQueryByKey("SQL_CHECK_RECON_CHR_NEEDED");
            Map<String, Object> response = ccbsJdbcTemplate.call(new CallableStatementCreator() {
                @Override
                public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                    CallableStatement callableStatement = connection.prepareCall(procCall);
                    callableStatement.registerOutParameter(1, Types.VARCHAR);
                    callableStatement.setString(2, imageId);
                    return callableStatement;
                }
            }, params);

            isReconChargeExist = ((String)response.get("p_recon_charge_needed")).toUpperCase();

        }
        log.info("isReconChargeExistResponse Success contractNo={}|imageIdInit={}|isReconChargeExist={}",contractNo,imageIdInit,isReconChargeExist);
        return isReconChargeExist;
    }

    private String getDisconnectReason(String contractNo){
        log.info("getDisconnectReasonRequest contractNo={}",contractNo);
        String disconnectReason = ccbsJdbcTemplate.queryForObject(QueryConfig.getQueryByKey("SQL_GET_DISCON_REASON"), new Object[] { contractNo }, String.class);
        log.info("getDisconnectReasonResponse Success contractNo={}|disconnectReason={}",contractNo,disconnectReason);
        return disconnectReason;
    }

    private Double getDtvMinAmountToPay(String contractNo, String connectionReference, double payedAmount){
        log.info("SMSproxy:getDtvMinAmountToPayRequest contractNo={}|connectionReference={}|payedAmount={}",contractNo,connectionReference,payedAmount);

        // Getting MinAmountToPay by calling dtv_fb_bill_tran_api.get_current_bal_after_payment procedure - Pass 'R' for p_recon_job_api parameter
        List<SqlParameter> params = new ArrayList<>();
        params.add(new SqlOutParameter("wk_min_amount", Types.DOUBLE));
        params.add(new SqlParameter(Types.VARCHAR));
        params.add(new SqlParameter(Types.VARCHAR));
        params.add(new SqlParameter(Types.DOUBLE));
        params.add(new SqlOutParameter(PROC_STATUS, Types.VARCHAR));
        params.add(new SqlParameter(Types.DOUBLE));
        params.add(new SqlParameter(Types.DOUBLE));
        params.add(new SqlParameter(Types.VARCHAR));

        final String dtvBalancefunction = QueryConfig.getQueryByKey("SQL_GET_DTV_REMINDER_BAL");
        Map<String, Object> response = ccbsJdbcTemplate.call(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                CallableStatement callableStatement = connection.prepareCall(dtvBalancefunction);
                callableStatement.registerOutParameter(1, Types.DOUBLE);
                callableStatement.setString(2, connectionReference);
                callableStatement.setString(3, "N");
                callableStatement.setDouble(4, payedAmount);
                callableStatement.registerOutParameter(5, Types.VARCHAR);
                callableStatement.setString(6, null);
                callableStatement.setString(7, null);
                callableStatement.setString(8,"R");

                return callableStatement;
            }
        }, params);

        Double minPaymentToReconn = ((Double) response.get("wk_min_amount"));
        String status= (String) response.get(PROC_STATUS);

        log.info("SMSproxy:getDtvMinAmountToPayResponse Success contractNo={}|connectionReference={}|payedAmount={}|minPaymentToReconnect={}",contractNo,connectionReference,payedAmount,minPaymentToReconn);

        return status!=null && !status.isEmpty() && status.equals("OK")?minPaymentToReconn:null;
    }

    private Double getGsmOrVolteMinAmountToPay(Payment payment, String disconnectedReason){

        log.info("SMSproxy:getGsmOrVolteMinAmountToPayRequest contractNo={}|connectionReference={}|disconnectedReason={}",payment.getContractNo(),payment.getConnRef(),disconnectedReason);

        // Getting Min by calling Fb_Bill_Tran_Api.GET_REMINDER_BALANCE procedure - Pass 'R' for p_recon_job_api parameter
        List<SqlParameter> params = new ArrayList<>();
        params.add(new SqlOutParameter(PROC_OUTPUT, Types.DOUBLE));
        params.add(new SqlParameter(Types.DOUBLE));
        params.add(new SqlParameter(Types.VARCHAR));
        params.add(new SqlParameter(Types.VARCHAR));
        params.add(new SqlParameter(Types.DOUBLE));
        params.add(new SqlParameter(Types.DOUBLE));
        params.add(new SqlParameter(Types.VARCHAR));


        final String gsmBalancefunction = QueryConfig.getQueryByKey("SQL_GET_GSM_REMINDER_BAL");
        Map<String, Object> response = ccbsJdbcTemplate.call(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                CallableStatement callableStatement = connection.prepareCall(gsmBalancefunction);
                callableStatement.registerOutParameter(1, Types.DOUBLE);
                callableStatement.setString(2, payment.getContractNo());
                callableStatement.setString(3, disconnectedReason);
                callableStatement.setString(4,null );
                callableStatement.setString(5, null);
                callableStatement.setString(6,null );
                callableStatement.setString(7,"R");

                return callableStatement;
            }
        }, params);



        Double minPaymentToReconnect = ((Double) response.get(PROC_OUTPUT));
        log.info("SMSproxy:getGsmOrVolteMinAmountToPayResponse Success minPaymentToReconnect={}|contractNo={}|connectionReference={}|disconnectedReason={}",minPaymentToReconnect,payment.getContractNo(),payment.getConnRef(),disconnectedReason);
        return minPaymentToReconnect;
    }
//--------------------------------------------------------

//----------------logic blocks -----------------------------

    private void generateDtvMessage(SmsRequest smsRequest,Payment payment ,SBUEnum sbu,ConnectionStatus connectionStatus)
        throws DCPEException, IOException {
        log.info("SMSProxy:generateDtvMessageRequest contractNo={}sbu={}|paymentAmount={}|connectionStatus={}",payment.getContractNo(),sbu,payment.getAccountPaymentMny(),connectionStatus);

        if ( connectionStatus == ConnectionStatus.C){
            addMessageText(SMS_MESSAGE.ACTIVE_DTV_CX_VALID_PAY,smsRequest,payment);

            /**
             * Calling addDTVOfferActRecord method to add DTV POSTPaid payment records to activate DTV offers after a payment
             * when Connection Status - C
             */
            if(sbu.name().equals("DTV_POSTPAID")){
                log.info("SMSproxy:generateDtvMessage Calling addDTVOfferActRecord method to add DTV Postpaid payment record to add offer for connected number contractNo={}",payment.getContractNo());
                this.addDTVOfferActRecord(payment.getTrxID(),payment.getContractNo(),payment.getAccountPaymentMny(),payment.getConnectionStatus());
            }

        } else if (connectionStatus.orEqual(ConnectionStatus.D,ConnectionStatus.B,connectionStatus,ConnectionStatus.T)){
            /* Note :
            If the status=D and reconnectionCharge is exists , it is an Permanent disconnection
            If the status=D and reconnectionCharge is not exist , it is an temporary disconnection
             */
            String isReconChargeExist = isReconChargeExist(payment.getContractNo());

            if ( "Y".equals(isReconChargeExist)){
                log.info("SMSProxy:generateDtvMessage Permanently Disconnected DTV Number contractNo={}",payment.getContractNo());
                // Permanent disconnection - Connection will not be reconnect automatically
                addMessageText(SMS_MESSAGE.ACTIVE_DTV_CX_VALID_PAY,smsRequest,payment);
            }else if ("N".equals(isReconChargeExist)){
                // Get the disconnection reason
                String disconnectReason= getDisconnectReason(payment.getContractNo());
                log.info("SMSproxy:generateDtvMessage Temporary Disconnected DTV Number, Disconnected Reason={}|contractNo={}",disconnectReason,payment.getContractNo());

                if ( Arrays.stream(config.getDtvDisconReason()).anyMatch(disconnectReason::equals)) {
                    generateDtvDisconnectMessage(smsRequest,payment,sbu);
                }else{
                    addMessageText(SMS_MESSAGE.ACTIVE_DTV_CX_VALID_PAY,smsRequest,payment);
                }
            }else{
                addMessageText(SMS_MESSAGE.ACTIVE_DTV_CX_VALID_PAY,smsRequest,payment);
            }
        }
        log.info("SMSProxy:generateDtvMessageResponse Success contractNo={}",payment.getContractNo());
    }

    private void generateDtvDisconnectMessage(SmsRequest smsRequest,Payment payment,SBUEnum sbu)
        throws DCPEException, IOException {
        log.info("SMSProxy:generateDtvDisconnectMessageRequest contractNo={}|sbu={}|paymentAmount={}",payment.getContractNo(),sbu.name(),payment.getAccountPaymentMny());

        // Getting MinAmountToPay by calling dtv_fb_bill_tran_api.get_current_bal_after_payment procedure - Pass 'R' for p_recon_job_api parameter
        Double minPaymentToReconnect = getDtvMinAmountToPay(payment.getContractNo(),payment.getConnRef(),payment.getAccountPaymentMny());
        log.info("SMSProxy:generateDtvMessage Minimum Amount to Pay:{}",minPaymentToReconnect);

        if (minPaymentToReconnect!=null) {

            //If the PaidAmount is greater than minimumPayment-100,
            if (minPaymentToReconnect <= 100) {
                // The line will be reconnected
                addMessageText(SMS_MESSAGE.INACTIVE_DTV_CX_SUFFICIENT_PAY,smsRequest,payment);
                /**
                 * Calling addDTVOfferActRecord method to add DTV POSTPaid payment records to activate DTV offers after a payment
                 * when Connection Status - T/B
                 */
                if(sbu == SBUEnum.DTV_POSTPAID){
                    log.info("SmsProxy:generateDtvDisconnectMessage Calling addDTVOfferActRecord method to add DTV Postpaid payment record to add offer for temporary disconnected number contractNo={}",payment.getContractNo());
                    this.addDTVOfferActRecord(payment.getTrxID(),payment.getContractNo(),payment.getAccountPaymentMny(),payment.getConnectionStatus());
                }
            } else {
                CcbsMinPayResponse minPayResp = mifeService.getCcbsMinPayment(payment.getConnRef(),String.valueOf(payment.getTrxID()));
                MinimumPayInfo minPayInfo = minPayResp.getResponse()!=null?minPayResp.getResponse().getMinPayInfo():new MinimumPayInfo();

                if(minPayInfo.getExecutionStatus().equals("00")) {
                    Double totAmountToPay = Double.parseDouble(minPayInfo.getMinimumAmount());
                    addMessageTextWithReconnectAmount(SMS_MESSAGE.INACTIVE_DTV_CX_IN_SUFFICIENT_PAY,smsRequest,payment,totAmountToPay);
                }else {
                    addMessageText(SMS_MESSAGE.ACTIVE_DTV_CX_VALID_PAY,smsRequest,payment);
                }
            }
        } else {
            addMessageText(SMS_MESSAGE.ACTIVE_DTV_CX_VALID_PAY,smsRequest,payment);
        }
        log.info("SMSProxy:generateDtvDisconnectMessageResponse success contractNo={}|sbu={}",payment.getContractNo(),sbu.name());
    }

    private void generateGsmMessage(SmsRequest smsRequest,Payment payment ,ConnectionStatus connectionStatus)
        throws DCPEException, IOException {
        log.info("SMSProxy:generateGsmMessage contractNo={}|connectionStatus={}",payment.getContractNo(),connectionStatus.name());
        if (connectionStatus.orEqual(ConnectionStatus.C,ConnectionStatus.D)) {
            addMessageText(SMS_MESSAGE.ACTIVE_GSM_CX_VALID_PAY,smsRequest,payment);

        } else if ( connectionStatus.orEqual(ConnectionStatus.B,ConnectionStatus.T)) {
            String disconnReason = getDisconnectReason(payment.getContractNo());
            log.info("SmsProxy:generateGsmMessage Disconnected GSM Number, Disconnected Reason={}",disconnReason);

            if ( Arrays.stream(config.getGsmNpDisconReason()).anyMatch(disconnReason::equals) || Arrays.stream(config.getGsmCleDisconReason()).anyMatch(disconnReason::equals)) {

                CcbsMinPayResponse minPayResp = mifeService.getCcbsMinPayment(payment.getConnRef(),String.valueOf(payment.getTrxID()));
                MinimumPayInfo minPayInfo = minPayResp.getResponse()!=null?minPayResp.getResponse().getMinPayInfo():new MinimumPayInfo();

                if (minPayInfo.getExecutionStatus().equalsIgnoreCase("00")) {
                    generateGsmMinPayMessage(smsRequest,payment,minPayInfo,disconnReason);
                } else {
                    log.info("smsProxy Get Minimum Payment API returns an error contractNo={}|connectionReferance={}",payment.getContractNo(),payment.getConnRef());
                    addMessageText(SMS_MESSAGE.ACTIVE_GSM_CX_VALID_PAY,smsRequest,payment);
                }
            }else{
                addMessageText(SMS_MESSAGE.ACTIVE_GSM_CX_VALID_PAY,smsRequest,payment);
            }
        }
        log.info("SMSProxy:generateGsmMessageResponse success contractNo={}|connectionStatus={}",payment.getContractNo(),connectionStatus.name());
    }

    private void generateGsmMinPayMessage(SmsRequest smsRequest,Payment payment ,MinimumPayInfo minPayInfo ,String disconnReason){
        log.info("SMSProxy:generateGsmMinPayMessageRequest contractNo={}|payedAmount={}|minimumPayment={}|disconnectReason={}",payment.getContractNo(),payment.getAccountPaymentMny(),minPayInfo.getMinimumAmount(),disconnReason);

        double totPaymentToReconn = Double.parseDouble(minPayInfo.getMinimumAmount());
        double totPaymentToReconnect = totPaymentToReconn;
        Double minPaymentToReconnect = null;

        if ( Arrays.stream(config.getGsmNpDisconReason()).anyMatch(disconnReason::equals)){

            minPaymentToReconnect = getGsmOrVolteMinAmountToPay(payment,disconnReason);
            log.info("SMSProxy:generateGsmMinPayMessageResponse Minimum GSM Amount to Pay for reconnection ={}|contractNo={}",minPaymentToReconnect,payment.getContractNo());

            if (minPaymentToReconnect >100) {
                addMessageTextWithReconnectAmount(SMS_MESSAGE.INACTIVE_GSM_CX_IN_SUFFICIENT_PAY,smsRequest,payment,totPaymentToReconnect);
            }else{
                addMessageText(SMS_MESSAGE.INACTIVE_GSM_CX_SUFFICIENT_PAY,smsRequest,payment);
            }
        }else if (Arrays.stream(config.getGsmCleDisconReason()).parallel().anyMatch(disconnReason::equals)) {
            double feeToConnect = 0;
            if (getOcsCommandRead(payment.getTrxID()).equalsIgnoreCase("S")) {
                feeToConnect = totPaymentToReconnect;
            }else{
                feeToConnect = totPaymentToReconnect - payment.getAccountPaymentMny();
            }
            if (feeToConnect>0) {
                addMessageTextWithReconnectAmount(SMS_MESSAGE.INACTIVE_GSM_CX_IN_SUFFICIENT_PAY,smsRequest,payment,totPaymentToReconnect);

            }else{
                addMessageText(SMS_MESSAGE.INACTIVE_GSM_CX_SUFFICIENT_PAY,smsRequest,payment);
            }
        }
        log.info("SMSProxy:generateGsmMinPayMessageResponse success contractNo={}|payedAmount={}|totPaymentToReconnect={}|minPaymentToReconnect={}",payment.getContractNo(),payment.getAccountPaymentMny(),totPaymentToReconn,minPaymentToReconnect);

    }

    private void generateFixedNumberMessage(SmsRequest smsRequest, Payment payment, ConnectionStatus connectionStatus, PRODUCT_TYPE productType)
        throws DCPEException, IOException {
        log.info("SMSProxy:generateFixedNumberMessageRequest contractNo={}|connectionStatus={}|productType={}",payment.getContractNo(),connectionStatus.name(),productType.name());
        if (connectionStatus.orEqual(ConnectionStatus.C,ConnectionStatus.D)) {
            addMessageText(SMS_MESSAGE.ACTIVE_DBN_CX_VALID_PAY,smsRequest,payment);
        } else if (connectionStatus.orEqual(ConnectionStatus.B,ConnectionStatus.T)) {

            String disconnectReason = getDisconnectReason(payment.getContractNo());
            log.info("SMSProxy:generateFixedNumberMessage Temporary Disconnected Fixed Number, Disconnected Reason : {}", disconnectReason);

            if (Arrays.stream(config.getFixedNpDisconReason()).anyMatch(disconnectReason::equals) || Arrays.stream(config.getFixedCleDisconReason()).anyMatch(disconnectReason::equals)) {

                // Get Minimum Amount to connect
                String connRef = payment.getConnRef();
                boolean isMinPayNull = true;
                if (productType == PRODUCT_TYPE.VOLTE) {

                    isMinPayNull = generateFixedMinPayMessage(smsRequest,payment,connRef,disconnectReason);
                }

                if (isMinPayNull) {
                    addMessageText(SMS_MESSAGE.ACTIVE_DBN_CX_VALID_PAY,smsRequest,payment);
                }
            } else {
                addMessageText(SMS_MESSAGE.ACTIVE_DBN_CX_VALID_PAY,smsRequest,payment);
            }
        }
        log.info("SMSProxy:generateFixedNumberMessageRespons success contractNo={}",payment.getContractNo());
    }

    private boolean generateFixedMinPayMessage(SmsRequest smsRequest,Payment payment, String connRef,String disconnectReason)
        throws DCPEException, IOException {
        log.info("SMSProxy:generateFixedMinPayMessageRequest contractNo={}|connectionRef={}|dosconnectReason={}",payment.getContractNo(),payment.getConnRef(),disconnectReason);
        boolean isMinPayNull =true;
        CcbsMinPayResponse minPayResp = mifeService.getCcbsMinPayment(connRef.startsWith("0") ? connRef.substring(1) : connRef,String.valueOf(payment.getTrxID()));
        if (minPayResp.getResponse()!=null && minPayResp.getResponse().getMinPayInfo().getExecutionStatus().equalsIgnoreCase("00")) {
            double minPaymentToReconnect = Double.parseDouble(minPayResp.getResponse().getMinPayInfo().getMinimumAmount());
            isMinPayNull =false;

            if (Arrays.stream(config.getFixedCleDisconReason()).anyMatch(disconnectReason::equals)) {

                setFixedCleDisconnectReasonMessage(smsRequest,payment,minPaymentToReconnect);

            } else if (Arrays.stream(config.getFixedNpDisconReason()).anyMatch(disconnectReason::equals)){
                // Actual totalAmount to be paid is get from min pay service - minPaymentToReconnect, Need to get the minimum value which consider when reconnecting

                Double totAmountToPay = minPaymentToReconnect;
                Double minAmountToPay = getGsmOrVolteMinAmountToPay(payment,disconnectReason);
                log.info("SmsProxy:generateFixedMinPayMessageResponse Minimum Amount to Pay for VOLTE Reconnect ={}|contractId={}|connectionReference={}",minAmountToPay,payment.getContractNo(),payment.getConnRef());

                if (minAmountToPay > 100) {
                    //one old logic removed since it always evaluate to true
                    addMessageTextWithReconnectAmount(SMS_MESSAGE.INACTIVE_VOLTE_CX_IN_SUFFICIENT_PAY,smsRequest,payment,totAmountToPay);

                } else {
                    addMessageText(SMS_MESSAGE.INACTIVE_DBN_CX_SUFFICIENT_PAY,smsRequest,payment);
                }
            }else{
                addMessageText(SMS_MESSAGE.ACTIVE_DBN_CX_VALID_PAY,smsRequest,payment);
            }
            log.info("SMSProxy:generateFixedMinPayMessageResponse Success contractNo={}",payment.getContractNo());
        }
        return isMinPayNull;
    }

    private void setFixedCleDisconnectReasonMessage(SmsRequest smsRequest, Payment payment, double minPaymentToReconnect){
        log.info("SMSProxy:setFixedCleDisconnectReasonMessageRequest contractNo={}|payedAmount={}|minPaymentToReconnect={}",payment.getContractNo(),payment.getAccountPaymentMny(),minPaymentToReconnect);
        double feeToConnect = 0;
        if (getOcsCommandRead(payment.getTrxID()).equalsIgnoreCase("S")) {
            feeToConnect = minPaymentToReconnect;
        } else {
            feeToConnect = minPaymentToReconnect - payment.getAccountPaymentMny();
        }

        if (feeToConnect > 0) {
            //one old logic removed since it always evaluate to true
            addMessageTextWithReconnectAmount(SMS_MESSAGE.INACTIVE_VOLTE_CX_IN_SUFFICIENT_PAY,smsRequest,payment,feeToConnect);

        } else {
            addMessageText(SMS_MESSAGE.INACTIVE_DBN_CX_SUFFICIENT_PAY,smsRequest,payment);
        }
        log.info("SMSProxy:setFixedCleDisconnectReasonMessageResponse Success contractNo={}|payedAmount={}|minPaymentToReconnect={}|feeToConnect={}",payment.getContractNo(),payment.getAccountPaymentMny(),minPaymentToReconnect,feeToConnect);

    }

    private void sendSmsRequestToCustomer(SmsRequest smsRequest, Payment payment, SBUEnum sbu , PAYMENT_SYSTEM paymentSystem ){
        log.info("SMSPRoxy:sendSmsRequestToCustomerRequest contractNo={}|sbu={}|paymentSystem={}",payment.getContractNo(),sbu.name(),paymentSystem.name());
        SmsRequest discSmsRequest = new SmsRequest();
        discSmsRequest.setConnRef(payment.getConnRef());
        discSmsRequest.setPhoneNo(smsRequest.getPhoneNo());
        discSmsRequest.setMessage(config.getMessageForDiscount());

        if (smsRequest.getPhoneNo() != null && smsRequest.getMessage() != null && (!isNotAllowSMS(payment.getContractNo(),payment.getPaymentMode(),sbu) || paymentSystem == PAYMENT_SYSTEM.TELBIZ )) {
            log.info("SmsProxy:sendSmsRequestToCustomer sending SMS message phoneNo={}|contractNo={}|message={}",smsRequest.getPhoneNo(),payment.getContractNo(),smsRequest.getMessage());
            sendSMS(smsRequest);
        } else {
            log.info("SmsProxy:sendSmsRequestToCustomerResponse SMS not sent : phone no(={}) null or Credit Type/Payment Mode not allowed or SMS message[({})] is null",smsRequest.getPhoneNo(),smsRequest.getMessage());
        }

        if (config.getIsSendDiscountSMS() !=null && config.getIsSendDiscountSMS().equals("Y")) {
            String receiptBranch=payment.getReceiptBranch();

            if (discSmsRequest.getPhoneNo() != null && discSmsRequest.getMessage() != null && isAllowDiscSMS(receiptBranch, payment.getContractNo())) {
                log.info("SmsProxy:sendSmsRequestToCustomer sending Discount SMS message phoneNo={}|contractNo={}|message={}",discSmsRequest.getPhoneNo(),payment.getContractNo(),discSmsRequest.getMessage());
                sendSMS(discSmsRequest);
            }
        }
        log.info("SMSPRoxy:sendSmsRequestToCustomerResponse success contractNo={}|sbu={}|paymentSystem={}",payment.getContractNo(),sbu.name(),paymentSystem.name());
    }

    private void setSmsRequestPhoneNo(SmsRequest smsRequest,Payment payment,ConnectionStatus connectionStatus,SBUEnum sbu){
        log.info("SMSPRoxy:setSmsRequestPhoneNoRequest contractNo={}|sbu={}|connectionStatus={}",payment.getContractNo(),sbu!=null?sbu.name():"null",connectionStatus.name());

        if (sbu != null && sbu.name().startsWith("DTV")){
            smsRequest.setPhoneNo(getDtvContactNoForContractNo(payment.getContractNo()));
        }else if (sbu != null && sbu.name().matches("GSM")) {
            if (connectionStatus == ConnectionStatus.D && payment.getContactNo() != null && !payment.getContactNo().isEmpty()){
                smsRequest.setPhoneNo(payment.getContactNo());
            }else{
                smsRequest.setPhoneNo(payment.getConnRef());
            }
        }else{
            smsRequest.setPhoneNo(getVolteContactNoForContractNo(payment.getContractNo()));
        }
        log.info("SMSProxy:setSmsRequestPhoneNoResponse success contractNo={}|sbu={}|connectionStatus={}|deliveryPhoneNo={}",payment.getContractNo(),sbu!=null?sbu.name():"null",connectionStatus.name(),smsRequest.getPhoneNo());

    }
//----------------------------------------------------
    @Override
    public void sendSmsProxy(Payment payment, PAYMENT_SYSTEM paymentSystem) {
        ConnectionStatus connectionStatus = ConnectionStatus.getConnectionStatus(payment.getConnectionStatus());
        SBUEnum sbu = SBUEnum.getSBU(payment.getSbu());
        PRODUCT_TYPE productType = PRODUCT_TYPE.getProductTypeByValue(payment.getProductType());
        log.info("sendSmsProxyRequest paymentSystem={}|contractNo={}|connectionReference={}|payedAmount={}|sbu={}|productType={}|connectionStatus={}",paymentSystem.name(),payment.getContractNo(),payment.getConnRef(),payment.getAccountPaymentMny(),sbu,productType,connectionStatus);
        try {
            SmsRequest smsRequest = new SmsRequest();
            smsRequest.setConnRef(payment.getConnRef());

            if ( PAYMENT_SYSTEM.RBM_CHQ == paymentSystem) {
                addMessageText(SMS_MESSAGE.UNSUCCESSFUL_GSM_PAY,smsRequest,payment);
            }
            else if (sbu != null && sbu.name().startsWith("DTV")) {
                generateDtvMessage(smsRequest,payment,sbu,connectionStatus);

            } else if (sbu != null && sbu.name().matches("GSM")) {
                generateGsmMessage(smsRequest,payment,connectionStatus);

            } else if (( paymentSystem == PAYMENT_SYSTEM.TELBIZ && productType == PRODUCT_TYPE.LTE) || (paymentSystem == PAYMENT_SYSTEM.RBM && productType == PRODUCT_TYPE.VOLTE )) {
                /* Fixed Numbers */
                generateFixedNumberMessage(smsRequest,payment,connectionStatus,productType);
            }

            setSmsRequestPhoneNo(smsRequest,payment,connectionStatus,sbu);
            sendSmsRequestToCustomer(smsRequest,payment,sbu,paymentSystem);

        } catch (Exception ex) {
            log.error("sendSmsProxy send SMS failed contractNo={}|connectionReferance={} error={}",payment.getContractNo(),payment.getConnRef(),ex.getMessage(),ex);
        }
        log.info("sendSmsProxyResponse Success paymentSystem={}|contractNo={}",paymentSystem.name(),payment.getContractNo(),payment.getConnRef());
    }

    public String getVolteContactNoForContractNo(String contractNo) {
        log.info("SMSProxy:getVolteContactNoForContractNo contractNo:{}",  contractNo);
        try {
            return ccbsJdbcTemplate.queryForObject(QueryConfig.getQueryByKey("SQL_PE_VOLTE_CONTACT_NO"), new Object[]{contractNo}, String.class);
        } catch (EmptyResultDataAccessException dataEx) {
            log.error("No mapping found for given contractNo:" + contractNo, dataEx);
            return null;
        }
    }

    private void addDTVOfferActRecord(Long txnId,String contractNo, double amount, String connectionStatus) {

        /**
         * This method is used to insert payment records of DTV POSTPAID Payments to add DTV Offer activation after a payment
         */
        try {
            String sqlDtvOfferRecInsert = QueryConfig.getQueryByKey("SQL_INS_DTV_OFFER_ACT_REC");
            ccbsJdbcTemplate.update(sqlDtvOfferRecInsert, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {

                    ps.setLong(1, txnId);
                    ps.setString(2, contractNo);
                    ps.setDouble(3, amount);
                    ps.setString(4, connectionStatus);
                }
            });
            log.info("SMSProxy:Record added to activate DTV Offer - txnId:{},contractId:{},amount:{},connectionStatus:{}",txnId,contractNo,amount,connectionStatus);
        } catch (EmptyResultDataAccessException dataEx) {
            log.error("SMSProxy:Error in inserting record to activate DTV Offer - TxnId:{}, error:{}", txnId, dataEx.getMessage());
        }
    }

    public String getDtvContactNoForContractNo(String contractNo) {
        log.info("SMSProxy:getDtvContactNoForContractNoRequest contractNo={}", contractNo);
        String contactNo =null;
        try {
            contactNo = ccbsJdbcTemplate.queryForObject(QueryConfig.getQueryByKey("SQL_PE_DTV_CONTACT_NO"), new Object[] { contractNo,contractNo,contractNo }, String.class);
        } catch (EmptyResultDataAccessException dataException) {
            log.error("SMSProxy:getDtvContactNoForContractNoResponse No mapping found for given contractNo={}|error={}"+ contractNo, dataException.getMessage(),dataException);
        }
        log.info("SMSProxy:getDtvContactNoForContractNoResponse Success contractNo={}|contactNo={}",contractNo,contactNo);
        return contactNo;
    }

    private String getCreditType(String contractNo) {
        log.info("SMSProxy:getCreditTypeRequest contractNo={}", contractNo);
        String creditType =null;
        try {
            creditType = ccbsJdbcTemplate.queryForObject(QueryConfig.getQueryByKey("SQL_SEL_CRE_TYPE"), new Object[] { contractNo }, String.class);
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException dataException) {
            log.error("SMSProxy:getCreditTypeResponse No credit type found for given contractNo={}|error={}" + contractNo, dataException.getMessage(),dataException);
        }
        log.info("SMSProxy:getCreditTypeResponse Success contractNo={}|creditType={}", contractNo,creditType);
        return creditType;
    }

    private boolean isAllowDiscSMS(String receiptBranch,String contractId){
        log.info("SMSProxy:isAllowDiscSMSRequest contractNo={}|receiptBranch={}", contractId,receiptBranch);
        boolean isAllowDiscSMS=false;
        /**
         * Check receiptBranch for Digital Type
         */
        if(Arrays.stream(config.getDiscountBranch()).parallel().anyMatch(receiptBranch::matches)){
            /**
             * Check for valid IdTypes
             */
            String idType =null ;

            try {
                idType = ccbsJdbcTemplate.queryForObject(QueryConfig.getQueryByKey("SQL_GET_ID_TYPE_BY_CONTRACT"),
                        new Object[]{contractId}, String.class);

            } catch (Exception e) {
                log.error("SMSProxy:isAllowDiscSMS Error in getting Id Type  contractId={}|error={}" + contractId,e.getMessage(),e);
            }
            if (idType!=null && Arrays.stream(config.getDiscountIdType()).parallel().anyMatch(idType::matches)) {
                log.info("SMSProxy:isAllowDiscSMS Valid Id Type : {}",idType);
                isAllowDiscSMS= true;
            }else{
                log.info("SMSProxy:isAllowDiscSMS Id Type is not valid for Discount: {}",idType);
                isAllowDiscSMS= false;
            }
        }else{
            log.info("SMSProxy:isAllowDiscSMS Channel is not valid for Discount: {}",receiptBranch);
        }
        log.info("SMSProxy:isAllowDiscSMSRequest contractNo={}|isAllowDiscSMS={}", contractId,receiptBranch,isAllowDiscSMS);
        return isAllowDiscSMS;
    }

    private boolean isNotAllowSMS(String contractNo, String paymentMode, SBUEnum sbu) {
        log.info("SMSProxy:isNotAllowSMSRequest contractNo={}|paymentMode={}|sbu={}", contractNo,paymentMode,sbu.name());
        String creType = getCreditType(contractNo);
        try {
            int connRefNoExclusion = ccbsJdbcTemplate.queryForObject(QueryConfig.getQueryByKey("SQL_SEL_CONN_REF_NO_EXCLUSION"),
                    new Object[]{contractNo}, Integer.class);
            if (connRefNoExclusion > 0) {
                log.info("SMSProxy:isNotAllowSMSRequest Connection Ref exclusion found");
                return true;
            }
        } catch (Exception e) {
            log.error("SMSProxy:isNotAllowSMSRequest SMS Exclusion Check error contractNo={}|error={} " + contractNo,e.getMessage(),e);
        }
        if (creType == null || paymentMode==null) {
            log.error("SMSProxy:isNotAllowSMSRequest creType(={}) or paymentMode(={}) is null contractNo={}" + contractNo,creType,paymentMode);
            return false;
        }
        if(Arrays.stream(config.getExcludePaymentModes()).parallel().anyMatch(paymentMode::contains)){
            log.info("Payment Mode exclusion found, {}",paymentMode);
            return true;
        }
        boolean isNotAllowSms = true;
        switch (sbu) {
            case DTV_POSTPAID:
                isNotAllowSms = Arrays.stream(config.getExcludeDtvCreditTypes()).parallel().anyMatch(creType::contains);
                break;
            case GSM:
                isNotAllowSms = Arrays.stream(config.getExcludeMobileCreditTypes()).parallel().anyMatch(creType::contains);
                break;
            case DBN:
                isNotAllowSms = Arrays.stream(config.getExcludeDbnCreditTypes()).parallel().anyMatch(creType::contains);
                break;
            default:
                isNotAllowSms = true;
        }
        log.info("SMSProxy:isNotAllowSMSResponse contractNo={}|isNotAllowSms={}", contractNo,isNotAllowSms);

        return isNotAllowSms;
    }
    @Override
    public String insertOtcPayment(Payment payment) {
        log.info("insertOtcPaymentRequest contractNo={}|productCategory={}",payment.getContractNo(), ProductCategoryEnum.getProductCategoryByValue(payment.getProductCategory()));
        String otcPaymentStatus = null;
        try {
            if(payment.getProductCategory() == ProductCategoryEnum.TELBIZ.getCategory() && payment.getProductType() == PRODUCT_TYPE.DCS.getValue()) {
                // Changed the code to insert OTC payments to DCS schema , When the product Type is DCS(6), ReasonCode for DCS numbers is 814

                MiscellaneousChargeRequest reqData= new MiscellaneousChargeRequest();
                Date transDate = new Date();
                reqData.setAccountNo(payment.getContractNo());
                reqData.setAmount(config.getSurchargeAmount());
                reqData.setReasonCode(config.getSurchargeDcsReasonCode());
                reqData.setTransactionDate(dateFormat.format(transDate));

                MiscellaneousChargeResponse miscCharge = mifeService.addMiscellaneousCharge(reqData,String.valueOf(payment.getTrxID()));

                if(miscCharge.getStatus() !=null && miscCharge.getStatus().equalsIgnoreCase("SUCCESS")) {
                    otcPaymentStatus="OK";
                }else{
                    otcPaymentStatus=miscCharge.getStatus();
                }

            }else {
                // VOLTE(productType-5) OTC payments are added to CCBS schema
                /* CCBS OTC */
                List<SqlParameter> paramList = new ArrayList<>();
                paramList.add(new SqlOutParameter("wk_rbm_out", Types.VARCHAR)); // wk_tran_no
                paramList.add(new SqlParameter(Types.VARCHAR)); // p_tran_type
                paramList.add(new SqlParameter(Types.DATE)); // p_ccbs_transaction_date
                paramList.add(new SqlParameter(Types.VARCHAR)); // p_inserted_by
                paramList.add(new SqlParameter(Types.INTEGER)); // p_subsidiary_node_id
                paramList.add(new SqlParameter(Types.INTEGER)); // p_rbm_acccount_id
                paramList.add(new SqlParameter(Types.DOUBLE)); // p_amount
                paramList.add(new SqlParameter(Types.VARCHAR)); // p_reason_code
                paramList.add(new SqlParameter(Types.VARCHAR)); // p_ocs_integration_required
                paramList.add(new SqlParameter(Types.VARCHAR)); // p_installment_scheme
                paramList.add(new SqlParameter(Types.DOUBLE)); // p_down_payment
                paramList.add(new SqlParameter(Types.INTEGER)); // p_no_of_installments
                paramList.add(new SqlParameter(Types.VARCHAR)); // p_ocs_text
                paramList.add(new SqlParameter(Types.VARCHAR)); // p_remark

                final String procedureCall = QueryConfig.getQueryByKey("SQL_PE_OTC_PAYMENT");
                Map<String, Object> resultMap = ccbsJdbcTemplate.call(new CallableStatementCreator() {
                    @Override
                    public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                        CallableStatement callableStatement = connection.prepareCall(procedureCall);
                        callableStatement.registerOutParameter(1, Types.VARCHAR);
                        callableStatement.setString(2, config.getSurchargeTranType());
                        callableStatement.setString(3, config.getSurchargeInsertedBy());
                        callableStatement.setInt(4, Integer.parseInt(payment.getContractNo()));
                        callableStatement.setInt(5, Integer.parseInt(payment.getContractNo()));
                        callableStatement.setDouble(6, config.getSurchargeAmount());
                        callableStatement.setString(7, config.getSurchargeReasonCode());
                        callableStatement.setNull(8, Types.VARCHAR);
                        callableStatement.setNull(9, Types.VARCHAR);
                        callableStatement.setNull(10, Types.DOUBLE);
                        callableStatement.setNull(11, Types.INTEGER);
                        callableStatement.setNull(12, Types.VARCHAR);
                        callableStatement.setNull(13, Types.VARCHAR);

                        return callableStatement;
                    }
                }, paramList);
                otcPaymentStatus = ((String) resultMap.get("wk_rbm_out"));
                log.info("insertOtcPayment SQL_CCBS_OTC_PAYMENT :{} for contract number :{}",otcPaymentStatus, payment.getContractNo());
            }
        } catch (Exception ex) {
            log.info("insertOtcPayment failed due to :{} for contract number :{}", ex , payment.getContractNo());
        }
        log.info("insertOtcPaymentResponse contractNo={}|otcPaymentStatus={}",payment.getContractNo(),otcPaymentStatus);

        return otcPaymentStatus;
    }

    public String getOcsCommandRead(Long reqId){
        log.info("getOcsCommandReadRequest reqId={}");
        String commandRead = peJdbcTemplate.queryForObject(QueryConfig.getQueryByKey("SQL_OCS_COMMAND_STATUS"), new Object[]{reqId,reqId}, String.class);
        log.info("getOcsCommandReadResponse reqId={}|commandRead={}",reqId,commandRead);
        return commandRead;
    }

    public String getOcsHistoryCommandRead(Long reqId){
        log.info("getOcsHistoryCommandReadRequest reqId={}");
        String commandRead = peJdbcTemplate.queryForObject(QueryConfig.getQueryByKey("SQL_OCS_H_COMMAND_STATUS"), new Object[]{reqId}, String.class);
        log.info("getOcsHistoryCommandReadResponse reqId={}|commandRead={}",reqId,commandRead);
        return commandRead;
    }
}
