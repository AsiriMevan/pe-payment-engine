/**
 *
 */
package lk.dialog.pe.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentJSON {
	@JsonIgnore
	private String reqId;

	@JsonIgnore
	private String productCategory;

	private String receiptNumber;

	private String accountNumber;

	private String accountType;

	private Double amount;

	private String nomineeNumber;

	private String paymentType;

	private String txType;

	private String txDate;

	private String receiptBranch;

	private String branchCounter;

	private String paymentRef;

	private String connRef;

	private String physicalPaymentText;

	private String receiptUser;

	private String paymentText;

	private String moduleName;

	private String commandRead;

	private String errorCode;

	private String errorDesc;

	private Long productType;

	private String connectionStatus;

	private String connectionType;

	private String receiptDate;

	private String physicalPaymentDate;

	private String statusReason;

	private String createdUser;

}
