package lk.dialog.pe.persistence.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
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
import lk.dialog.pe.persistence.domain.ForcefulRealizeCheques;
import lk.dialog.pe.persistence.service.ForcefulRealizeChequesService;
import lk.dialog.pe.persistence.writer.repository.QueryExcecuterWriterRepository;

@Service
public class ForcefulRealizeChequesServiceImpl implements ForcefulRealizeChequesService {

	@Autowired
	private QueryExcecuterWriterRepository queryExcecuterWriterRepository;

	@Autowired
	@Qualifier("paymentMap")
	private Map<String, String> paymentMap;

	private static final Logger LOGGER = LoggerFactory.getLogger(ForcefulRealizeChequesServiceImpl.class);

	@Override
	public Object saveForcefulRealizeCheques(ForcefulRealizeCheques forcefulRealizeCheques, String traceId)
			throws DCPEException,ParseException{
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String requestString = DCPEUtil.convertToString(forcefulRealizeCheques);
		LOGGER.info("saveForcefulRealizeChequesRequest : traceId={}|ForcefulRealizeCheques={}", traceId, requestString);
		Instant start = Instant.now();
		ArrayList<Object> param = new ArrayList<>();

		param.add(forcefulRealizeCheques.getReqId());
		param.add(forcefulRealizeCheques.getAccountNo());
		param.add(forcefulRealizeCheques.getContractId());
		param.add(forcefulRealizeCheques.getProductCategory());
		param.add(forcefulRealizeCheques.getSbu());
		param.add(forcefulRealizeCheques.getAccountType());
		param.add(forcefulRealizeCheques.getPaymentAmount());
		param.add(forcefulRealizeCheques.getChqBank());
		param.add(forcefulRealizeCheques.getChqBranch());
		param.add(forcefulRealizeCheques.getChqNo());
		

		param.add(forcefulRealizeCheques.getChqDate());
		
		param.add(forcefulRealizeCheques.getTerminalId());
		param.add(forcefulRealizeCheques.getReceiptNo());
		param.add(forcefulRealizeCheques.getReceiptBranch());
		param.add(forcefulRealizeCheques.getBranchCounter());
		param.add(forcefulRealizeCheques.getReceiptDate());
		param.add(forcefulRealizeCheques.getReceiptUser());
		param.add(forcefulRealizeCheques.getPhysicalPaymentDate());
		param.add(forcefulRealizeCheques.getProductType());
		param.add(forcefulRealizeCheques.getRealTime());
		param.add(forcefulRealizeCheques.getConnRef());
		param.add(forcefulRealizeCheques.getPaymentRef());
		param.add(forcefulRealizeCheques.getRemarks());
		param.add(forcefulRealizeCheques.getConnectionStatus());

		int result = 0;

		try {
			LOGGER.info("saveForcefulRealizeCheques : traceId={}|body={}", traceId, param.toArray());
			result = (int) queryExcecuterWriterRepository.insertData(
					paymentMap.get(SQLQueryEnum.SQL_CHQ_FORCEFUL_REALIZE.getValue()), param.toArray(), traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);

		LOGGER.info("saveForcefulRealizeChequesRespose : traceId={}|timeTaken={}|body={}", traceId, timeTaken, result);

		return result;

	}

}
