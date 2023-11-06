package lk.dialog.pe.billing.service.impl;

import java.util.Arrays;
import java.util.List;

import lk.dialog.pe.billing.service.MifeIntegrationService;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.credit.cam.dto.FixedNumberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.dialog.pe.billing.domain.AccountRef;
import lk.dialog.pe.billing.service.PersistanceIntegrationService;
import lk.dialog.pe.billing.service.QueryRemarkService;
import lk.dialog.pe.common.util.DCPEErrorEnum;
import lk.dialog.pe.common.util.ErrorCodeEnum;
import lk.dialog.pe.billing.util.HotlineRemarkRequest;
import lk.dialog.pe.billing.util.HotlineRemarkResponse;
import lk.dialog.pe.billing.util.RemarkInfo;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.ProductTypeEnum;

import java.util.*;
//todo - no logs had been added and logger is introduced when developing TelBiz path
@Service
public class QueryRemarkServiceImpl implements QueryRemarkService {
	

	@Autowired
	private PersistanceIntegrationService persistanceIntegrationService;

	@Autowired
	private MifeIntegrationService mifeIntegrationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryRemarkServiceImpl.class);

	@Override
	public HotlineRemarkResponse getHotlineRemarks(HotlineRemarkRequest hotlineRemarkRequest, String traceId) {

		List<AccountRef> accounts = hotlineRemarkRequest.getAccounts();
		boolean hasWifiContracts = false;
		boolean hasCcbsContracts = false;

		HotlineRemarkResponse remarkResponse = new HotlineRemarkResponse();
		List<String> volteContractList = new ArrayList<>();

		ProductCategoryEnum productCategory = ProductCategoryEnum.getProductCategoryByValue(hotlineRemarkRequest.getProductCategory());

		switch (productCategory) {
		case CCBS: {
			List<Boolean> checkedValues = this.hasCcbsAndWifiContract(accounts);
			hasWifiContracts = checkedValues.get(0);
			hasCcbsContracts = checkedValues.get(1);
			List<RemarkInfo> remarkInfoList = null;
			if (hasCcbsContracts || hasWifiContracts) {
				try {
					remarkInfoList = this.getRemarkInfoListForCcbs(hasCcbsContracts, hasWifiContracts, hotlineRemarkRequest, traceId);
				} catch (Exception e) {			
					remarkResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
					remarkResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_FAIL.getErrorCode());
					remarkResponse.setErrorDesc((e.getCause() == null) ? e.getMessage() : e.getCause().toString());
					break;
				}
			} else {
				remarkResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				remarkResponse.setErrorCode(ErrorCodeEnum.ERR_INPUT_DATA.getErrorCode());
				remarkResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_INPUT_DATA.getErrorCode());
				break;
			}
			remarkResponse.setAccounts(remarkInfoList);
			remarkResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
			remarkResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
			remarkResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
			break;
		}

		case TELBIZ:
			remarkResponse = setupTelbiz(accounts, volteContractList, hasCcbsContracts, traceId);
			break;

		case NFC: 
			remarkResponse = setupNFC();
				break;
		
		default: 
			remarkResponse = setupFailResponse();
		
		}
		return remarkResponse;
	}

	List<RemarkInfo> getRemarkInfoListForCcbs(boolean hasCcbsContracts, boolean hasWifiContracts, HotlineRemarkRequest hotlineRemarkRequest, String traceId) throws DCPEException {
		List<RemarkInfo> remarkInfoList = null;
		if (hasCcbsContracts) {
			remarkInfoList = persistanceIntegrationService.getHotlineRemarks(hotlineRemarkRequest, traceId);//PT= 0 2 3 4 5 6(any of this)
		}

		if (hasWifiContracts) {
			remarkInfoList = persistanceIntegrationService.getCcbsWifiRemarks(hotlineRemarkRequest, traceId);//PT = only 1
		}
		if (remarkInfoList != null && !remarkInfoList.isEmpty() && remarkInfoList.get(0).getRemarks().get(0).getCreatedDate()!=null) {
			remarkInfoList.get(0).getRemarks().get(0).setCreatedDate(remarkInfoList.get(0).getRemarks().get(0).getCreatedDate());
		}
		return remarkInfoList;
	}

	List<Boolean> hasCcbsAndWifiContract(List<AccountRef> accounts) {
		boolean hasWifiContracts = false;
		boolean hasCcbsContracts = false;
		for (AccountRef accountRef: accounts) {
			if (accountRef.getContractNo() != null && !"".equals(accountRef.getContractNo())) {
				if (accountRef.getProductType() == ProductTypeEnum.WIFI.getType()) {
					hasWifiContracts = true;
				} else {
					hasCcbsContracts = true;
				}
			}
		}
		return Arrays.asList(hasWifiContracts, hasCcbsContracts);
	}


	private HotlineRemarkResponse setupNFC() {
		HotlineRemarkResponse remarkResponse= new HotlineRemarkResponse();
		remarkResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
		remarkResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_CUSTOMER_NFC.getErrorCode());
		remarkResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_CUSTOMER_NFC.getErrorCode());
		return remarkResponse;
	}
	
	private HotlineRemarkResponse setupFailResponse() {
		HotlineRemarkResponse remarkResponse = new HotlineRemarkResponse();
		remarkResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
		remarkResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_INVALID_PC.getErrorCode());
		remarkResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_INVALID_PC.getErrorCode());
		return remarkResponse;
	}

	private HotlineRemarkResponse setupTelbiz(List<AccountRef> accounts, List<String> volteContractList, boolean hasCcbsContracts, String traceId) {
		LOGGER.info("setupTelbiz Request : traceId=[{}] | accounts=[{}]", traceId, accounts);
		HotlineRemarkResponse remarkResponse = new HotlineRemarkResponse();

		for (AccountRef accountRef : accounts) {
			String accountNumber = accountRef.getContractNo();
			LOGGER.info("setupTelbiz loop accounts: traceId=[{}] | accountNumber=[{}]", traceId, accountNumber);
			if (accountNumber != null && !accountNumber.isEmpty()) {
				FixedNumberDTO fixedNumberDetailsDTO;
				try {
					fixedNumberDetailsDTO = mifeIntegrationService.validateSystemAvailability(accountNumber, "AccountNo", traceId);
					String crmSystem = fixedNumberDetailsDTO.getCrmSystem();

					if (("CCBS".equalsIgnoreCase(crmSystem) || "DCS".equalsIgnoreCase(crmSystem))) {
						volteContractList.add(accountNumber);
						hasCcbsContracts = true;

					}

				} catch (Exception e) {
					remarkResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
					remarkResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_FAIL.getErrorCode());
					remarkResponse.setErrorDesc((e.getCause() == null) ? e.getMessage() : e.getCause().toString());
					return remarkResponse;
				}

			}
		}
		List<RemarkInfo> remarkInfoList;
		List<RemarkInfo> volteRemarkList;
		LOGGER.info("setupTelbiz : traceId=[{}] | hasCcbsContracts=[{}]", traceId, hasCcbsContracts);
		if (hasCcbsContracts) {
			try {
				volteRemarkList = persistanceIntegrationService.getDcsRemarks(volteContractList, traceId);;
			} catch (Exception e) {
				remarkResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				remarkResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_FAIL.getErrorCode());
				remarkResponse.setErrorDesc((e.getCause() == null) ? e.getMessage() : e.getCause().toString());
				return remarkResponse;
			}

		} else {
			remarkResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
			remarkResponse.setErrorCode(ErrorCodeEnum.ERR_INPUT_DATA.getErrorCode());
			remarkResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_INPUT_DATA.getErrorCode());
			return remarkResponse;
		}

		LOGGER.info("setupTelbiz : traceId=[{}] | volteRemarkList=[{}]", traceId, volteRemarkList);
		if (volteRemarkList != null && !volteRemarkList.isEmpty()) {
            remarkResponse.setAccounts(volteRemarkList);
			remarkResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
			remarkResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
			remarkResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
            return remarkResponse;
        } else {
            remarkInfoList = new ArrayList<>();
            remarkResponse.setAccounts(remarkInfoList);
			remarkResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
			remarkResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
			remarkResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
			return remarkResponse;
        }

	}
}
