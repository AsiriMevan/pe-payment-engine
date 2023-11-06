package lk.dialog.pe.persistence.service.impl;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.SQLQueryEnum;
import lk.dialog.pe.persistence.reader.repository.QueryExcecuterReaderRepository;
import lk.dialog.pe.persistence.service.NextPaymentService;

@Service
public class NextPaymentServiceImpl implements NextPaymentService {
	
	@Autowired
	private QueryExcecuterReaderRepository queryExcecuterReaderRepository;
	
	@Autowired
	@Qualifier("paymentMap")
	private  Map<String, String>  paymentMap;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NextPaymentServiceImpl.class);
	
	@Override
	public Long nextPaymentSequence(String traceId) throws DCPEException {
		
		LOGGER.info("nextPaymentSequenceRequest : traceId={}", traceId);
		Instant start = Instant.now();		
		Object nextVal = null;
		try {
			nextVal = queryExcecuterReaderRepository.getSingleObject(paymentMap.get(SQLQueryEnum.SQL_PAYMENT_NEXT_SEQ.getValue()),new Object[] {}, traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}
		
		Long value = Long.parseLong(String.valueOf(nextVal));
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("nextPaymentSequenceResponse : traceId={}|timeTaken={}|value={}", traceId, timeTaken, value);

		return value;			
	}
}
