package lk.dialog.pe.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DBNPaymentDto {
	
	private Long reqId;	
	private String accountNo;	
	private String contractNo;	
	private String connRef;
	private String accountType;
	private Double paymentAmount;
	private Long paymentMethodId;
	private String paymentMode;
	private String paymentRef;
	private String chqBank;
	private String chqNo;
	private String chqBranch;
	private Date chqDate;
	private String chqSuspend;
	private String cardType;
	private String cardAgent;
	private String cardNo;
	private String cardAprvCode;
	private String receiptNo;
	private String receiptBranch;
	private String branchCounter;
	private String receiptUser;
	private Date receiptDate;
	private Integer transferredType;
	private String transferredMode ;
	private String transferredNo;
	private String remarks;
	private String contactNo;
	private String transferReasonCode;
	private Date physicalPaymentDate;
	private Integer productType;
	@JsonProperty("terminalID")
	private String terminalId;
	private String moduleName;
	private String invoiceList;
	private String connectionStatus;
}
