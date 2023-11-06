package lk.dialog.pe.common.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class ForcefulRealizeChequesDto {

	private Long reqId;	
	private String accountNo;
	private String contractNo;	
	private String productCategory;	
	private String sbu;	
	private String accountType;
	private Double paymentAmount;	
	private String chqBank;	
	private String chqBranch;	
	private String chqNo;	
	private Date chqDate;
	@JsonProperty("terminalID")
	private String terminalId;	
	private String receiptNo;
	private String receiptBranch;
	private String branchCounter;
	private Date receiptDate;
	private String receiptUser;
	private Date physicalPaymentDate;
	private Long productType;
	private String realTime;
	private String connRef;
	private String paymentRef;
	private String remarks;
	private String connectionStatus;
	private String paymentMode;
	private String cardNo;
}
