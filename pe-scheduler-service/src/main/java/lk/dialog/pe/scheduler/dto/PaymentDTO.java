package lk.dialog.pe.scheduler.dto;

import lk.dialog.pe.scheduler.domain.Payment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Calendar;

@Data
public class PaymentDTO {
	private long trxID;
	private int accountPaymentStatus;
	private Calendar createdDtm;
	private String accountPaymentText;
	private String accountPaymentRef;
	private long accountPaymentSeq;
	private Calendar cancelledDtm;
	private String cancelledByUser;
	private String accountNo;
	private String contractNo;
	private double accountPaymentMny;
	private Calendar accountPaymentDate;
	private String receiptNo;
	private String receiptBranch;
	private String branchCounter;
	private String receiptUser;
	private Calendar receiptDate;
	private String paymentMode;
	private String paymentRefNo;
	private String remarks;
	private String statusReason;
	private String paymentReversalType;
	private String paymentCancelledReason;
	private int accountType;
	private int sbu;
	private String transferredNo;
	private String transferredMode;
	private double transferredAmount;
	private String chequebankCode;
	private String chequebankBranchCode;
	private String chequeNo;
	private Calendar chequeDate;
	private String chequeSuspend;
	private String ccType;
	private String ccBankCode;
	private String ccNo;
	private String ccApprovalCode;
	private int productType;
	private String paymentCurrency;
	private int paymentMethodID;
	private String terminalID;
	private long physicalPaymentSeq;
	private String connRef;
	private String transferredTo;

	public PaymentDTO(Payment paymentSource){
		BeanUtils.copyProperties(paymentSource, this);
	}

    public PaymentDTO() {

    }
}
