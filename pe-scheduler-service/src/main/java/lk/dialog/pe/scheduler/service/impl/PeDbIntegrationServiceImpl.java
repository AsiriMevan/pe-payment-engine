package lk.dialog.pe.scheduler.service.impl;

import lk.dialog.pe.scheduler.config.prop.QueryConfig;
import lk.dialog.pe.scheduler.domain.PayMode;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.repository.mapper.*;
import lk.dialog.pe.scheduler.service.PeDbIntegrationService;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.QUERY;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PeDbIntegrationServiceImpl implements PeDbIntegrationService {

    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate peJdbcTemplate;

    @Override
    @Cacheable("payModeCache")
    public synchronized PayMode getPayMode(String cposId) throws SQLException {
        log.info("getPayModeRequest[Cacheable] Loading PayMode from Mapping. CPOS paymode :" + cposId);
        try {
            PayMode payMode = peJdbcTemplate.queryForObject(QueryConfig.getCacheQueryByKey("SQL_MAP_PAYMODE"), new Object[] { cposId }, new BeanPropertyRowMapper<PayMode>(PayMode.class));
            String response = SchUtil.convertToString(payMode);
            log.info("getPayModeResponse[[Cacheable]] inputPaymode={}|mappedPaymode={}",cposId,response);
            return payMode;
        } catch (EmptyResultDataAccessException dataEx) {
            log.error("No mapping found for given paymentMode={}|error={}",cposId,dataEx.getMessage(),dataEx);
            throw new SQLException("No mapping found for given payment mode. Error:{}" + dataEx.getMessage());

        }
    }

    @Override
    public List<Payment> loadPaymentsForProcessing(HANDLER handler) {
        QUERY loadQuery = null;
        QUERY updateQuery =null;
        RowMapper<Payment> rowMapper=null;
        switch(handler){
            case RBM_PAY:
                loadQuery = QUERY.SQL_SEL_RBM_PAYMENT;
                updateQuery = QUERY.SQL_UPDATE_RBM_PAYMENT;
                rowMapper = new RBMPaymentMapper();
                break;
            case OCS_PAY:
                loadQuery = QUERY.SQL_SEL_OCS_PAYMENT;
                updateQuery = QUERY.SQL_UPDATE_OCS_PAYMENT;
                rowMapper = new OCSPaymentMapper();
                break;
            case DBN_PAY:
                loadQuery = QUERY.SQL_SEL_DBN_PAYMENT;
                updateQuery = QUERY.SQL_UPDATE_DBN_PAYMENT;
                rowMapper = new DBNPaymentMapper();
                break;
            case CHEQUE_FORCEFUL_PAY:
                loadQuery = QUERY.SQL_SEL_FORCEFUL_PEND_SUS_CHQ;
                updateQuery = QUERY.SQL_UPDATE_FORCEFUL_CHQ_READ;
                rowMapper = new ChequePaymentMapper(false);
                break;
            case CANCEL_PAY:
                loadQuery = QUERY.SQL_SEL_CANCEL_PAYMENT;
                updateQuery = QUERY.SQL_UPDATE_CANCEL_PAYMENT_READ;
                rowMapper = new CancelPaymentMapper();
                break;
            case RBM_PAY_RETRY:
                loadQuery = QUERY.SQL_SEL_RBM_FAIL_PAYMENT;
                updateQuery = QUERY.SQL_UPDATE_RBM_PAYMENT;
                rowMapper = new RBMPaymentMapper();
                break;
            case DBN_PAY_RETRY:
                loadQuery = QUERY.SQL_SEL_DBN_FAIL_PAYMENT;
                updateQuery = QUERY.SQL_UPDATE_RBM_PAYMENT;
                rowMapper = new DBNPaymentMapper();
                break;
            case CHEQUE_FORCEFUL_PAY_RETRY:
                loadQuery = QUERY.SQL_SEL_FAIL_FORCEFUL_PEND_SUS_CHQ;
                updateQuery = QUERY.SQL_UPDATE_FORCEFUL_CHQ_READ;
                rowMapper = new ChequePaymentMapper(false);
                break;
            default:
                log.error("loadPaymentsForProcessing invalid handler type(={}) provided",handler.name());
                return new ArrayList<>();
        }
        String loadQueryString = QueryConfig.getQuery(loadQuery);
        String updateQueryString =QueryConfig.getQuery(updateQuery);

        log.info("loadPaymentsForProcessingRequest handler={}|loadQueryKey={}|updateQueryKey={}",handler,loadQuery.name(),updateQuery.name());
        List<Payment> payments = peJdbcTemplate.query(loadQueryString, rowMapper);
        log.info("loadPaymentsForProcessingResponse loadedPayments handler={}|loadedSize={}",handler.name(),payments.size());

        List<Payment> updatedPayments = payments.stream().filter(payment->{
            try {
                if (peJdbcTemplate.update(updateQueryString, payment.getTrxID()) > 0) {
                    return true;
                }
            }catch(Exception ex){
                log.error("loadPaymentsForProcessing updateFailed handler={}|error={}",payment.getTrxID(),handler,ex.getMessage(),ex);
            }
            return false;
        }).collect(Collectors.toList());
        log.info("loadPaymentsForProcessingResponse handler={}|loadedBatchSize={}|updatedBatchSize={}",handler,payments.size(),updatedPayments.size());
        return updatedPayments;
    }

    @Override
    public int updatePaymentPostSuccess(HANDLER handler, COMMAND_READ commandRead,Long trxId) {
        QUERY query =getUpdatePaymentQueryByHandler(handler,commandRead);
        log.info("updatePaymentPostSuccessRequest handler={}|command_read={}|error_code={}|sqlKey={}",handler,commandRead,commandRead.getErrorCode(),query);
        int updateResult =  executePaymentPostUpdate(trxId,query,null,commandRead.getErrorCode(),"SUCCESS",commandRead.toString());
        log.info("updatePaymentPostSuccessResponse handler={}|updateResult={}",handler,updateResult);
        return updateResult;
    }
    @Override
    public int updatePaymentPostSuccessWithPaySequence(HANDLER handler, COMMAND_READ commandRead, Long trxId, Integer paymentSequence) {
        QUERY query =getUpdatePaymentQueryByHandler(handler,commandRead);
        log.info("updatePaymentPostSuccessWithPaySequenceRequest handler={}|paymentSequence={}|command_read={}|error_code={}|sqlKey={}",handler,paymentSequence,commandRead,commandRead.getErrorCode(),query);
        String successString = handler != HANDLER.RBM_PAY_RETRY ? "SUCCESS":"RETRY-SUCCESS";
        int updateResult =  executePaymentPostUpdate(trxId,query,paymentSequence,commandRead.getErrorCode(),successString,commandRead.toString());
        log.info("updatePaymentPostSuccessWithPaySequenceResponse handler={}|paymentSequence={}|updateResult={}",handler,paymentSequence,updateResult);
        return updateResult;
    }
    @Override
    public int updatePaymentPostSuccessWithPaySequenceAndErrDesc(HANDLER handler, COMMAND_READ commandRead, Long trxId, Integer paymentSequence,String errorDescription) {
        QUERY query =getUpdatePaymentQueryByHandler(handler,commandRead);
        log.info("updatePaymentPostSuccessWithPaySequenceRequest handler={}|paymentSequence={}|command_read={}|error_code={}|errorDescription={}|sqlKey={}",handler,paymentSequence,commandRead,commandRead.getErrorCode(),errorDescription,query);
        int updateResult =  executePaymentPostUpdate(trxId,query,paymentSequence,commandRead.getErrorCode(),errorDescription,commandRead.toString());
        log.info("updatePaymentPostSuccessWithPaySequenceResponse handler={}|paymentSequence={}|updateResult={}",handler,paymentSequence,updateResult);
        return updateResult;
    }
    @Override
    public int updatePaymentPostFailure(HANDLER handler, COMMAND_READ commandRead, Long trxId, String errorDescription) {
        QUERY query =getUpdatePaymentQueryByHandler(handler,commandRead);
        log.info("updatePaymentPostFailureRequest handler={}|command_read={}|sqlKey={}|error_code={}|errorDesc={}",handler,commandRead,query,commandRead.getErrorCode(),errorDescription);
        int updateResult =  executePaymentPostUpdate(trxId,query,null,commandRead.getErrorCode(),errorDescription,commandRead.toString());
        log.info("updatePaymentPostFailureResponse handler={}|updateResult={}",handler,updateResult);
        return updateResult;
    }
    @Override
    public int updatePaymentPostRetryFailure(HANDLER handler, Long trxId) {
        QUERY query=null;
        switch(handler){
            case RBM_PAY_RETRY:
                query = QUERY.SQL_UPDATE_RBM_FAIL_PAYMENT;
                break;
            case CHEQUE_FORCEFUL_PAY_RETRY:
                query = QUERY.SQL_UPDATE_FAIL_FORCEFUL_CHQ;
                break;
            default:
                query=null;
        }
        if(query==null) return -1;
        log.info("updatePaymentPostRetryFailureRequest handler={}|sqlKey={}|trxId={}",handler,query.name(),trxId);
        int updateResult = peJdbcTemplate.update(QueryConfig.getQuery(query),trxId);
        log.info("updatePaymentPostRetryFailureResponse handler={}|updateResult={}",handler,updateResult);
        return updateResult;
    }
    @Override
    public int updatePaymentPostFailureWithPaySequence(HANDLER handler, COMMAND_READ commandRead, Long trxId, Integer paymentSequence, String errorDescription) {
        QUERY query =getUpdatePaymentQueryByHandler(handler,commandRead);
        log.info("updatePaymentPostFailureRequest handler={}|paymentSequence={}|command_read={}|sqlKey={}|error_code={}|errorDesc={}",handler,paymentSequence,commandRead,query,commandRead.getErrorCode(),errorDescription);
        int updateResult =  executePaymentPostUpdate(trxId,query,paymentSequence,commandRead.getErrorCode(),errorDescription,commandRead.toString());
        log.info("updatePaymentPostFailureResponse handler={}|paymentSequence={}|updateResult={}",handler,paymentSequence,updateResult);
        return updateResult;
    }

    @Override
    public int updatePaymentPostCustom(HANDLER handler, QUERY query, COMMAND_READ commandRead, Long trxId, Integer paymentSequence, String errorDescription) {
        log.info("updatePaymentPostCustomRequest handler={}|paymentSequence={}|command_read={}|sqlKey={}|error_code={}|errorDesc={}",handler,paymentSequence,commandRead,query,commandRead.getErrorCode(),errorDescription);
        int updateResult =  executePaymentPostUpdate(trxId,query,paymentSequence,commandRead.getErrorCode(),errorDescription,commandRead.toString());
        log.info("updatePaymentPostCustomResponse handler={}|paymentSequence={}|updateResult={}",handler,paymentSequence,updateResult);
        return updateResult;
    }

    private QUERY getUpdatePaymentQueryByHandler(HANDLER handler,COMMAND_READ commandRead){

        switch(handler){
            case RBM_PAY:
            case RBM_PAY_RETRY:
                if(COMMAND_READ.S.equals(commandRead))return QUERY.SQL_UPDATE_RBM_PAYMENT_RES;
                else if(COMMAND_READ.N.equals(commandRead)) return QUERY.SQL_UPDATE_RBM_NO_CX_FOUND;
                else return QUERY.SQL_UPDATE_RBM_PAYMENT_FAIL_RES;
            case OCS_PAY:
                return QUERY.SQL_UPDATE_OCS_PAYMENT_RES;
            case DBN_PAY:
            case DBN_PAY_RETRY:
                if(COMMAND_READ.S.equals(commandRead))return QUERY.SQL_UPDATE_DBN_PAYMENT_RES;
                else return QUERY.SQL_UPDATE_DBN_PAYMENT_FAIL_RES;
            case CHEQUE_FORCEFUL_PAY:
            case CHEQUE_FORCEFUL_PAY_RETRY:
                return getCheckRealizeQuery(commandRead);
            case CANCEL_PAY:
                if(COMMAND_READ.S.equals(commandRead))return QUERY.SQL_UPDATE_CANCEL_PAYMENT_RES;
                else return QUERY.SQL_UPDATE_CANCEL_FAIL_PAYMENT_RES;
            default:
                return null;
        }
    }

    private QUERY getCheckRealizeQuery(COMMAND_READ commandRead){
        if(COMMAND_READ.S.equals(commandRead) || COMMAND_READ.P.equals(commandRead))return QUERY.SQL_UPDATE_FORCEFUL_CHQ;
        else if(COMMAND_READ.N.equals(commandRead)) return QUERY.SQL_UPDATE_FORCEFUL_CHQ_NO_CX_FOUND;
        else return QUERY.SQL_UPDATE_FORCEFUL_CHQ_FAIL_RES;
    }

    private int executePaymentPostUpdate(long trxID, QUERY query, Integer paySeq, String errCode, String errDesc, String status) {
        String newErrDesc = (errDesc!=null && errDesc.length() > 255) ? errDesc.substring(0, 255) : errDesc;
        if (paySeq != null)
            return peJdbcTemplate.update(QueryConfig.getQuery(query), status, paySeq, errCode, newErrDesc, trxID );
        else
            return peJdbcTemplate.update(QueryConfig.getQuery(query), status, errCode, newErrDesc, trxID );
    }

    @Override
    public List<Payment> getPartialChqRealizePayments(){
        log.info("getPartialCheckRealizePaymentsRequest");
        List<Payment> payments = peJdbcTemplate.query(QueryConfig.getQuery(QUERY.SQL_SEL_FORCEFUL_CHQ_PARTIAL), new ChequePaymentMapper(true));
        int size = payments!=null?payments.size():0;
        log.info("getPartialCheckRealizePaymentsResponse loadedPayments={}",size);
        return payments;
    }
    @Override
    public List<Payment> getFullCheckRealizePayments(String accountNo, String chequeNo){
        log.info("getFullCheckRealizePaymentsRequest accountNo={}|chequeNo={}",accountNo,chequeNo);
        List<Payment> payments = peJdbcTemplate.query(QueryConfig.getQuery(QUERY.SQL_SEL_FORCEFUL_CHQ_FULL), new ChequePaymentMapper(false), new Object[] { accountNo, chequeNo });
        int size = payments!=null?payments.size():0;
        log.info("getFullCheckRealizePaymentsResponse loadedPayments={}",size);
        return payments;
    }
    @Override
    public boolean isChequeReturnExist(String chqNo, String chqBank, String chqBranch) {
        log.info("isChequeReturnExistRequest chqNo={}|chqBank={}|chqBranch={}",chqNo,chqBank,chqBranch);
        try {
            int count = peJdbcTemplate.queryForObject(QueryConfig.getQueryByKey("SQL_SEL_CHQ_RETURN_LOG"), new Object[]{chqNo, chqBank, chqBranch}, Integer.class);
            log.info("isChequeReturnExistResponse success resultCount={}|chqNo={}|chqBank={}|chqBranch={}",count,chqNo,chqBank,chqBranch);
            return count > 0;
        } catch (EmptyResultDataAccessException dataEx) {
            log.error("isChequeReturnExistResponse No cheque return record found in the log. chqNo={}|chqBank={}|chqBranch={}|error={}",chqNo,chqBank,chqBranch, dataEx.getMessage(),dataEx);
            return false;
        }
    }
    @Override
    public boolean logChequeReturn(String contractNo, String chqBank, String chqBranch, String chqNo, String receiptNo) {
        log.info("logChequeReturnRequest contractNp={}|chqNo={}|chqBank={}|chqBranch={}|receiptNo={}",contractNo,chqNo,chqBank,chqBranch,receiptNo);
        try {
            String sqlChqReturnInsert = QueryConfig.getQueryByKey("SQL_INSERT_CHQ_RETURN");
            peJdbcTemplate.update(sqlChqReturnInsert, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {

                    ps.setString(1, contractNo);
                    ps.setString(2, chqBank);
                    ps.setString(3, chqBranch);
                    ps.setString(4, chqNo);
                    ps.setString(5, receiptNo);
                }
            });
            log.info("logChequeReturnResponse success contractNo={}|chqNo={}|chqBank={}|chqBranch={}|receiptNo={}",contractNo,chqNo,chqBank,chqBranch,receiptNo);
            return true;
        } catch (EmptyResultDataAccessException dataEx) {
            log.error("logChequeReturn cheque return record insert failed. contractNo={}|chqNo={}|chqBank={}|chqBranch={}|receiptNo={}|error={}",contractNo,chqNo,chqBank,chqBranch,receiptNo, dataEx.getMessage(),dataEx);
            return false;
        }
    }
    @Override
    public void deleteOcsPayment(Long trxId){
        log.info("deleteOcsPaymentRequest trxId={}",trxId);
        Integer status = peJdbcTemplate.update(QueryConfig.getQuery(QUERY.SQL_DEL_OCS_PAYMENT), new Object[] { trxId });
        log.info("deleteOcsPaymentResponse trxId={}|deleteStatus={}",trxId,status);
    }
}
