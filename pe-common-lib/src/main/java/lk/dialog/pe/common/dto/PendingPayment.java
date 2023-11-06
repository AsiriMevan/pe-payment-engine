package lk.dialog.pe.common.dto;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class PendingPayment {

	private String connRef;
	private String contractNo;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Calendar cancelledDtm;
	private String paymentCancelledReason;
	private String paymentCancelledUser;
	private String accountNo;
	private Double accountPaymentMny;
	private String receiptNo;
	private String receiptBranch;
	private String branchCounter;
	private String receiptUser;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private Calendar receiptDate;
	private String paymentMode;
	private String paymentRefNo;
	private Integer accountType;
	private Integer sbu;
	private Integer productType;
	private String transferErrDesc;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Calendar lastTransferRecordedTime;

}
