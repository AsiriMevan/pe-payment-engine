package lk.dialog.pe.persistence.service.impl;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.SQLQueryEnum;
import lk.dialog.pe.persistence.domain.DBNPayment;
import lk.dialog.pe.persistence.service.CposDBNPayementService;
import lk.dialog.pe.persistence.writer.repository.QueryExcecuterWriterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
public class CposDbnPaymentServiceImpl implements CposDBNPayementService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CposDbnPaymentServiceImpl.class);

	@Autowired
	private QueryExcecuterWriterRepository queryExcecuterWriterRepository;

	@Autowired
	@Qualifier("paymentMap")
	private Map<String, String> map;

	@Override
	public Object saveDbnPayment(DBNPayment dbnPayment, String traceId) throws DCPEException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String requestString = DCPEUtil.convertToString(dbnPayment);
		LOGGER.info("saveDbnPaymentRequest : traceId = [{}] | Request = [{}]", traceId, requestString);
		Instant start = Instant.now();

		List<Object> param = new ArrayList<>();

		param.add(dbnPayment.getReqId());
		param.add(dbnPayment.getAccountNo());
		param.add(dbnPayment.getContractNo());
		param.add(dbnPayment.getConnRef());
		param.add(dbnPayment.getAccountType());

		param.add(dbnPayment.getPaymentAmount());
		param.add(dbnPayment.getPaymentMethodId());
		param.add(dbnPayment.getPaymentMode());
		param.add(dbnPayment.getPaymentRef());

		param.add(dbnPayment.getChqBank());
		param.add(dbnPayment.getChqNo());
		param.add(dbnPayment.getChqBranch());

		if (dbnPayment.getChqDate() != null) {
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			param.add(Timestamp.valueOf(df.format(dbnPayment.getChqDate().getTime())));
		} else {
			param.add(null);
		}

		param.add(dbnPayment.getChqSuspend());

		param.add(dbnPayment.getCardType());
		param.add(dbnPayment.getCardAgent());
		param.add(dbnPayment.getCardNo());
		param.add(dbnPayment.getCardAprvCode());

		param.add(dbnPayment.getReceiptNo());
		param.add(dbnPayment.getReceiptBranch());
		param.add(dbnPayment.getBranchCounter());
		param.add(dbnPayment.getReceiptUser());

		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		param.add(Timestamp.valueOf(df.format(dbnPayment.getReceiptDate().getTime())));

		param.add(dbnPayment.getTransferredType());
		param.add(dbnPayment.getTransferredMode());
		param.add(dbnPayment.getTransferredNo());

		param.add(dbnPayment.getRemarks());
		param.add(dbnPayment.getContactNo());
		param.add(dbnPayment.getTransferReasonCode());

		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		param.add(Timestamp.valueOf(df.format(dbnPayment.getPhysicalPaymentDate().getTime())));


		param.add(dbnPayment.getProductType());
		param.add(dbnPayment.getTerminalId());
		param.add(dbnPayment.getModuleName());
		param.add(dbnPayment.getInvoiceList());
		param.add(dbnPayment.getConnectionStatus());

		int result;

		try {
			LOGGER.info("QUERY : SQL_DBN_PAYMENT_POST traceID = [{}]", traceId);
			result = (int) queryExcecuterWriterRepository
					.insertData(map.get(SQLQueryEnum.SQL_DBN_PAYMENT_POST.getValue()), param.toArray(), traceId);

		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("saveDbnPaymentRequest : traceId = [{}] | timeTaken = [{}] | body = [{}]", traceId, timeTaken, result);
		return result;

	}

}
