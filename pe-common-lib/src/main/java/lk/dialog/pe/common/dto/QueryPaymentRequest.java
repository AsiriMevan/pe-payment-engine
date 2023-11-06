package lk.dialog.pe.common.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lk.dialog.pe.common.util.Receipt;
import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryPaymentRequest {

	private Integer productCategory;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = Shape.NUMBER_INT)
	private Date receiptFromDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = Shape.NUMBER_INT)
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
