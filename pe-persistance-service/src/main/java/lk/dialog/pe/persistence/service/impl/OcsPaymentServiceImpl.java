package lk.dialog.pe.persistence.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import lk.dialog.pe.common.util.Util;
import lk.dialog.pe.persistence.domain.OcsPayment;
import lk.dialog.pe.persistence.service.OcsPaymentService;
import lk.dialog.pe.persistence.writer.repository.QueryExcecuterWriterRepository;

@Service
public class OcsPaymentServiceImpl implements OcsPaymentService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OcsPaymentServiceImpl.class);

	@Autowired
	private QueryExcecuterWriterRepository queryExcecuterWriterRepository;
	
	@Autowired
	@Qualifier("paymentMap")
	private Map<String, String> map;
	
	@Override
	public Object saveOcsPayment(OcsPayment ocsPayment, String traceId)throws DCPEException {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String requestString = DCPEUtil.convertToString(ocsPayment);
		LOGGER.info("saveOcsPaymentRequest : traceId={}|OcsPaymentSeriveImpl={}", traceId, requestString);
		Instant start = Instant.now();
		
		List<Object> param = new ArrayList<>();

		param.add(ocsPayment.getReqId());
		param.add(ocsPayment.getAccountNo());
		param.add(ocsPayment.getContractNo());
		param.add(ocsPayment.getConnRef());
		param.add(ocsPayment.getProductCategory());
		param.add(ocsPayment.getSbu());
		param.add(ocsPayment.getAccountType());
		
		if(ocsPayment.getPhysicalPaymentDate() != null) {
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		param.add(Timestamp.valueOf(df.format(ocsPayment.getPhysicalPaymentDate().getTime())));
		}else {
			param.add(null);
		}
		

		if(ocsPayment.getPaymentAmount() == null) {
			param.add(java.sql.Types.DOUBLE);
		}else {
		param.add(ocsPayment.getPaymentAmount());
		}
		param.add(ocsPayment.getPaymentRef());
		param.add(ocsPayment.getReceiptNo());
		param.add(ocsPayment.getReceiptBranch());
		param.add(ocsPayment.getBranchCounter());
		param.add(ocsPayment.getReceiptUser());
	
		if(ocsPayment.getReceiptDate() != null) {
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		param.add(Timestamp.valueOf(df.format(ocsPayment.getReceiptDate().getTime())));
		}else {
			param.add(null);
		}
		
		param.add(ocsPayment.getPaymentMode());
		param.add(ocsPayment.getChqNo());
		param.add(ocsPayment.getChqBank());
		param.add(ocsPayment.getChqBranch());
		param.add(ocsPayment.getRemarks());
		param.add(ocsPayment.getReasonCode());
		param.add(ocsPayment.getProductType());
		param.add(ocsPayment.getContactNo());
		param.add(ocsPayment.getTranTypeId());
		param.add(ocsPayment.getErrorDesc());
		int result = 0;
		try {
			LOGGER.info("insert Ocs Payment Data : SQL_OCS_PAYMENT_POST traceId={}", traceId);

			result = (int) queryExcecuterWriterRepository.insertData(map.get(SQLQueryEnum.SQL_OCS_PAYMENT_POST.getValue()),param.toArray(), traceId);

		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("saveOcsPaymentResponse : traceId={}|timeTaken={}|result={}", traceId, timeTaken, result);

		return result;

	}

}
