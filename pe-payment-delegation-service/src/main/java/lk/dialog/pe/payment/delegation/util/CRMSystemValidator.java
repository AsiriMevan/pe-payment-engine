package lk.dialog.pe.payment.delegation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lk.dialog.pe.payment.delegation.domain.ErrorCodeEnum;

public class CRMSystemValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(CRMSystemValidator.class);

	private CRMSystemValidator() {
	}

	public static String isValidSystemAgainstPcSbu(Integer productCategory, Integer sbu, String traceId) {

		String status;

		switch (productCategory) {

		case Constants.PC_CCBS:
			status = getCCBSSbuTypeInPcSbu(sbu, traceId);

			break;
		case Constants.PC_TELBIZ:

			status = getTELBIZSbuTypeInPcSbu(sbu, traceId);
			break;
		case Constants.PC_NFC:

			status = getNFCSbuTypeInPcSbu(sbu, traceId);
			break;
		default:
			LOGGER.info("Invalid PC : traceId={}", traceId);
			status = Constants.ERR_CODE_INVALID_PC;
			break;
		}

		return status;

	}

	private static String getNFCSbuTypeInPcSbu(Integer sbu, String traceId) {
		String status;
		if (sbu == Constants.SBU_GSM) {
			LOGGER.info("getNFCSbuTypeInPcSbu :SBU_GSM traceId={}", traceId);
			status = Constants.ERR_CODE_VALID;

		} else {
			LOGGER.info("getNFCSbuTypeInPcSbu : Invalid SBU traceId={}", traceId);
			status = Constants.ERR_CODE_INVALID_SBU;
		}
		return status;
	}

	private static String getTELBIZSbuTypeInPcSbu(Integer sbu, String traceId) {
		String status;
		if (sbu == Constants.SBU_DBN) {
			LOGGER.info("getTELBIZSbuTypeInPcSbu : SBU_DBN traceId={}", traceId);
			status = Constants.ERR_CODE_VALID;

		} else {
			LOGGER.info("getTELBIZSbuTypeInPcSbu : Invalid SBU traceId={}", traceId);
			status = Constants.ERR_CODE_INVALID_SBU;
		}
		return status;
	}

	private static String getCCBSSbuTypeInPcSbu(Integer sbu, String traceId) {
		String status;
		switch (sbu) {
		case Constants.SBU_ALL:
			LOGGER.info("getCCBSSbuTypeInPcSbu : SBU_ALL traceId={}", traceId);
			status = Constants.ERR_CODE_VALID;
			break;
		case Constants.SBU_DTV_PRE:
			LOGGER.info("getCCBSSbuTypeInPcSbu : SBU_DTV_PRE traceId={}", traceId);
			status = Constants.ERR_CODE_VALID;
			break;
		case Constants.SBU_DTV_POST:
			LOGGER.info("getCCBSSbuTypeInPcSbu : SBU_DTV_POST traceId={}", traceId);
			status = Constants.ERR_CODE_VALID;
			break;
		case Constants.SBU_GSM:
			LOGGER.info("getCCBSSbuTypeInPcSbu :SBU_GSM traceId={}", traceId);
			status = Constants.ERR_CODE_VALID;
			break;

		default:
			LOGGER.info("getCCBSSbuTypeInPcSbu :Invalid SBU traceId={}", traceId);
			status = Constants.ERR_CODE_INVALID_SBU;
			break;
		}
		return status;
	}

	public static String isValidSystemAgainstPcSbuPt(Integer productCategory, Integer sbu, Integer productType, String traceId) {

		String status;

		if (productCategory == null) {

			return ErrorCodeEnum.ERR_CODE_INVALID_PC.getErrorCode();

		}
		if (sbu == null) {
			return ErrorCodeEnum.ERR_CODE_INVALID_SBU.getErrorCode();
		}
		if (productType == null) {
			return ErrorCodeEnum.ERR_CODE_INVALID_PT.getErrorCode();
		}
		switch (productCategory) {// 1
		case Constants.PC_CCBS:
			status = getCCBSSbuTypeInPcSbuPt(sbu, productType, traceId);

			LOGGER.info("PC_CCBS :traceId={}", traceId);

			break;
//todo - done - add telbiz switch case | [pe-common/src/main/java/lk/dialog/crm/pe/util/CRMSystemValidator.java:172]
		case Constants.PC_TELBIZ:
			status = getTelbizSbuTypeInPcSbuPt(sbu, productType, traceId);
			LOGGER.info("PC_TELBIZ :traceId=[{}]", traceId);
			break;
		case Constants.PC_NFC:
			if (sbu == Constants.SBU_GSM) {

				LOGGER.info("SBU_GSM :traceId={}", traceId);

				if (productType == Constants.PT_NFC) {
					status = Constants.ERR_CODE_VALID;

				} else {
					status = Constants.ERR_CODE_INVALID_PT;
				}

			} else {

				LOGGER.info("Invalid SBU :traceId={}", traceId);
				status = Constants.ERR_CODE_INVALID_SBU;
			}
			break;

		default:

			LOGGER.info("Invalid PC :traceId={}", traceId);
			status = Constants.ERR_CODE_INVALID_PC;
			break;
		}

		return status;

	}

	private static String getCCBSSbuTypeInPcSbuPt(Integer sbu, Integer productType, String traceId) {
		String status;
		switch (sbu) {
		case Constants.SBU_ALL:
			LOGGER.info("SBU_ALL :traceId={}", traceId);
			switch (productType) {
			case Constants.PT_OTHER:
				status = Constants.ERR_CODE_VALID;
				break;

			case Constants.PT_WIFI:
				status = Constants.ERR_CODE_VALID;
				break;

			default:

				status = Constants.ERR_CODE_INVALID_PT;
				break;
			}

			break;

		case Constants.SBU_DTV_PRE:// 1

			LOGGER.info("SBU_DTV_PRE : traceId={}", traceId);
			if (productType == Constants.PT_OTHER) {
				status = Constants.ERR_CODE_VALID;
			} else {

				status = Constants.ERR_CODE_INVALID_PT;
			}
			break;

		case Constants.SBU_DTV_POST:

			LOGGER.info("SBU_DTV_POST :traceId={}", traceId);

			if (productType == Constants.PT_OTHER) {
				status = Constants.ERR_CODE_VALID;
			} else {
				status = Constants.ERR_CODE_INVALID_PT;
			}
			break;

		case Constants.SBU_GSM:

			LOGGER.info("SBU_GSM : traceId={}", traceId);

			switch (productType) {
			case Constants.PT_OTHER:
				status = Constants.ERR_CODE_VALID;
				break;

			case Constants.PT_WIFI:
				status = Constants.ERR_CODE_VALID;
				break;

			default:

				status = Constants.ERR_CODE_INVALID_PT;
				break;
			}
			break;

		default:

			LOGGER.info("Invalid SBU traceId={}", traceId);
			status = Constants.ERR_CODE_INVALID_SBU;
			break;
		}
		return status;
	}

	private static String getTelbizSbuTypeInPcSbuPt(Integer sbu, Integer productType, String traceId) {
		String status;
        if (sbu == Constants.SBU_DBN) {
			LOGGER.info("SBU_DBN traceId=[{}]", traceId);
			LOGGER.info("Product Type=[{}] | traceId=[{}]", productType, traceId);

            switch (productType) {
                case Constants.PT_OTHER:

                case Constants.PT_WIFI:

                case Constants.PT_CDMA:

                case Constants.PT_LTE:

                case Constants.PT_VOLTE:

                case Constants.PT_DCS:
                    status = Constants.ERR_CODE_VALID;
                    break;

                default:
                    status = Constants.ERR_CODE_INVALID_PT;
                    break;
            }
        } else {
            LOGGER.info("Invalid SBU traceId=[{}]", traceId);
            status = Constants.ERR_CODE_INVALID_SBU;
        }
		return status;
	}

}
