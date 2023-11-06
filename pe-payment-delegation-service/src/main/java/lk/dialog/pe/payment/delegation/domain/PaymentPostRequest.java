package lk.dialog.pe.payment.delegation.domain;

import java.util.Date;

import lk.dialog.pe.payment.delegation.dto.BaseRequest;
import lombok.Data;
@Data
public class PaymentPostRequest extends BaseRequest {

	private String accountNo;
	private String contractNo;
	private String connRef;
	private Integer sbu = null;
	private Integer accountType = null;
	private Date physicalPaymentDate;
	private Double paymentAmount = null;
	private String paymentCurrency;
	private Integer paymentMethodId = null;
	private String physicalPaymentText;
	private String paymentRef;
	private String receiptNo;
	private String receiptBranch;
	private String branchCounter;
	private String receiptUser;
	private Date receiptDate;
	private String paymentMode;
	private String paymentText;
	private String terminalID;
	private String chqNo;
	private String chqBank;
	private String chqBranch;
	private Date chqDate;
	private String chqSuspend;
	private String cardType;
	private String cardAgent;
	private String cardNo;
	private String cardAprvCode;
	private String moduleName;
	private Integer transferredType = null;
	private String transferredNo;
	private String transferredMode;
	private String remarks;
	private String transferReasonCode;
	private String contactNo;
	private Integer productType;
	private String invoiceList;
	private Integer connectionStatus;
	
}

