package lk.dialog.pe.ccbs.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryPaymentRequest {
	
		private Integer productCategory;
		private Date receiptFromDate;		
		private Date receiptToDate;
		private String receiptBranch;
		private String branchCounter;
		private String contractNo;
		private String chequebankCode;
		private String chequebankBranchCode;
		private String chequeNo;
		private String receiptUser;
		private List<Receipt> receipts;
}
