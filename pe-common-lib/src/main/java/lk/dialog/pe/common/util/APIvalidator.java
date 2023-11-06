package lk.dialog.pe.common.util;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.dialog.pe.common.domain.CRMProfileRequest;
import lk.dialog.pe.common.domain.ConnectionRef;
import lk.dialog.pe.common.dto.ValidatorResponse;

public class APIvalidator {

	private APIvalidator() {
	}

	public static String validateIP510(PaymentPostRequest jsonInput) {

		String ip510Status = "VALID";
		if ("".equalsIgnoreCase(jsonInput.getPaymentMode())) {
			return "INVALID PaymentMode";
		}

		if (!jsonInput.getPaymentMode().isEmpty()) {

			String validPayModeMapStatus = validPayModeMap(jsonInput);
			if (validPayModeMap(jsonInput) != null) {
				return validPayModeMapStatus;
			}
		}

		if (!jsonInput.getPaymentMode().isEmpty())

		{
			String validpayModeCardStatus = validpayModeCard(jsonInput);
			if (validpayModeCard(jsonInput) != null) {
				return validpayModeCardStatus;
			}
		}

		if (jsonInput.getTransferredType() != null && 1 == jsonInput.getTransferredType()) {
			String transferredNo = jsonInput.getTransferredNo();
			String transferredMode = jsonInput.getTransferredMode();

			if (transferredNo == null || "".equalsIgnoreCase(transferredNo)) {
				return "INVALID TransferredNo";
			}
			if (transferredMode == null || "".equalsIgnoreCase(transferredMode)) {
				return "INVALID TransferredMode";
			}

		}

		return ip510Status;
	}

	public static String validateIP511(CancelPaymentRequest jsonInput) {

		String ip511Status = "VALID";

		if (jsonInput.getTransferredType() != null && 1 == jsonInput.getTransferredType()) {
			String transferredMode = jsonInput.getTransferredMode();
			String transferredRef = jsonInput.getTransferredRef();

			if (transferredMode == null || "".equalsIgnoreCase(transferredMode)) {
				return "INVALID TransferredMode";
			}
			if (transferredRef == null || "".equalsIgnoreCase(transferredRef)) {
				return "INVALID TransferredRef";
			}

		}

		return ip511Status;
	}

	private static String validpayModeCard(PaymentPostRequest jsonInput) {
		String cardType = jsonInput.getCardType();
		String cardAgent = jsonInput.getCardAgent();
		String cardNo = jsonInput.getCardNo();

		if (PaymentDelegationEnum.PAY_MODE_CARD.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (cardType == null || "".equalsIgnoreCase(cardType)))
			return "INVALID CardType";
		if (PaymentDelegationEnum.PAY_MODE_CARD.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (cardAgent == null || "".equalsIgnoreCase(cardAgent)))
			return "INVALID CardAgent";
		if (PaymentDelegationEnum.PAY_MODE_CARD.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (cardNo == null || "".equalsIgnoreCase(cardNo)))
			return "INVALID CardNo";
		return null;
	}

	private static String validPayModeMap(PaymentPostRequest jsonInput) {

		String chqBank = jsonInput.getChqBank();
		String chqBranch = jsonInput.getChqBranch();
		String chqNo = jsonInput.getChqNo();
		Date chqDate = jsonInput.getChqDate();
		
		String chqSuspend = jsonInput.getChqSuspend();
		if (PaymentDelegationEnum.PAY_MODE_CHQ.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (chqBank == null || "".equalsIgnoreCase(chqBank)))
			return "INVALID ChqBank";
		if (PaymentDelegationEnum.PAY_MODE_CHQ.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (chqBranch == null || "".equalsIgnoreCase(chqBranch)))
			return "INVALID ChqBranch";
		if (PaymentDelegationEnum.PAY_MODE_CHQ.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (chqNo == null || "".equalsIgnoreCase(chqNo)))
			return "INVALID ChqNo";
		if (PaymentDelegationEnum.PAY_MODE_CHQ.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (chqDate == null))
			return "INVALID ChqDate";
		if (PaymentDelegationEnum.PAY_MODE_CHQ.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (chqSuspend == null || "".equalsIgnoreCase(chqSuspend)))
			return "INVALID ChqSuspend";
		return null;
	}
	
	public static ValidatorResponse validateIP500TelbizConRef(CRMProfileRequest jsonInput) {

		ValidatorResponse validatorResponse = new ValidatorResponse();

		switch (jsonInput.getProductCategory()) {
		case Constants.PC_CCBS:

			switch (jsonInput.getSbu()) {
			case Constants.SBU_ALL:
			case Constants.SBU_DTV_PRE:
			case Constants.SBU_DTV_POST:
			case Constants.SBU_GSM:

				validatorResponse.setStatus(Constants.ERR_CODE_VALID);
				validatorResponse.setCrmSystem(ProductCategoryEnum.CCBS.toString());
				break;

			default:
				validatorResponse.setStatus(Constants.ERR_CODE_INVALID_SBU);
				validatorResponse.setCrmSystem(ProductCategoryEnum.CCBS.toString());
				break;
			}

			break;
		case Constants.PC_TELBIZ:
			switch (jsonInput.getSbu()) {
			case Constants.SBU_ALL:
			case Constants.SBU_DBN:

				List<ConnectionRef> telbizConRefList = jsonInput.getAccounts();
				List<ConnectionRef> newTelbizConRefList = new ArrayList<>();
				String custRef = jsonInput.getCustRef();
				String billInvoice = jsonInput.getBillInvoiceNo();
				String oldCustRef = jsonInput.getOldCustRef();
				if ((oldCustRef == null || "".equalsIgnoreCase(oldCustRef))
						&& (custRef == null || "".equalsIgnoreCase(custRef))
						&& (billInvoice == null || "".equalsIgnoreCase(billInvoice))) {
					for (ConnectionRef connectionRef : telbizConRefList) {

						String connRef = connectionRef.getConnRef();
						if (connRef != null && !"".equalsIgnoreCase(connRef)) {
							if (connRef.matches(Constants.TELBIZ_CONN_REF_VALIDATOR)) {

								validatorResponse.setStatus(Constants.ERR_CODE_VALID);
								validatorResponse.setCrmSystem(ProductCategoryEnum.TELBIZ.toString());
								connectionRef.setContractNo(connectionRef.getContractNo());
								connectionRef.setConnRef("0" + connectionRef.getConnRef());
								newTelbizConRefList.add(connectionRef);

							} else {
								validatorResponse.setStatus(Constants.ERR_CODE_VALID);
								validatorResponse.setCrmSystem(ProductCategoryEnum.TELBIZ.toString());
								newTelbizConRefList.add(connectionRef);
							}
						} else {
							if (!"".equalsIgnoreCase(connectionRef.getContractNo())) {
								validatorResponse.setStatus(Constants.ERR_CODE_VALID);
								validatorResponse.setCrmSystem(ProductCategoryEnum.TELBIZ.toString());
								newTelbizConRefList.add(connectionRef);
							} else {
								validatorResponse.setStatus(Constants.ERR_CONN_REF);
								validatorResponse.setCrmSystem(ProductCategoryEnum.TELBIZ.toString());
							}
						}
					}
					jsonInput.setAccounts(newTelbizConRefList);
					validatorResponse.setCrmProfileRequest(jsonInput);
				} else {
					validatorResponse.setStatus(Constants.ERR_CODE_VALID);
					validatorResponse.setCrmSystem(ProductCategoryEnum.TELBIZ.toString());
					jsonInput.setAccounts(telbizConRefList);
					validatorResponse.setCrmProfileRequest(jsonInput);
				}
				break;

			default:
				validatorResponse.setStatus(Constants.ERR_CODE_INVALID_SBU);
				validatorResponse.setCrmSystem(ProductCategoryEnum.TELBIZ.toString());
				break;
			}
			break;
		case Constants.PC_NFC:
			switch (jsonInput.getSbu()) {
			case Constants.SBU_ALL:
			case Constants.SBU_GSM:

				validatorResponse.setStatus(Constants.ERR_CODE_VALID);
				validatorResponse.setCrmSystem(ProductCategoryEnum.NFC.toString());
				break;

			default:
				validatorResponse.setStatus(Constants.ERR_CODE_INVALID_SBU);
				validatorResponse.setCrmSystem(ProductCategoryEnum.NFC.toString());
				break;
			}
			break;
		default:

			validatorResponse.setStatus(Constants.ERR_CODE_INVALID_PC);
			break;
		}

		return validatorResponse;
	}

	
	
}
