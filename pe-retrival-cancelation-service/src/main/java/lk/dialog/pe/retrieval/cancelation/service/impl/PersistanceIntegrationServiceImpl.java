package lk.dialog.pe.retrieval.cancelation.service.impl;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.RestExecutor;
import lk.dialog.pe.retrieval.cancelation.domain.PaymentDTO;
import lk.dialog.pe.retrieval.cancelation.service.PersistanceIntegrationService;
import lk.dialog.pe.retrieval.cancelation.util.QueryPaymentsSummery;

@Service
public class PersistanceIntegrationServiceImpl implements PersistanceIntegrationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersistanceIntegrationServiceImpl.class);

	@Autowired
	RestExecutor restExecutor;

	@Value("${api.pe-persistance.get-contract-subsidiary-type}")
	String getContractSubsidiaryType;

	@Value("${api.pe-persistance.get-cpos-paymode}")
	String getCpodId;

	@Value("${api.pe.scheduler.get-payments-summery}")
	String getPaymentsSummery;
	
	@Autowired
	ObjectMapper mapper;

	@Override
	public String findContractSubsidiaryTypeById(String contractId, boolean dcsOnly, String traceId) throws DCPEException {

		LOGGER.info("findContractSubsidiaryTypeByIdRequest : traceId={}", traceId);
		Instant start = Instant.now();

		String uri = getContractSubsidiaryType.replace("{contractId}", contractId).replace("{dcsOnly}", dcsOnly ? "true" : "false");
		String response = restExecutor.get(uri, traceId, String.class);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("findContractSubsidiaryTypeByIdResponse : traceId={}|timeTaken={}", traceId, timeTaken);
		return response;

	}

	@Override
	public String getPayCposModeToRBMdata(String id, String traceId) throws DCPEException {

		LOGGER.info("getPayCposModeToRBMdataRequest : traceId={}", traceId);
		Instant start = Instant.now();

		String uri = getCpodId.replace("{id}", id).replace("{isRbm}", "true");
		String response = restExecutor.get(uri, traceId, String.class);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getPayCposModeToRBMdataResponse : traceId={}|timeTaken={}", traceId, timeTaken);
		return response;
	}

	@Override
	public List<PaymentDTO> getPaymentsSummery(QueryPaymentsSummery queryPaymentsSummery, String traceId) throws DCPEException {
		
		LOGGER.info("getPaymentsSummeryRequest : traceId={}| uri={}", traceId, getPaymentsSummery);
		Instant start = Instant.now();
		
		List<PaymentDTO>  resDTO= restExecutor.post(getPaymentsSummery, traceId, queryPaymentsSummery, new TypeReference<List<PaymentDTO>>() {});
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getPaymentsSummeryResponse : traceId={}|timeTaken={}", traceId, timeTaken);

		return resDTO;
	}

}
