package lk.dialog.pe.persistence.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.SQLQueryEnum;
import lk.dialog.pe.persistence.domain.CancelPayment;
import lk.dialog.pe.persistence.service.CancelPaymentService;
import lk.dialog.pe.persistence.writer.repository.QueryExcecuterWriterRepository;

@Service
public class CancelPaymentServiceImpl implements CancelPaymentService {
	
	
	@Autowired
	private QueryExcecuterWriterRepository queryExcecuterWriterRepository;

	@Autowired
	@Qualifier("paymentMap")
	private Map<String, String>  map;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CancelPaymentServiceImpl.class);

	@Override
	public Object saveCancelPayment(CancelPayment cancelPayment, String traceId) throws DCPEException{
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String requestString = DCPEUtil.convertToString(cancelPayment);
		LOGGER.info("saveCancelPaymentRequest : traceId={}|CancelPayment={}", traceId, requestString);
		Instant start = Instant.now();
		
		List<Object> param = new ArrayList<>();
		param.add(cancelPayment.getReqId());
		param.add(cancelPayment.getProductCategory());
		param.add(cancelPayment.getSbu());
		param.add(cancelPayment.getQuerySystem());
		param.add(cancelPayment.getAccountNo());
		param.add(cancelPayment.getContractNo());
		param.add(cancelPayment.getPhysicalSeq());
		param.add(cancelPayment.getAccountSeq());
		param.add(cancelPayment.getChqReturn());
		param.add(cancelPayment.getChqSuspend());
		param.add(cancelPayment.getReversalType());
		param.add(cancelPayment.getTransferredType());
		param.add(cancelPayment.getTransferredMode());
		param.add(cancelPayment.getTransferredNo());
		param.add(cancelPayment.getTransferredRef());
		param.add(cancelPayment.getMistakeBy());		
		param.add(cancelPayment.getRemarks());
		param.add(cancelPayment.getCancelledReason());
		param.add(cancelPayment.getCancelledUser());		
		param.add(cancelPayment.getApprovedUser());
		param.add(cancelPayment.getProductType());		
		param.add(cancelPayment.getReceiptNo());	
		param.add(cancelPayment.getReceiptBranch());		
		param.add(cancelPayment.getBranchCounter());
		param.add(cancelPayment.getReceiptUser());
		
		if(cancelPayment.getReceiptDate() != null) {
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		param.add(Timestamp.valueOf(df.format(cancelPayment.getReceiptDate().getTime())));
		}else {
			param.add(null);
		}
		
	
		param.add(cancelPayment.getChqBank());
		param.add(cancelPayment.getChqNo());
		param.add(cancelPayment.getChqBranch());
		param.add(cancelPayment.getTerminalId());
		param.add(cancelPayment.getSourceSystem());
		
		if(cancelPayment.getPhysicalPaymentDate() != null) {
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		param.add(Timestamp.valueOf(df.format(cancelPayment.getPhysicalPaymentDate().getTime())));
		}else {
			param.add(null);
		}
		
		if(cancelPayment.getPaymentAmount() == null) {
			param.add(java.sql.Types.DOUBLE);
		}else {
		param.add(cancelPayment.getPaymentAmount());
		}
		int result = 0;
		
		try {
			result =(int) queryExcecuterWriterRepository.insertData(map.get(SQLQueryEnum.SQL_PAYMENT_CANCEL.getValue()), param.toArray(), traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		LOGGER.info("saveCancelPaymentResonse : traceId={}|timeTaken={}|body={}",traceId, timeTaken, result);

		return result;

	}
}