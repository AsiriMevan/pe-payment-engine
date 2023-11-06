package lk.dialog.pe.payment.delegation.util;

import lk.dialog.pe.common.util.CancelPaymentRequest;
import lk.dialog.pe.common.util.PaymentDelegationEnum;
import lk.dialog.pe.common.util.PaymentPostRequest;

import java.util.Date;

public class APIvalidator {

	private APIvalidator() {
	}

	public static String validateIP510(PaymentPostRequest jsonInput) {

		String ip510Status = "VALID";
		if ("".equalsIgnoreCase(jsonInput.getPaymentMode())) {
			return "INVALID PaymentMode";
		}
//todo - duplicate conditions
		if (!"".equalsIgnoreCase(jsonInput.getPaymentMode())) {

			String validPayModeMapStatus = validPayModeMap(jsonInput);
			//todo - why not validPayModeMapStatus here
			if (validPayModeMap(jsonInput) != null) {
				return validPayModeMapStatus;
			}
		}

		if (!"".equalsIgnoreCase(jsonInput.getPaymentMode()))

		{
			String validpayModeCardStatus = validpayModeCard(jsonInput);
			//todo - why not validpayModeCardStatus here and method name should be validPayModeCard
			if (validpayModeCard(jsonInput) != null) {
				return validpayModeCardStatus;
			}
		}

		if (jsonInput.getTransferredType() != null && 1 == jsonInput.getTransferredType()) {
			String transferredNo = jsonInput.getTransferredNo();
			String transferredMode = jsonInput.getTransferredMode();

			if (transferredNo == null || transferredNo.isEmpty()) {
				return "INVALID TransferredNo";
			}
			if (transferredMode == null || transferredMode.isEmpty()) {
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

			if (transferredMode == null || transferredMode.isEmpty()) {
				return "INVALID TransferredMode";
			}
			if (transferredRef == null || transferredRef.isEmpty()) {
				return "INVALID TransferredRef";
			}

		}

		return ip511Status;
	}

//todo - no trace id
	private static String validpayModeCard(PaymentPostRequest jsonInput) {
		String cardType = jsonInput.getCardType();
		String cardAgent = jsonInput.getCardAgent();
		String cardNo = jsonInput.getCardNo();

		if (PaymentDelegationEnum.PAY_MODE_CARD.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (cardType == null || cardType.isEmpty()))
			return "INVALID CardType";
		if (PaymentDelegationEnum.PAY_MODE_CARD.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (cardAgent == null || cardAgent.isEmpty()))
			return "INVALID CardAgent";
		if (PaymentDelegationEnum.PAY_MODE_CARD.getName().equalsIgnoreCase(jsonInput.getPaymentMode()) && (cardNo == null || cardNo.isEmpty()))
			return "INVALID CardNo";
		return null;
	}
//todo - no trace id
	private static String validPayModeMap(PaymentPostRequest jsonInput) {

		String chqBank = jsonInput.getChqBank();
		String chqBranch = jsonInput.getChqBranch();
		String chqNo = jsonInput.getChqNo();
		Date chqDate = jsonInput.getChqDate();

		String chqSuspend = jsonInput.getChqSuspend();

		if(PaymentDelegationEnum.PAY_MODE_CHQ.getName().equalsIgnoreCase(jsonInput.getPaymentMode())) {
			return validPayModeChq(chqBank, chqBranch, chqNo, chqDate, chqSuspend);
		}
		return null;
	}
//todo - no trace id
	private static String validPayModeChq (String chqBank,
										   String chqBranch,
										   String chqNo,
										   Date chqDate,
										   String chqSuspend) {
		if (chqBank == null || chqBank.isEmpty())
			return "INVALID ChqBank";
		if (chqBranch == null || chqBranch.isEmpty())
			return "INVALID ChqBranch";
		if (chqNo == null || chqNo.isEmpty())
			return "INVALID ChqNo";
		if (chqDate == null)
			return "INVALID ChqDate";
		if (chqSuspend == null || chqSuspend.isEmpty())
			return "INVALID ChqSuspend";
		return null;
	}


}
