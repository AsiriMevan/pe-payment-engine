package lk.dialog.pe.common.util;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lk.dialog.pe.common.domain.BaseRequest;
import lombok.Data;
@Data
public class PaymentPostRequest extends BaseRequest {

	private Long reqId;
	private String accountNo;
	private String contractNo;
	private String connRef;
	private Integer sbu = null;
	private Integer accountType = null;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = Shape.NUMBER_INT)
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = Shape.NUMBER_INT)
	private Date receiptDate;
	private String paymentMode;
	private String paymentText;
	private String terminalID;
	private String chqNo;
	private String chqBank;
	private String chqBranch;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = Shape.NUMBER_INT)
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
	private String realTime;
	private Integer connectionStatus;

}
