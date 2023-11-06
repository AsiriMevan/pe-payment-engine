package lk.dialog.pe.customer.info.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.credit.cam.dto.*;
import lk.dialog.pe.credit.cam.service.ccbs.CCBSNumberDetailService;
import lk.dialog.pe.credit.cam.service.common.MifeApiService;
import lk.dialog.pe.credit.cam.util.MifeUrlEnum;
import lk.dialog.pe.customer.info.domain.NEConstants;
import lk.dialog.pe.customer.info.domain.RBMAccount;
import lk.dialog.pe.customer.info.domain.RBMCustomer;
import lk.dialog.pe.customer.info.service.MifeIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MifeIntegrationServiceImpl implements MifeIntegrationService {

	@Autowired
	private MifeApiService mifeApiService;

	@Autowired
	private CCBSNumberDetailService ccbsNumberDetailService;

	private static final Logger LOGGER = LoggerFactory.getLogger(MifeIntegrationServiceImpl.class);

	@Override
	public RBMCustomer findCustomerByAccountRef(String accountNum, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("findCustomerByAccountRef : traceId={}|accountNum={}", traceId, accountNum);

		RBMFindCustomerRequest rbmFindCustomerRequest = new RBMFindCustomerRequest();
		IntegratorContextRequest integratorContextRequest = new IntegratorContextRequest();
		FindCustomerContextRq findCustomerContextRq = new FindCustomerContextRq();
		FindCustomerArrRq findCustomerArrRq = new FindCustomerArrRq();

		rbmFindCustomerRequest.setMaxRows(1);
		rbmFindCustomerRequest.setExactMatchRequired(true);
		rbmFindCustomerRequest.setSoundsLike(false);
		rbmFindCustomerRequest.setWhenDtm("10/03/2016 02:30:20 IST");
		rbmFindCustomerRequest.setLanguageId(1);
		rbmFindCustomerRequest.setAccountNumSearch(accountNum);
		rbmFindCustomerRequest.setCustomerNameSearch("1");
		rbmFindCustomerRequest.setUserId("PE");
		rbmFindCustomerRequest.setAppName("PE");
		integratorContextRequest.setRequestId("w");

		List<FindCustomerArrRq> findCustomerArrRqList = new ArrayList<>();
		findCustomerArrRq.setName("PE");
		findCustomerArrRq.setValue(null);
		findCustomerArrRqList.add(findCustomerArrRq);
		findCustomerContextRq.setFindCustomerArrRq(findCustomerArrRqList);
		integratorContextRequest.setFindCustomerContextRq(findCustomerContextRq);
		rbmFindCustomerRequest.setIntegratorContextRequest(integratorContextRequest);

		String rbmFindCustomerRequestString = DCPEUtil.convertToString(rbmFindCustomerRequest);
		LOGGER.info("findCustomerByAccountRefRequest : traceId={}|rbmFindCustomerRequest={}", traceId, rbmFindCustomerRequestString);
		RBMFindCustomerResultResponse findCustOutput1 = null;
		try {
			findCustOutput1 = mifeApiService.post(MifeUrlEnum.RBM_FIND_CUSTOMER, rbmFindCustomerRequest, traceId);
			LOGGER.info("findCustomerByAccountRefRequest : traceId={}|findCustOutput={}", traceId, findCustOutput1);

		} catch (Exception e) {
			Long  timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("findCustomerByAccountRefResponse-ERROR: traceId={}|timeTaken={}|error={}", traceId, timeTaken, e.getMessage(), e);

		}

		String response = DCPEUtil.convertToString(findCustOutput1);
		LOGGER.info("findCustomerByAccountRefResponse : traceId={}|response={}", traceId, response);

		RBMCustomer customer = new RBMCustomer();
		final FindCustomerElement customerElement = findCustOutput1 != null ? findCustOutput1.getFindCustomerElement() : null;
		List<DataSet> customerDataSet = customerElement != null ? customerElement.getDataSet() : null;
		if (customerDataSet != null && !customerDataSet.isEmpty()) {
			final CustomerSummary customerSummary = customerDataSet.get(0).getCustomerSummary();
			customer.setCustRef(customerSummary.getCustomerRef());
			final ContactSummary contactSummary = customerDataSet.get(0).getContactSummary();
			customer.setCustName(
					((contactSummary.getSalutationName() != null ?  contactSummary.getSalutationName() + " " : "")
							+ (contactSummary.getLastName() != null ? contactSummary.getLastName() + " " : ""))
									.trim());
			customer.setTitle(contactSummary.getTitle());

			final AddressData addressData = customerDataSet.get(0).getAddressData();
			customer.setPostalCode(addressData.getZipcode());
			final List<String> addressLines = addressData.getAddressLines();
			customer.setAddrLine1(addressLines.get(0));
			customer.setAddrLine2(addressLines.get(1));
			customer.setAddrLine3(addressLines.get(2));
		}
		
		
		String responseString = DCPEUtil.convertToString(customer);
		Long  timeTaken1 = DCPEUtil.getTimeTaken(start);
		LOGGER.info("findCustomerByAccountRefResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken1, responseString);
		return customer;
	}

	@Override
	public RBMAccount readAllAccountAttributes(String accountNum, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("readAllCustomerAttributesRequest : traceId={}|accountNum={}", traceId, accountNum);

		RBMAccount rbmAccount = new RBMAccount();
		RBMReadAllAccountAttributesDTO rbmReadAllAccountAttributesDTO = new RBMReadAllAccountAttributesDTO();
		Map<String, String> map = new HashMap<>();
		map.put("accountNum" , accountNum);
		map.put("traceId" , traceId);

		try {
			Object dataObject = mifeApiService.getWithUriParams(MifeUrlEnum.CON_STATUS_AND_SBU_INFO, map, traceId);

			ObjectMapper objectMapper = new ObjectMapper();
			String responseData = objectMapper.writeValueAsString(dataObject);
			RBMReadAllAccountAttributesDTO respMap = objectMapper.readValue(responseData, RBMReadAllAccountAttributesDTO.class);

			if (respMap != null) {
				for (ApiResponseData apiResponseData : respMap.getData()) {
					if ("CONTRACT_SUBSIDIARY_TYPE".equalsIgnoreCase(apiResponseData.getName())) {
						rbmAccount.setSbu(apiResponseData.getValue());
					} else if ("NETWORK_STATUS".equalsIgnoreCase(apiResponseData.getName())) {
						rbmAccount.setConStatus(apiResponseData.getValue());
					}
				}
			}

			rbmAccount.setAccountNum(accountNum);
			rbmAccount.setAccountType(NEConstants.POSTPAID);

		} catch (Exception e) {
			Long timeTaken = DCPEUtil.getTimeTaken(start);
			//todo - done - why exception is not thrown here
			//todo - done - log.info might be wrong here
			LOGGER.error("readAllAccountAttributes - ERROR : traceId={}|timeTaken={}|error={}", traceId, timeTaken, e.getMessage(), e);
			throw new DCPEException(e.getMessage());
		}

		String rbmAccountToString = DCPEUtil.convertToString(rbmAccount);
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("readAllAccountAttributesResponse : traceId={}|timeTaken={}|rbmCustomer={}", traceId, timeTaken, rbmAccountToString);

		return rbmAccount;
	}

	@Override
	public RBMCustomer readAllCustomerAttributes(String accountNum, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("readAllCustomerAttributesRequest : traceId={}|accountNum={}", traceId, accountNum);

		RBMCustomer rbmCustomer = new RBMCustomer();
		RBMReadAllCustomerAttributesDTO rbmReadAllCustomerAttributesDTO =  new RBMReadAllCustomerAttributesDTO();
		Map<String, String> map = new HashMap<>();
		map.put("accountNum" , accountNum);
		map.put("traceId" , traceId);

		try {
			Object data = mifeApiService.getWithUriParams(MifeUrlEnum.BILL_RUN_CODE_AND_PR_CUSTOMER_REF_INFO, map, traceId);

			ObjectMapper objectMapper = new ObjectMapper();
			String respData = objectMapper.writeValueAsString(data);
			RBMReadAllCustomerAttributesDTO respMap = objectMapper.readValue(respData, RBMReadAllCustomerAttributesDTO.class);

			if (respMap != null) {
				for(ApiResponseData apiResponseData : respMap.getData()){
					if ("BILL_RUN_CODE".equalsIgnoreCase(apiResponseData.getName())){
						rbmCustomer.setBillRunCode(apiResponseData.getValue());
					} else if ("PR_CUSTOMER_REF".equalsIgnoreCase(apiResponseData.getName())) {
						rbmCustomer.setPrCustRef(apiResponseData.getValue());
					} else if ("IDENTIFICATION_NUMBER".equalsIgnoreCase(apiResponseData.getName())) {
						rbmCustomer.setCustRef(apiResponseData.getValue());
					}
				}
			}
		} catch (Exception e) {
			Long timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("readAllCustomerAccountAttributes - ERROR : traceId={}|timeTaken={}|error={}", traceId, timeTaken, e.getMessage(), e);
		}


		String rbmCustomerToString = DCPEUtil.convertToString(rbmCustomer);
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("readAllCustomerAttributesResponse : traceId={}|timeTaken={}|rbmCustomer={}", traceId, timeTaken, rbmCustomerToString);

		return rbmCustomer;
	}

    @Override
    public FixedNumberDTO validateSystemAvailability(String accountNum, String connectionRefType, String traceId) throws DCPEException {
        Instant start = Instant.now();
        LOGGER.info("validateSystemAvailabilityRequest : traceId=[{}]|accountNum=[{}]", traceId, accountNum);

		FixedNumberDTO fixedNumberDTO;

        try {
			Map<String, String> map = new HashMap<>();
			map.put("connectionRef", accountNum);
			map.put("connectionRefType", connectionRefType);
			fixedNumberDTO = mifeApiService.getWithUriParams(MifeUrlEnum.SYS_VALIDATOR,
					map, traceId);
        } catch (Exception e) {
            Long timeTaken = DCPEUtil.getTimeTaken(start);
            LOGGER.error("validateSystemAvailability - ERROR : traceId=[{}]|timeTaken=[{}]|error=[{}]", traceId, timeTaken, e.getMessage(), e);
			throw new DCPEException(e.getMessage());
        }

        String fixedNumberDtoToString = DCPEUtil.convertToString(fixedNumberDTO);
        Long  timeTaken = DCPEUtil.getTimeTaken(start);
        LOGGER.info("validateSystemAvailabilityResponse : traceId=[{}]|timeTaken=[{}]|fixedNumberDto=[{}]", traceId, timeTaken, fixedNumberDtoToString);

        return fixedNumberDTO;
    }
}
