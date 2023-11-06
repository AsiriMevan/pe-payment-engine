package lk.dialog.pe.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CRMSystemValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(CRMSystemValidator.class);

	private CRMSystemValidator() {
	}

	public static String isValidSystemAgainstPcSbu(Integer productCategory, Integer sbu) {

		String status;

		switch (productCategory) {

		case Constants.PC_CCBS:
			status = getCCBSSbuTypeInPcSbu(sbu);

			break;
		case Constants.PC_TELBIZ:

			status = getTELBIZSbuTypeInPcSbu(sbu);
			break;
		case Constants.PC_NFC:

			status = getNFCSbuTypeInPcSbu(sbu);
			break;
		default:
			LOGGER.info("Invalid PC");
			status = Constants.ERR_CODE_INVALID_PC;
			break;
		}

		return status;

	}

	private static String getNFCSbuTypeInPcSbu(Integer sbu) {
		String status;
		if (sbu == Constants.SBU_GSM) {
			LOGGER.info("getNFCSbuTypeInPcSbu :SBU_GSM");
			status = Constants.ERR_CODE_VALID;

		}else {
			LOGGER.info("getNFCSbuTypeInPcSbu : Invalid SBU");
			status = Constants.ERR_CODE_INVALID_SBU;
		}
		return status;
	}

	private static String getTELBIZSbuTypeInPcSbu(Integer sbu) {
		String status;
		if (sbu == Constants.SBU_DBN) {
			LOGGER.info("SBU_DBN");
			status = Constants.ERR_CODE_VALID;

		}else {
			LOGGER.info("getTELBIZSbuTypeInPcSbu : Invalid SBU");
			status = Constants.ERR_CODE_INVALID_SBU;
		}
		return status;
	}

	private static String getCCBSSbuTypeInPcSbu(Integer sbu) {
		String status;
		switch (sbu) {
		case Constants.SBU_ALL:
			LOGGER.info("getCCBSSbuTypeInPcSbu : SBU_ALL");
			status = Constants.ERR_CODE_VALID;
			break;
		case Constants.SBU_DTV_PRE:
			LOGGER.info("getCCBSSbuTypeInPcSbu : SBU_DTV_PRE");
			status = Constants.ERR_CODE_VALID;
			break;
		case Constants.SBU_DTV_POST:
			LOGGER.info("getCCBSSbuTypeInPcSbu : SBU_DTV_POST");
			status = Constants.ERR_CODE_VALID;
			break;
		case Constants.SBU_GSM:
			LOGGER.info("getCCBSSbuTypeInPcSbu :SBU_GSM");
			status = Constants.ERR_CODE_VALID;
			break;

		default:
			LOGGER.info("getCCBSSbuTypeInPcSbu :Invalid SBU");
			status = Constants.ERR_CODE_INVALID_SBU;
			break;
		}
		return status;
	}

	public static String isValidSystemAgainstPcSbuPt(Integer productCategory, Integer sbu, Integer productType) {

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
		switch (productCategory) {
		case Constants.PC_CCBS:
			status = getCCBSSbuTypeInPcSbuPt(sbu, productType);

			LOGGER.info("PC_CCBS");

			break;

		case Constants.PC_TELBIZ:

			if (sbu == Constants.SBU_DBN) {
				LOGGER.info("isValidSystemAgainstPcSbuPt : SBU_DBN");
				status = getTELBIZSbuTypeInPcSbuPt(productType);
			} else {
				LOGGER.info("isValidSystemAgainstPcSbuPt : Invalid SBU");
				status = Constants.ERR_CODE_INVALID_SBU;
			}
			break;

		case Constants.PC_NFC:
			if (sbu == Constants.SBU_GSM) {

				LOGGER.info("SBU_GSM");

				if (productType == Constants.PT_NFC) {
					status = Constants.ERR_CODE_VALID;

				} else {
					status = Constants.ERR_CODE_INVALID_PT;
				}

			} else {

				LOGGER.info("Invalid SBU");
				status = Constants.ERR_CODE_INVALID_SBU;
			}
			break;

		default:

			LOGGER.info("Invalid PC");
			status = Constants.ERR_CODE_INVALID_PC;
			break;
		}

		return status;

	}

	private static String getTELBIZSbuTypeInPcSbuPt(Integer productType) {
		String status;
		switch (productType) {
		case Constants.PT_OTHER:
			status = Constants.ERR_CODE_VALID;
			break;

		case Constants.PT_WIFI:
			status = Constants.ERR_CODE_VALID;
			break;

		case Constants.PT_CDMA:
			status = Constants.ERR_CODE_VALID;
			break;

		case Constants.PT_LTE:
			status = Constants.ERR_CODE_VALID;
			break;

		case Constants.PT_VOLTE:
			status = Constants.ERR_CODE_VALID;
			break;

		case Constants.PT_DCS:
			status = Constants.ERR_CODE_VALID;
			break;

		default:
			status = Constants.ERR_CODE_INVALID_PT;
			break;
		}
		return status;
	}

	private static String getCCBSSbuTypeInPcSbuPt(Integer sbu, Integer productType) {
		String status;
		switch (sbu) {
		case Constants.SBU_ALL:
			LOGGER.info("SBU_ALL");
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

		case Constants.SBU_DTV_PRE:

			LOGGER.info("SBU_DTV_PRE");
			if (productType == Constants.PT_OTHER) {
				status = Constants.ERR_CODE_VALID;
			} else {

				status = Constants.ERR_CODE_INVALID_PT;
			}
			break;

		case Constants.SBU_DTV_POST:

			LOGGER.info("SBU_DTV_POST");

			if (productType == Constants.PT_OTHER) {
				status = Constants.ERR_CODE_VALID;
			} else {
				status = Constants.ERR_CODE_INVALID_PT;
			}
			break;

		case Constants.SBU_GSM:

			LOGGER.info("SBU_GSM");

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

			LOGGER.info("Invalid SBU");
			status = Constants.ERR_CODE_INVALID_SBU;
			break;
		}
		return status;
	}

}
