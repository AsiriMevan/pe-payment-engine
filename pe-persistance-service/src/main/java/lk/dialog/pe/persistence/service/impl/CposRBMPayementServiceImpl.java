package lk.dialog.pe.persistence.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import lk.dialog.pe.persistence.domain.RBMPayment;
import lk.dialog.pe.persistence.service.CposRBMPayementService;
import lk.dialog.pe.persistence.writer.repository.QueryExcecuterWriterRepository;

@Service
public class CposRBMPayementServiceImpl implements CposRBMPayementService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CposRBMPayementServiceImpl.class);

	@Autowired
	private QueryExcecuterWriterRepository queryExcecuterWriterRepository;

	@Autowired
	@Qualifier("paymentMap")
	private Map<String, String> map;

	@Override
	public Object saveRBMpayment(RBMPayment rbmPayment, String traceId) throws DCPEException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String requestString = DCPEUtil.convertToString(rbmPayment);
		LOGGER.info("saveRBMpaymentRequestServiceImple : traceId={}|RBMPaymentServiceImple={}", traceId, requestString);
		Instant start = Instant.now();

		List<Object> param = new ArrayList<>();

		param.add(rbmPayment.getReqId());
		param.add(rbmPayment.getAccountNo());
		param.add(rbmPayment.getContractNo());
		param.add(rbmPayment.getConnRef());
		param.add(rbmPayment.getProductCategory());
		param.add(rbmPayment.getSbu());
		param.add(rbmPayment.getAccountType());

		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		param.add(Timestamp.valueOf(df.format(rbmPayment.getPhysicalPaymentDate().getTime())));

		param.add(rbmPayment.getPaymentAmount());
		param.add(rbmPayment.getPaymentCurrency());
		param.add(rbmPayment.getPaymentMethodId());
		param.add(rbmPayment.getPhysicalPaymentText());
		param.add(rbmPayment.getPaymentRef());
		param.add(rbmPayment.getReceiptNo());
		param.add(rbmPayment.getReceiptBranch());
		param.add(rbmPayment.getBranchCounter());
		param.add(rbmPayment.getReceiptUser());

		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		param.add(Timestamp.valueOf(df.format(rbmPayment.getReceiptDate().getTime())));

		param.add(rbmPayment.getPaymentMode());
		param.add(rbmPayment.getPaymentText());
		param.add(rbmPayment.getTerminalId());
		param.add(rbmPayment.getChqNo());
		param.add(rbmPayment.getChqBank());
		param.add(rbmPayment.getChqBranch());

		if (rbmPayment.getChqDate() != null) {
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			param.add(Timestamp.valueOf(df.format(rbmPayment.getChqDate().getTime())));
		} else {
			param.add(null);
		}

		param.add(rbmPayment.getChqSuspend());
		param.add(rbmPayment.getCardType());
		param.add(rbmPayment.getCardAgent());
		param.add(rbmPayment.getCardNo());
		param.add(rbmPayment.getCardAprvCode());
		param.add(rbmPayment.getModuleName());
		param.add(rbmPayment.getTransferredType());
		param.add(rbmPayment.getTransferredMode());
		param.add(rbmPayment.getTransferredNo());
		param.add(rbmPayment.getRemarks());
		param.add(rbmPayment.getTransferReasonCode());
		param.add(rbmPayment.getProductType());
		param.add(rbmPayment.getContactNo());
		param.add(rbmPayment.getConnectionStatus());

		int result = 0;

		try {
			LOGGER.info("QUERY : SQL_RBM_PAYMENT_POST traceID={}", traceId);
			result = (int) queryExcecuterWriterRepository
					.insertData(map.get(SQLQueryEnum.SQL_RBM_PAYMENT_POST.getValue()), param.toArray(), traceId);

		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("saveRBMPaymentResonse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, result);
		return result;

	}

}
