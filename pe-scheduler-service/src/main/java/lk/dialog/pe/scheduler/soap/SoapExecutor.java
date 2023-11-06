package lk.dialog.pe.scheduler.soap;


import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.dto.QueryPaymentsSummaryRequest;
import lk.dialog.pe.scheduler.exception.NEException;
import lk.dialog.pe.scheduler.soap.custom.ApplicationExceptionError;
import lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub;
import lk.dialog.pe.scheduler.soap.custom.ParameterExceptionError;
import lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub;
import lk.dialog.pe.scheduler.soap.payment.NullParameterExceptionError;
import lk.dialog.pe.scheduler.soap.stub.invoker.CUSTOMECADBTCustomECAStubInvoker;
import lk.dialog.pe.scheduler.util.NEConstants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.impl.httpclient3.HttpTransportPropertiesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class SoapExecutor {

    @Value("${billingconfig.ecaep}")
    private String ecaEP;
    @Value("${billingconfig.customep}")
    private String customEP;
    @Value("${billingconfig.username}")
    private String userName;
    @Value("${billingconfig.pwd}")
    private String pwd;
    @Value("${billingconfig.readtimeout}")
    private Long readTimeoutPeriod;

    @Value("${poolconfig.maxtotal}")
    private String maxTotal;
    @Value("${poolconfig.maxidle}")
    private String maxIdle;
    @Value("${poolconfig.minidle}")
    private String minIdle;
    @Value("${poolconfig.waiting}")
    private String waiting;
    @Value("${poolconfig.timeOutInMilliSeconds}")
    private String timeOutInMilliSeconds;
    @Value("${poolconfig.testOnBorrow}")
    private String testOnBorrow;
    @Value("${poolconfig.testOnReturn}")
    private String testOnReturn;

    CUSTOMECADBTCustomECAStubInvoker customECADBTCustomECAStubInvoker=null;
    CUSTOMECADBTCustomECAStub customECAStub=null;
    ECAPaymentStub ecaPaymentStub;

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapExecutor.class);

    public int postPayment(PaymentDTO paymentDTO)
            throws RemoteException, ApplicationExceptionError, ParameterExceptionError, NEException {

        LOGGER.info("Calling postPayment. Contract No::{}, Trx ID::{}" ,paymentDTO.getContractNo(),paymentDTO.getTrxID());
        int phyPaymentSeq = -1;

        final CUSTOMECADBTCustomECAStub.DbtCreatePhysicalPaymentAndAccountPayments dbtCreatePhysicalPaymentAndAccountPayments34 = new CUSTOMECADBTCustomECAStub.DbtCreatePhysicalPaymentAndAccountPayments();
        final CUSTOMECADBTCustomECAStub.DbtCreatePhysicalPaymentAndAccountPaymentsInput dbtCreatePhysicalPaymentAndAccountPaymentsInput = new CUSTOMECADBTCustomECAStub.DbtCreatePhysicalPaymentAndAccountPaymentsInput();
        final CUSTOMECADBTCustomECAStub.DbtNewPhysicalPaymentData dbtNewPhysicalPaymentData = new CUSTOMECADBTCustomECAStub.DbtNewPhysicalPaymentData();
        final CUSTOMECADBTCustomECAStub.NewPhysicalPaymentData_2 newPhysicalPaymentData2 = new CUSTOMECADBTCustomECAStub.NewPhysicalPaymentData_2();
        final lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.CustomerPK customerPK = new lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.CustomerPK();
        customerPK.setCustomerRef(paymentDTO.getAccountNo());
        newPhysicalPaymentData2.setCustomerPK(customerPK);
        newPhysicalPaymentData2.setPhysicalPaymentDat(paymentDTO.getAccountPaymentDate());
        newPhysicalPaymentData2.setPaymentMny(Math.round(paymentDTO.getAccountPaymentMny() * 1000));
        newPhysicalPaymentData2.setPaymentCurrency(paymentDTO.getPaymentCurrency());

        newPhysicalPaymentData2.setPaymentMethodId(paymentDTO.getPaymentMethodID());
        if (paymentDTO.getAccountPaymentText() != null) {
            newPhysicalPaymentData2.setPhysicalPaymentText(paymentDTO.getAccountPaymentText());
        }
        if (paymentDTO.getAccountPaymentRef() != null) {
            newPhysicalPaymentData2.setPaymentRef(paymentDTO.getAccountPaymentRef().length() > 40
                    ? paymentDTO.getAccountPaymentRef().substring(0, 39) : paymentDTO.getAccountPaymentRef());
        }
        final Calendar createdDtm = Calendar.getInstance();
        newPhysicalPaymentData2.setCreatedDtm(createdDtm);
        // newPhysicalPaymentData2.setPaymentRequestId();
        // newPhysicalPaymentData2.setRefundBoo();
        dbtNewPhysicalPaymentData.setNewPhysicalPaymentData(newPhysicalPaymentData2);
        dbtNewPhysicalPaymentData.setApplyExtDetToAllAccPayBoo(true);
        final CUSTOMECADBTCustomECAStub.DbtPaymentExtensionData dbtPaymentExtensionData = new CUSTOMECADBTCustomECAStub.DbtPaymentExtensionData();
        dbtPaymentExtensionData.setReceiptNumber(paymentDTO.getReceiptNo());
        dbtPaymentExtensionData.setReceiptBranchName(paymentDTO.getReceiptBranch());
        dbtPaymentExtensionData.setBranchCounter(paymentDTO.getBranchCounter());
        dbtPaymentExtensionData.setReceiptUser(paymentDTO.getReceiptUser());
        dbtPaymentExtensionData.setReceiptDate(paymentDTO.getReceiptDate());
        dbtPaymentExtensionData.setPaymentMode(paymentDTO.getPaymentMode());
        dbtPaymentExtensionData
                .setPaymentRefNo(
                        paymentDTO.getPaymentRefNo() != null
                                ? paymentDTO.getPaymentRefNo().length() > 255
                                ? paymentDTO.getPaymentRefNo().substring(0, 254) : paymentDTO.getPaymentRefNo()
                                : null);
        dbtPaymentExtensionData.setRemarks(
                paymentDTO.getRemarks() != null ? paymentDTO.getRemarks().replaceAll("\\p{Cntrl}", "").trim() : null);
        dbtPaymentExtensionData
                .setStatusReason(paymentDTO.getStatusReason() != null ? paymentDTO.getStatusReason() : null);

        final String[] paymentExtAttributeArray = new String[15];
        if (NEConstants.PAY_MODE_CHEQ.equalsIgnoreCase(paymentDTO.getPaymentMode())) {
            paymentExtAttributeArray[0] = paymentDTO.getChequebankCode();
            paymentExtAttributeArray[1] = paymentDTO.getChequebankBranchCode();
            paymentExtAttributeArray[2] = paymentDTO.getChequeNo();
            // paymentExtAttributeArray[3] = ; //Cheque Tran No
            // paymentExtAttributeArray[4] = ; // Cheque reference
            // paymentExtAttributeArray[5] = ; // Cheque direction
            paymentExtAttributeArray[6] = paymentDTO.getTerminalID();
        } else if (NEConstants.PAY_MODE_CARD.equalsIgnoreCase(paymentDTO.getPaymentMode())) {
            paymentExtAttributeArray[0] = paymentDTO.getCcType();
            paymentExtAttributeArray[1] = paymentDTO.getCcBankCode();
            paymentExtAttributeArray[2] = paymentDTO.getCcNo();
            paymentExtAttributeArray[3] = paymentDTO.getCcApprovalCode();
            paymentExtAttributeArray[6] = paymentDTO.getTerminalID();
        } else {
            paymentExtAttributeArray[6] = paymentDTO.getTerminalID();
        }

        dbtPaymentExtensionData.setPaymentExtAttribute(paymentExtAttributeArray);
        dbtNewPhysicalPaymentData.setDbtPaymentExtensionData(dbtPaymentExtensionData);
        dbtCreatePhysicalPaymentAndAccountPaymentsInput.setNewPhysicalPaymentData(dbtNewPhysicalPaymentData);

        final CUSTOMECADBTCustomECAStub.DbtNewAccountPaymentData[] dbtNewAccountPaymentDataArr = new CUSTOMECADBTCustomECAStub.DbtNewAccountPaymentData[1];
        final CUSTOMECADBTCustomECAStub.DbtNewAccountPaymentData dbtNewAccountPaymentData = new CUSTOMECADBTCustomECAStub.DbtNewAccountPaymentData();

        final lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.AccountPK accountPK = new lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.AccountPK();
        accountPK.setAccountNum(paymentDTO.getContractNo());
        dbtNewAccountPaymentData.setAccountPK(accountPK);
        dbtNewAccountPaymentData.setDbtPaymentExtensionData(dbtPaymentExtensionData);

        final CUSTOMECADBTCustomECAStub.NewAccountPaymentData_4 newAccountPaymentData4 = new CUSTOMECADBTCustomECAStub.NewAccountPaymentData_4();
        newAccountPaymentData4.setCreatedDtm(createdDtm);
        newAccountPaymentData4.setPaymentCurrency(paymentDTO.getPaymentCurrency());
        newAccountPaymentData4.setWhenDtm(paymentDTO.getCreatedDtm());
        newAccountPaymentData4.setPaymentMny(Math.round(paymentDTO.getAccountPaymentMny() * 1000));
        newAccountPaymentData4.setPaymentTxt(paymentDTO.getAccountPaymentText());
        newAccountPaymentData4.setPaymentRef(paymentDTO.getAccountPaymentRef() != null
                ? (paymentDTO.getAccountPaymentRef().length() > 40 ? paymentDTO.getAccountPaymentRef().substring(0, 39)
                : paymentDTO.getAccountPaymentRef())
                : null);

        newAccountPaymentData4.setCustomerPK(customerPK);
        newAccountPaymentData4.setPaymentMethodId(paymentDTO.getPaymentMethodID());
        // newAccountPaymentData4.setPaymentRule();
        // newAccountPaymentData4.setBatchId();
        // newAccountPaymentData4.setDepositRequestSeq();
        // newAccountPaymentData4.setDepositBalanceSeq();
        // newAccountPaymentData4.setRefundBoo();
        dbtNewAccountPaymentData.setNewAccountPaymentData(newAccountPaymentData4);
        dbtNewAccountPaymentDataArr[0] = dbtNewAccountPaymentData;

        dbtCreatePhysicalPaymentAndAccountPaymentsInput.setNewAccountPaymentDataSuperArray(dbtNewAccountPaymentDataArr);
        dbtCreatePhysicalPaymentAndAccountPayments34
                .setDbtCreatePhysicalPaymentAndAccountPayments(dbtCreatePhysicalPaymentAndAccountPaymentsInput);

        customECADBTCustomECAStubInvoker = new CUSTOMECADBTCustomECAStubInvoker();
        customECADBTCustomECAStubInvoker.setUp(customEP + "/CUSTOMECADBTCustomECAPort",userName, pwd, timeOutInMilliSeconds, maxIdle, maxTotal, minIdle, waiting, testOnBorrow, testOnReturn);
        try {

            customECAStub = customECADBTCustomECAStubInvoker.getCUSTOMECADBTCustomECAStub();
            final CUSTOMECADBTCustomECAStub.DbtCreatePhysicalPaymentAndAccountPaymentsOutputE dbtCreatePhyPayAndAccPayOutputE = customECAStub
                    .dbtCreatePhysicalPaymentAndAccountPayments(dbtCreatePhysicalPaymentAndAccountPayments34);
            if (dbtCreatePhyPayAndAccPayOutputE != null) {
                final CUSTOMECADBTCustomECAStub.DbtCreatePhysicalPaymentAndAccountPaymentsOutput dbtCreatePhyPayAndAccPaysOutput = dbtCreatePhyPayAndAccPayOutputE
                        .getDbtCreatePhysicalPaymentAndAccountPaymentsOutput();
                if (dbtCreatePhyPayAndAccPaysOutput != null) {
                    final CUSTOMECADBTCustomECAStub.DbtCreatePhyPayAndAccPayOutput dbtCreatePhyPayAndAccPayOutput = dbtCreatePhyPayAndAccPaysOutput
                            .getCreatePhyPayAndAccPayResult();
                    if (dbtCreatePhyPayAndAccPayOutput != null) {
                        final CUSTOMECADBTCustomECAStub.PhysicalPaymentPK physicalPaymentPK = dbtCreatePhyPayAndAccPayOutput
                                .getPhysicalPaymentPK();
                        if (physicalPaymentPK != null) {
                            phyPaymentSeq = physicalPaymentPK.getPhysicalPaymentSeq();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("No such customer"))
                throw new NEException("CUTOMER_NOT_FOUND");
            else
                throw new RemoteException(ex.getMessage(), ex);
        }finally{
            customECADBTCustomECAStubInvoker.returnCUSTOMECADBTCustomECAStub(customECAStub);
        }

        LOGGER.info("Payment post success. Contract No::{}, Trx ID::{}, RBM Pay Seq::{}", paymentDTO.getContractNo(),
                paymentDTO.getTrxID(), phyPaymentSeq);
        return phyPaymentSeq;
    }


    public int postChqInSuspendState(PaymentDTO paymentDTO)
            throws RemoteException, ApplicationExceptionError, ParameterExceptionError, NEException {
        LOGGER.info("Calling postChqInSuspendState. Chq No::{}, Trx ID::{}", paymentDTO.getChequeNo(),
                paymentDTO.getTrxID());
        final SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        final CUSTOMECADBTCustomECAStub.DbtPostChequeInSuspendStatus dbtPostChequeInSuspendStatus62 = new CUSTOMECADBTCustomECAStub.DbtPostChequeInSuspendStatus();
        final CUSTOMECADBTCustomECAStub.DbtPostChequeInSuspendStatusInput dbtPostChequeInSuspendStatusInput = new CUSTOMECADBTCustomECAStub.DbtPostChequeInSuspendStatusInput();
        final lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.AccountPK accountPK = new lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.AccountPK();
        accountPK.setAccountNum(paymentDTO.getContractNo());
        dbtPostChequeInSuspendStatusInput.setAccountPK(accountPK);
        final lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.CustomerPK customerPK = new lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.CustomerPK();
        customerPK.setCustomerRef(paymentDTO.getAccountNo());
        dbtPostChequeInSuspendStatusInput.setCustomerPK(customerPK);
        dbtPostChequeInSuspendStatusInput.setAccountPaymentMny(Math.round(paymentDTO.getAccountPaymentMny() * 1000));
        dbtPostChequeInSuspendStatusInput.setBankCode(paymentDTO.getChequebankCode());
        dbtPostChequeInSuspendStatusInput.setBankCounter(paymentDTO.getBranchCounter());
        dbtPostChequeInSuspendStatusInput.setBranchCode(paymentDTO.getChequebankBranchCode());
        if (paymentDTO.getAccountPaymentDate() != null)
            dbtPostChequeInSuspendStatusInput.setChequeDat(sd.format(paymentDTO.getAccountPaymentDate().getTime()));
        else {
            if (paymentDTO.getChequeDate() != null) {
                dbtPostChequeInSuspendStatusInput.setChequeDat(sd.format(paymentDTO.getChequeDate().getTime()));
            }
        }
        dbtPostChequeInSuspendStatusInput.setChequeMny(Math.round(paymentDTO.getAccountPaymentMny() * 1000));
        if(paymentDTO.getChequeNo() != null) {
            dbtPostChequeInSuspendStatusInput.setChequeNum(paymentDTO.getChequeNo());
        }
        dbtPostChequeInSuspendStatusInput.setChequeTransactionNum(Long.toString(paymentDTO.getTrxID()));
        // dbtPostChequeInSuspendStatusInput.setChequeRef();
        // dbtPostChequeInSuspendStatusInput.setChequeDirection();
        dbtPostChequeInSuspendStatusInput.setReceiptBranch(paymentDTO.getReceiptBranch());
        dbtPostChequeInSuspendStatusInput.setReceiptNum(paymentDTO.getReceiptNo());
        dbtPostChequeInSuspendStatusInput.setReceiptType(NEConstants.RECEIPT_TYPE);
        dbtPostChequeInSuspendStatusInput.setReceiptUser(paymentDTO.getReceiptUser());
        dbtPostChequeInSuspendStatusInput.setTerminalID(paymentDTO.getTerminalID());
        dbtPostChequeInSuspendStatus62.setDbtPostChequeInSuspendStatus(dbtPostChequeInSuspendStatusInput);

        customECADBTCustomECAStubInvoker = new CUSTOMECADBTCustomECAStubInvoker();
        customECADBTCustomECAStubInvoker.setUp(customEP + "/CUSTOMECADBTCustomECAPort",userName, pwd, timeOutInMilliSeconds, maxIdle, maxTotal, minIdle, waiting, testOnBorrow, testOnReturn);

        try {
            customECAStub = customECADBTCustomECAStubInvoker.getCUSTOMECADBTCustomECAStub();

            customECAStub.dbtPostChequeInSuspendStatus(dbtPostChequeInSuspendStatus62);
        } catch (Exception ex) {
            if (ex.getMessage().contains("Customer does not exist"))
                throw new NEException("CUTOMER_NOT_FOUND");
            else
                throw new RemoteException(ex.getMessage(), ex);
        }finally{
            customECADBTCustomECAStubInvoker.returnCUSTOMECADBTCustomECAStub(customECAStub);
        }

        LOGGER.info("Cheque payment post success. Chq No::{}, Trx ID::{}", paymentDTO.getChequeNo(),
                paymentDTO.getTrxID());
        /* API call success */
        return -1;
    }

    public int doRealizeChqForcefully(PaymentDTO paymentDTO)
            throws RemoteException, ApplicationExceptionError, ParameterExceptionError {

        CUSTOMECADBTCustomECAStub.DbtRealizeCheque dbtRealizeCheque20 = new CUSTOMECADBTCustomECAStub.DbtRealizeCheque();
        CUSTOMECADBTCustomECAStub.DbtRealizeChequeInput input = new CUSTOMECADBTCustomECAStub.DbtRealizeChequeInput();
        input.setBankCode(paymentDTO.getChequebankCode());
        input.setBranchCode(paymentDTO.getChequebankBranchCode());
        input.setChequeNum(paymentDTO.getChequeNo());
        CUSTOMECADBTCustomECAStub.CustomerPK customerPK = new CUSTOMECADBTCustomECAStub.CustomerPK();
        customerPK.setCustomerRef(paymentDTO.getAccountNo());
        input.setCustomerPK(customerPK);
        dbtRealizeCheque20.setDbtRealizeCheque(input);

        customECADBTCustomECAStubInvoker = new CUSTOMECADBTCustomECAStubInvoker();
        customECADBTCustomECAStubInvoker.setUp(customEP + "/CUSTOMECADBTCustomECAPort", userName, pwd, timeOutInMilliSeconds,maxIdle, maxTotal, minIdle, waiting, testOnBorrow, testOnReturn);

        try {
            customECAStub = customECADBTCustomECAStubInvoker.getCUSTOMECADBTCustomECAStub();

            customECAStub.dbtRealizeCheque(dbtRealizeCheque20);
        } catch (Exception ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }finally{
            customECADBTCustomECAStubInvoker.returnCUSTOMECADBTCustomECAStub(customECAStub);
        }

        return -1;
    }

    public String queryUnrealizedCheques(PaymentDTO paymentDTO)
            throws AxisFault, RemoteException, ApplicationExceptionError, ParameterExceptionError, NEException {

        CUSTOMECADBTCustomECAStub.DbtQueryUnrealizedCheques dbtQueryUnrealizedCheques36 = new CUSTOMECADBTCustomECAStub.DbtQueryUnrealizedCheques();
        CUSTOMECADBTCustomECAStub.DbtQueryUnrealizedChequesInput dbtQueryUnrealizedChequesInput = new CUSTOMECADBTCustomECAStub.DbtQueryUnrealizedChequesInput();
        dbtQueryUnrealizedChequesInput.setChequeNum(paymentDTO.getChequeNo());
        dbtQueryUnrealizedChequesInput.setBankCode(paymentDTO.getChequebankCode());
        dbtQueryUnrealizedChequesInput.setBranchCode(paymentDTO.getChequebankBranchCode());

        CUSTOMECADBTCustomECAStub.AccountPK accountPK = new CUSTOMECADBTCustomECAStub.AccountPK();
        accountPK.setAccountNum(paymentDTO.getContractNo());

        dbtQueryUnrealizedChequesInput.setAccountPK(accountPK);

        dbtQueryUnrealizedCheques36.setDbtQueryUnrealizedCheques(dbtQueryUnrealizedChequesInput);
        CUSTOMECADBTCustomECAStub.DbtQueryUnrealizedChequesOutputE response = null;

        customECADBTCustomECAStubInvoker = new CUSTOMECADBTCustomECAStubInvoker();
        customECADBTCustomECAStubInvoker.setUp(customEP + "/CUSTOMECADBTCustomECAPort",userName, pwd, timeOutInMilliSeconds, maxIdle, maxTotal, minIdle, waiting, testOnBorrow, testOnReturn);

        try {
            customECAStub = customECADBTCustomECAStubInvoker.getCUSTOMECADBTCustomECAStub();

            response = customECAStub.dbtQueryUnrealizedCheques(dbtQueryUnrealizedCheques36);
        } catch (Exception ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }finally{
            customECADBTCustomECAStubInvoker.returnCUSTOMECADBTCustomECAStub(customECAStub);
        }

        CUSTOMECADBTCustomECAStub.DbtQueryUnrealizedChequesOutput queryUnrealizedChequesOutput = response.getDbtQueryUnrealizedChequesOutput();
        if (queryUnrealizedChequesOutput != null) {
            CUSTOMECADBTCustomECAStub.ArrayOfDbtChequeData arrayOfDbtChequeData = queryUnrealizedChequesOutput.getDbtChequeData();
            if (arrayOfDbtChequeData != null) {
                CUSTOMECADBTCustomECAStub.DbtChequeData[] dbtChequeDataArray = arrayOfDbtChequeData.getA();
                if (dbtChequeDataArray != null && dbtChequeDataArray.length > 0) {
                    return dbtChequeDataArray[0].getChequeStatus();
                } else {
                    throw new NEException("No Suspend cheque found yet.");
                }
            } else {
                throw new NEException("No Suspend cheque found yet.");
            }
        } else {
            throw new NEException("No Suspend cheque found yet.");
        }
    }


    public int cancelPayment(PaymentDTO paymentDTO) throws RemoteException, NullParameterExceptionError, lk.dialog.pe.scheduler.soap.payment.ApplicationExceptionError {
        ECAPaymentStub.CancelPhysicalPayment_1 cancelPhysicalPayment_1134 = new ECAPaymentStub.CancelPhysicalPayment_1();
        ECAPaymentStub.CancelPhysicalPaymentInput_1 cancelInput = new ECAPaymentStub.CancelPhysicalPaymentInput_1();
        ECAPaymentStub.PhysicalPaymentPK param = new ECAPaymentStub.PhysicalPaymentPK();
        param.setPhysicalPaymentSeq((int) paymentDTO.getPhysicalPaymentSeq());
        param.setCustomerRef(paymentDTO.getAccountNo());
        cancelInput.setPhysicalPaymentPK(param);
        cancelPhysicalPayment_1134.setCancelPhysicalPayment_1(cancelInput);
        getECAPaymentStub().cancelPhysicalPayment_1(cancelPhysicalPayment_1134);
        return 1;
    }

    private ECAPaymentStub getECAPaymentStub() throws AxisFault {
        if (ecaPaymentStub == null) {
            ecaPaymentStub = new ECAPaymentStub(ecaEP + "/ECAPaymentPort?wsdl");
            /* set basic authentication in header level */
            final HttpTransportPropertiesImpl.Authenticator basicAuth = new HttpTransportPropertiesImpl.Authenticator();
            basicAuth.setUsername(userName);
            basicAuth.setPassword(pwd);
            final Options options = ecaPaymentStub._getServiceClient().getOptions();
            options.setProperty(HTTPConstants.CHUNKED, Constants.VALUE_FALSE);
            options.setProperty(HTTPConstants.AUTHENTICATE, basicAuth);
            options.setTimeOutInMilliSeconds(readTimeoutPeriod);
            ecaPaymentStub._getServiceClient().setOptions(options);

        }
        return ecaPaymentStub;
    }

    public int modifyPaymentExtension(PaymentDTO paymentDTO) throws RemoteException, ApplicationExceptionError, ParameterExceptionError, NEException {

        CUSTOMECADBTCustomECAStub.DbtCreateModifyPaymentExtension dbtCreateModifyPaymentExtension6 = new CUSTOMECADBTCustomECAStub.DbtCreateModifyPaymentExtension();
        CUSTOMECADBTCustomECAStub.DbtCreateModifyPaymentExtensionInput input = new CUSTOMECADBTCustomECAStub.DbtCreateModifyPaymentExtensionInput();
        CUSTOMECADBTCustomECAStub.DbtModifyPaymentExtensionDataPair modifyPaymentExtPair = new CUSTOMECADBTCustomECAStub.DbtModifyPaymentExtensionDataPair();
        /* set account */
        CUSTOMECADBTCustomECAStub.AccountPaymentPK accountPK = new CUSTOMECADBTCustomECAStub.AccountPaymentPK();
        accountPK.setAccountNum(paymentDTO.getContractNo());
        accountPK.setAccountPaymentSeq((int) paymentDTO.getAccountPaymentSeq());
        modifyPaymentExtPair.setAccountPaymentPK(accountPK);
        /* set old payment data */
        CUSTOMECADBTCustomECAStub.DbtPaymentExtensionData oldPaymentExtensionData = queryPaymentExtension(paymentDTO);
        if (oldPaymentExtensionData != null) {
            // set old payment extension data
            modifyPaymentExtPair.setOldPaymentExtensionData(oldPaymentExtensionData);
            // set new payment extension data
            CUSTOMECADBTCustomECAStub.DbtPaymentExtensionData newPaymentExtensionData = new CUSTOMECADBTCustomECAStub.DbtPaymentExtensionData();
            newPaymentExtensionData.setReceiptNumber(paymentDTO.getReceiptNo());
            newPaymentExtensionData.setReceiptBranchName(paymentDTO.getReceiptBranch());
            newPaymentExtensionData.setBranchCounter(paymentDTO.getBranchCounter());
            newPaymentExtensionData.setReceiptUser(paymentDTO.getReceiptUser());
            newPaymentExtensionData.setReceiptDate(paymentDTO.getReceiptDate());
            newPaymentExtensionData.setPaymentMode(oldPaymentExtensionData.getPaymentMode());// payment mode not a input for cancellation
            newPaymentExtensionData.setPaymentRefNo(oldPaymentExtensionData.getPaymentRefNo()); // payment ref no not a input for cancellation
            newPaymentExtensionData.setRemarks(paymentDTO.getRemarks());
            newPaymentExtensionData.setStatusReason(paymentDTO.getStatusReason()); // cancel reason code
            newPaymentExtensionData.setPaymentCancelledReason(
                    paymentDTO.getStatusReason() + " " + paymentDTO.getPaymentCancelledReason()); // cancel description
            newPaymentExtensionData.setPaymentCancelledUser(paymentDTO.getCancelledByUser()); // cancel user

            String[] paymentExtAttributeArray = oldPaymentExtensionData.getPaymentExtAttribute();
            paymentExtAttributeArray[6] = paymentDTO.getTerminalID();
            paymentExtAttributeArray[7] = paymentDTO.getTransferredNo();
            paymentExtAttributeArray[14] = paymentDTO.getReceiptNo();
            if (NEConstants.PAY_MODE_CASH.equalsIgnoreCase(oldPaymentExtensionData.getPaymentMode())
                    || NEConstants.PAY_MODE_CARD.equalsIgnoreCase(oldPaymentExtensionData.getPaymentMode())) {
                paymentExtAttributeArray[10] = paymentDTO.getTransferredMode(); // Transferred Mode
                paymentExtAttributeArray[11] = Double.toString(paymentDTO.getTransferredAmount()); // Transfer Amount
            }
            if (paymentExtAttributeArray != null) {
                newPaymentExtensionData.setPaymentExtAttribute(paymentExtAttributeArray);
                newPaymentExtensionData.setPaymentReversalType(paymentDTO.getPaymentReversalType());
            }
            modifyPaymentExtPair.setNewPaymentExtensionData(newPaymentExtensionData);

        } else {
            throw new NEException("No PaymentExtensionData found in RBM. AccountRef:" + paymentDTO.getContractNo());
        }
        input.setModifyPaymentExtensionDataPair(modifyPaymentExtPair);
        dbtCreateModifyPaymentExtension6.setDbtCreateModifyPaymentExtension(input);

        customECADBTCustomECAStubInvoker = new CUSTOMECADBTCustomECAStubInvoker();
        customECADBTCustomECAStubInvoker.setUp(customEP + "/CUSTOMECADBTCustomECAPort", userName, pwd, timeOutInMilliSeconds,maxIdle, maxTotal, minIdle, waiting, testOnBorrow, testOnReturn);
        try {
            customECAStub = customECADBTCustomECAStubInvoker.getCUSTOMECADBTCustomECAStub();
            customECAStub.dbtCreateModifyPaymentExtension(dbtCreateModifyPaymentExtension6);
        } catch (Exception ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }finally{
            customECADBTCustomECAStubInvoker.returnCUSTOMECADBTCustomECAStub(customECAStub);
        }
        return 1;
    }

    public CUSTOMECADBTCustomECAStub.DbtPaymentExtensionData queryPaymentExtension(PaymentDTO paymentDTO) throws RemoteException, ApplicationExceptionError, ParameterExceptionError {

        CUSTOMECADBTCustomECAStub.DbtQueryPaymentExtension dbtQueryPaymentExtension72 = new CUSTOMECADBTCustomECAStub.DbtQueryPaymentExtension();
        CUSTOMECADBTCustomECAStub.DbtQueryPaymentExtensionInput input = new CUSTOMECADBTCustomECAStub.DbtQueryPaymentExtensionInput();
        CUSTOMECADBTCustomECAStub.AccountPaymentPK accountPK = new CUSTOMECADBTCustomECAStub.AccountPaymentPK();
        accountPK.setAccountNum(paymentDTO.getContractNo());
        accountPK.setAccountPaymentSeq((int) paymentDTO.getAccountPaymentSeq());
        input.setAccountPaymentPK(accountPK);
        dbtQueryPaymentExtension72.setDbtQueryPaymentExtension(input);
        CUSTOMECADBTCustomECAStub.DbtQueryPaymentExtensionOutputE dbtPayExtOutputE = null;
        customECADBTCustomECAStubInvoker = new CUSTOMECADBTCustomECAStubInvoker();
        customECADBTCustomECAStubInvoker.setUp(customEP + "/CUSTOMECADBTCustomECAPort", userName, pwd, timeOutInMilliSeconds,maxIdle, maxTotal, minIdle, waiting, testOnBorrow, testOnReturn);
        try {
            customECAStub = customECADBTCustomECAStubInvoker.getCUSTOMECADBTCustomECAStub();
            dbtPayExtOutputE = customECAStub.dbtQueryPaymentExtension(dbtQueryPaymentExtension72);
        } catch (Exception ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }finally{
            customECADBTCustomECAStubInvoker.returnCUSTOMECADBTCustomECAStub(customECAStub);
        }
        if (dbtPayExtOutputE != null) {
            CUSTOMECADBTCustomECAStub.DbtQueryPaymentExtensionOutput dbtPayExtOutput = dbtPayExtOutputE.getDbtQueryPaymentExtensionOutput();
            if (dbtPayExtOutput != null)
                return dbtPayExtOutput.getPaymentExtensionData();
            else
                return null;
        } else
            return null;
    }

    public PaymentDTO queryPaymentsSummery(String receiptNo, String receiptBranch) throws RemoteException, ApplicationExceptionError, ParameterExceptionError {
        PaymentDTO payment = null;
        CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetails dbtQueryAllPaymentDetails40 = new CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetails();
        CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetailsInput dbtQueryPaymentInput = new CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetailsInput();
        dbtQueryPaymentInput.setReceiptBranch(receiptBranch);
        dbtQueryPaymentInput.setReceiptNumber(receiptNo);
        dbtQueryAllPaymentDetails40.setDbtQueryAllPaymentDetails(dbtQueryPaymentInput);
        CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetailsOutputE queryPaymentsOutputE = null;
        this.customECADBTCustomECAStubInvoker = new CUSTOMECADBTCustomECAStubInvoker();
        this.customECADBTCustomECAStubInvoker.setUp(this.customEP + "/CUSTOMECADBTCustomECAPort", this.userName, this.pwd, this.timeOutInMilliSeconds, this.maxIdle, this.maxTotal, this.minIdle, this.waiting, this.testOnBorrow, this.testOnReturn);

        try {
            this.customECAStub = this.customECADBTCustomECAStubInvoker.getCUSTOMECADBTCustomECAStub();
            queryPaymentsOutputE = this.customECAStub.dbtQueryAllPaymentDetails(dbtQueryAllPaymentDetails40);
        } catch (Exception var14) {
            throw new RemoteException(var14.getMessage(), var14);
        } finally {
            this.customECADBTCustomECAStubInvoker.returnCUSTOMECADBTCustomECAStub(this.customECAStub);
        }

        CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetailsOutput var7 = queryPaymentsOutputE.getDbtQueryAllPaymentDetailsOutput();
        CUSTOMECADBTCustomECAStub.DbtAccountPaymentDataResult[] queryPaymentDataResult = var7.getDbtAccountPaymentDataArray();
        if (queryPaymentDataResult != null && queryPaymentDataResult.length > 0) {
            payment = new PaymentDTO();
            CUSTOMECADBTCustomECAStub.DbtAccountPaymentDataResult result = queryPaymentDataResult[0];
            CUSTOMECADBTCustomECAStub.AccountPaymentData_9 accountPaymentData = result.getAccountPaymentData();
            if (accountPaymentData != null) {
                payment.setContractNo(accountPaymentData.getAccountNum());
                payment.setAccountPaymentDate(accountPaymentData.getAccountPaymentDat());
                payment.setAccountPaymentMny((double)accountPaymentData.getAccountPaymentMny() / 1000.0D);
                payment.setAccountPaymentRef(accountPaymentData.getAccountPaymentRef());
                payment.setAccountPaymentSeq((long)accountPaymentData.getAccountPaymentSeq());
                payment.setAccountPaymentStatus(accountPaymentData.getAccountPaymentStatus().getPaymentStatus());
                payment.setAccountPaymentText(accountPaymentData.getAccountPaymentText());
                payment.setAccountType(2);
                payment.setCancelledDtm(accountPaymentData.getCancelledDtm());
                payment.setAccountNo(accountPaymentData.getCustomerPK().getCustomerRef());
                payment.setPhysicalPaymentSeq((long)accountPaymentData.getPhysicalPaymentPK().getPhysicalPaymentSeq());
                payment.setCreatedDtm(accountPaymentData.getCreatedDtm());
//                payment.setSbu(getSBU(accountPaymentData.getAccountNum()));
                payment.setProductType(getProductType(accountPaymentData.getAccountNum()));
            }

            CUSTOMECADBTCustomECAStub.DbtPaymentExtensionData dbtExtensionData = result.getDbtPaymentExtensionData();
            if (dbtExtensionData != null) {
                payment.setBranchCounter(dbtExtensionData.getBranchCounter());
                payment.setCancelledByUser(dbtExtensionData.getPaymentCancelledUser());
                payment.setPaymentCancelledReason(dbtExtensionData.getPaymentCancelledReason());
                payment.setReceiptBranch(dbtExtensionData.getReceiptBranchName());
                payment.setReceiptNo(dbtExtensionData.getReceiptNumber());
                payment.setReceiptUser(dbtExtensionData.getReceiptUser());
                payment.setReceiptDate(dbtExtensionData.getReceiptDate());
                payment.setPaymentMode(dbtExtensionData.getPaymentMode());
                payment.setPaymentRefNo(dbtExtensionData.getPaymentRefNo());
                payment.setRemarks(dbtExtensionData.getRemarks());
                payment.setStatusReason(dbtExtensionData.getStatusReason());
                payment.setPaymentReversalType(dbtExtensionData.getPaymentReversalType());
                if (dbtExtensionData.getPaymentMode().equals("CARD")) {
                    payment.setCcApprovalCode(dbtExtensionData.getPaymentExtAttribute()[3]);
                    payment.setCcNo(dbtExtensionData.getPaymentExtAttribute()[2]);
                    payment.setCcType(dbtExtensionData.getPaymentExtAttribute()[0]);
                    payment.setCcBankCode(dbtExtensionData.getPaymentExtAttribute()[1]);
                    if (dbtExtensionData.getPaymentExtAttribute()[11] != null) {
                        payment.setTransferredAmount(Double.parseDouble(dbtExtensionData.getPaymentExtAttribute()[11]));
                    }

                    payment.setTransferredMode(dbtExtensionData.getPaymentExtAttribute()[10]);
                    payment.setTransferredNo(dbtExtensionData.getPaymentExtAttribute()[7]);
                } else if (dbtExtensionData.getPaymentMode().equals("CHEQ")) {
                    payment.setChequebankBranchCode(dbtExtensionData.getPaymentExtAttribute()[1]);
                    payment.setChequebankCode(dbtExtensionData.getPaymentExtAttribute()[0]);
                    payment.setChequeNo(dbtExtensionData.getPaymentExtAttribute()[2]);
                } else if (dbtExtensionData.getPaymentMode().equals("CASH")) {
                    if (dbtExtensionData.getPaymentExtAttribute()[11] != null) {
                        payment.setTransferredAmount(Double.parseDouble(dbtExtensionData.getPaymentExtAttribute()[11]));
                    }

                    payment.setTransferredMode(dbtExtensionData.getPaymentExtAttribute()[10]);
                    payment.setTransferredNo(dbtExtensionData.getPaymentExtAttribute()[7]);
                }
            }
        }

        return payment;
    }

    public List<PaymentDTO> queryPaymentsSummery(QueryPaymentsSummaryRequest summaryRequest) throws RemoteException {
        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        List<PaymentDTO> paymentList = new ArrayList();
        CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetails dbtQueryAllPaymentDetails40 = new CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetails();
        CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetailsInput dbtQueryPaymentInput = new CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetailsInput();
        if (summaryRequest.getContractNo() != null && !"".equals(summaryRequest.getContractNo())) {
            final CUSTOMECADBTCustomECAStub.AccountPK accountPK = new CUSTOMECADBTCustomECAStub.AccountPK();
            accountPK.setAccountNum(summaryRequest.getContractNo());
            dbtQueryPaymentInput.setAccountPK(accountPK);
        }

        dbtQueryPaymentInput.setBankBranchCode(summaryRequest.getChequeBankBranchCode());
        dbtQueryPaymentInput.setBankCode(summaryRequest.getChequeBankCode());
        dbtQueryPaymentInput.setBranchCounter(summaryRequest.getBranchCounter());
        dbtQueryPaymentInput.setChequeNum(summaryRequest.getChequeNo());
        dbtQueryPaymentInput.setReceiptBranch(summaryRequest.getReceiptBranch());
        if (summaryRequest.getReceiptFromDate() != null) {
            fromDate.setTime(summaryRequest.getReceiptFromDate());
            dbtQueryPaymentInput.setReceiptFromDate(fromDate);
        }

        if (summaryRequest.getReceiptToDate() != null) {
            toDate.setTime(summaryRequest.getReceiptToDate());
            dbtQueryPaymentInput.setReceiptToDate(toDate);
        }

        dbtQueryPaymentInput.setReceiptNumber(summaryRequest.getReceiptNo());
        dbtQueryPaymentInput.setReceiptUser(summaryRequest.getUser());
        dbtQueryAllPaymentDetails40.setDbtQueryAllPaymentDetails(dbtQueryPaymentInput);
        CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetailsOutputE queryPaymentsOutputE = null;
        this.customECADBTCustomECAStubInvoker = new CUSTOMECADBTCustomECAStubInvoker();
        this.customECADBTCustomECAStubInvoker.setUp(this.customEP + "/CUSTOMECADBTCustomECAPort", this.userName, this.pwd, this.timeOutInMilliSeconds, this.maxIdle, this.maxTotal, this.minIdle, this.waiting, this.testOnBorrow, this.testOnReturn);

        try {
            this.customECAStub = this.customECADBTCustomECAStubInvoker.getCUSTOMECADBTCustomECAStub();
            queryPaymentsOutputE = this.customECAStub.dbtQueryAllPaymentDetails(dbtQueryAllPaymentDetails40);
        } catch (Exception var28) {
            throw new RemoteException(var28.getMessage(), var28);
        } finally {
            this.customECADBTCustomECAStubInvoker.returnCUSTOMECADBTCustomECAStub(this.customECAStub);
        }

        CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetailsOutput var17 = queryPaymentsOutputE.getDbtQueryAllPaymentDetailsOutput();
        CUSTOMECADBTCustomECAStub.DbtAccountPaymentDataResult[] queryPaymentDataResult = var17.getDbtAccountPaymentDataArray();
        if (queryPaymentDataResult != null) {
            CUSTOMECADBTCustomECAStub.DbtAccountPaymentDataResult[] var19 = queryPaymentDataResult;
            int var20 = queryPaymentDataResult.length;

            for(int var21 = 0; var21 < var20; ++var21) {
                CUSTOMECADBTCustomECAStub.DbtAccountPaymentDataResult result = var19[var21];
                PaymentDTO payment = new PaymentDTO();
                CUSTOMECADBTCustomECAStub.AccountPaymentData_9 accountPaymentData = result.getAccountPaymentData();
                if (accountPaymentData != null) {
                    payment.setContractNo(accountPaymentData.getAccountNum());
                    payment.setAccountPaymentDate(accountPaymentData.getAccountPaymentDat());
                    payment.setAccountPaymentMny((double)accountPaymentData.getAccountPaymentMny() / 1000.0D);
                    payment.setAccountPaymentRef(accountPaymentData.getAccountPaymentRef());
                    payment.setAccountPaymentSeq((long)accountPaymentData.getAccountPaymentSeq());
                    payment.setAccountPaymentStatus(accountPaymentData.getAccountPaymentStatus().getPaymentStatus());
                    payment.setAccountPaymentText(accountPaymentData.getAccountPaymentText());
                    payment.setAccountType(2);
                    payment.setCancelledDtm(accountPaymentData.getCancelledDtm());
                    payment.setAccountNo(accountPaymentData.getCustomerPK().getCustomerRef());
                    payment.setPhysicalPaymentSeq((long)accountPaymentData.getPhysicalPaymentPK().getPhysicalPaymentSeq());
                    payment.setCreatedDtm(accountPaymentData.getCreatedDtm());
                    payment.setSbu(-1);
                    payment.setProductType(-1);
                }

                CUSTOMECADBTCustomECAStub.DbtPaymentExtensionData dbtExtensionData = result.getDbtPaymentExtensionData();
                if (dbtExtensionData != null) {
                    payment.setBranchCounter(dbtExtensionData.getBranchCounter());
                    payment.setCancelledByUser(dbtExtensionData.getPaymentCancelledUser());
                    payment.setPaymentCancelledReason(dbtExtensionData.getPaymentCancelledReason());
                    payment.setReceiptBranch(dbtExtensionData.getReceiptBranchName());
                    payment.setReceiptNo(dbtExtensionData.getReceiptNumber());
                    payment.setReceiptUser(dbtExtensionData.getReceiptUser());
                    payment.setReceiptDate(dbtExtensionData.getReceiptDate());
                    payment.setPaymentMode(dbtExtensionData.getPaymentMode());
                    payment.setPaymentRefNo(dbtExtensionData.getPaymentRefNo());
                    payment.setRemarks(dbtExtensionData.getRemarks());
                    payment.setStatusReason(dbtExtensionData.getStatusReason());
                    payment.setPaymentReversalType(dbtExtensionData.getPaymentReversalType());
                    if (dbtExtensionData.getPaymentMode().equals("CARD")) {
                        payment.setCcApprovalCode(dbtExtensionData.getPaymentExtAttribute()[3]);
                        payment.setCcNo(dbtExtensionData.getPaymentExtAttribute()[2]);
                        payment.setCcType(dbtExtensionData.getPaymentExtAttribute()[0]);
                        payment.setCcBankCode(dbtExtensionData.getPaymentExtAttribute()[1]);
                        if (dbtExtensionData.getPaymentExtAttribute()[11] != null) {
                            payment.setTransferredAmount(Double.parseDouble(dbtExtensionData.getPaymentExtAttribute()[11]));
                        }

                        payment.setTransferredMode(dbtExtensionData.getPaymentExtAttribute()[10]);
                        payment.setTransferredTo(dbtExtensionData.getPaymentExtAttribute()[7]);
                    } else if (dbtExtensionData.getPaymentMode().equals("CHEQ")) {
                        payment.setChequebankBranchCode(dbtExtensionData.getPaymentExtAttribute()[1]);
                        payment.setChequebankCode(dbtExtensionData.getPaymentExtAttribute()[0]);
                        payment.setChequeNo(dbtExtensionData.getPaymentExtAttribute()[2]);
                        payment.setTransferredTo(dbtExtensionData.getPaymentExtAttribute()[8]);
                        payment.setTransferredTo(dbtExtensionData.getPaymentExtAttribute()[7]);
                    } else if (dbtExtensionData.getPaymentMode().equals("CASH")) {
                        if (dbtExtensionData.getPaymentExtAttribute()[11] != null) {
                            payment.setTransferredAmount(Double.parseDouble(dbtExtensionData.getPaymentExtAttribute()[11]));
                        }

                        payment.setTransferredMode(dbtExtensionData.getPaymentExtAttribute()[10]);
                        payment.setTransferredTo(dbtExtensionData.getPaymentExtAttribute()[7]);
                    }
                }

                paymentList.add(payment);
            }
        }

        return paymentList;
    }


//    @Override
//    public RBMCustomerDTO readAllCustomerAttributes(String customerRef)
//            throws RemoteException, lk.dialog.crm.ne.rbm.customer.NullParameterExceptionError, NoSuchEntityExceptionError,
//            lk.dialog.crm.ne.rbm.customer.ApplicationExceptionError {
//        final RBMCustomerDTO rbmCxDTO = new RBMCustomerDTO();
//        ECACustomerStub.ReadAllCustomerAttributes_5_1 readAllCustomerAttributes;
//        readAllCustomerAttributes = new ECACustomerStub.ReadAllCustomerAttributes_5_1();
//        ECACustomerStub.ReadAllCustomerAttributesInput_5_1 param;
//        param = new ECACustomerStub.ReadAllCustomerAttributesInput_5_1();
//        lk.dialog.crm.ne.rbm.customer.ECACustomerStub.CustomerPK customerPK;
//        customerPK = new lk.dialog.crm.ne.rbm.customer.ECACustomerStub.CustomerPK();
//        customerPK.setCustomerRef(customerRef);
//        param.setCustomerPK(customerPK);
//        readAllCustomerAttributes.setReadAllCustomerAttributes_5_1(param);
//        ECACustomerStub.ReadAllCustomerAttributes_5_1Output readAllCustomerAttributesOutput = null;
//        try {
//            readAllCustomerAttributesOutput = getECACustomerStub()
//                    .readAllCustomerAttributes_5_1(readAllCustomerAttributes);
//        } catch (Exception ex) {
//            throw ex;
//        }
//
//        final ECACustomerStub.ReadAllCustomerAttributesOutput_5_1 readAllCustomerAttributesOutput51 = readAllCustomerAttributesOutput
//                .getReadAllCustomerAttributes_5_1Output();
//        final lk.dialog.crm.ne.rbm.customer.ECACustomerStub.AttributeField[] attributeArr = readAllCustomerAttributesOutput51
//                .getResult();
//        for (final lk.dialog.crm.ne.rbm.customer.ECACustomerStub.AttributeField field : attributeArr) {
//            if ("IDENTIFICATION_NUMBER".equalsIgnoreCase(field.getFieldName())) {
//                rbmCxDTO.setId(field.getFieldValueString());
//            } else if ("PR_CUSTOMER_REF".equalsIgnoreCase(field.getFieldName())) {
//                rbmCxDTO.setPrCustRef(field.getFieldValueString());
//            } else if ("BILL_RUN_CODE".equalsIgnoreCase(field.getFieldName())) {
//                rbmCxDTO.setBillRunCode(field.getFieldValueString());
//            }
//        }
//
//        return rbmCxDTO;
//    }
//
//    private int getSBU(String contractID) {
//        if (contractID.startsWith(NEConstants.REGEX_DTV)) {
//            return NEConstants.SBU_DTV_POST;
//        } else if ((contractID.startsWith(NEConstants.REGEX_DBN1) && contractID.length() == 8)
//                || (contractID.startsWith(NEConstants.REGEX_DBN2) && contractID.length() == 8)
//                || !contractID.matches("[0-9]+")) {
//            return NEConstants.SBU_DBN;
//        } else {
//            return NEConstants.SBU_GSM;
//        }
//    }

    private int getProductType(String contractID) {
        if (contractID.startsWith(NEConstants.REGEX_NFC)) {
            return NEConstants.PT_NFC;
        } else if (contractID.startsWith(NEConstants.REGEX_WIFI)) {
            return NEConstants.PT_WIFI;
        } else {
            return NEConstants.PT_OTHER;
        }
    }

//    public static int getSBU(String contractID) {
//        if (contractID.startsWith(lk.dialog.crm.ne.util.NEConstants.REGEX_DTV)) {
//            return lk.dialog.crm.ne.util.NEConstants.SBU_DTV_POST;
//        } else if ((contractID.startsWith(lk.dialog.crm.ne.util.NEConstants.REGEX_DBN1) && contractID.length() == 8)
//                || (contractID.startsWith(lk.dialog.crm.ne.util.NEConstants.REGEX_DBN2) && contractID.length() == 8)
//                || !contractID.matches("[0-9]+")) {
//            return lk.dialog.crm.ne.util.NEConstants.SBU_DBN;
//        } else {
//            return lk.dialog.crm.ne.util.NEConstants.SBU_GSM;
//        }
//    }
}
