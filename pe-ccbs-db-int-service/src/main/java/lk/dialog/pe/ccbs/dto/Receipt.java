package lk.dialog.pe.ccbs.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Receipt {
	
	private String receiptNo;
	private String connRef;
	private String receiptBranch;
	private String branchCounter;
	private Date receiptDate;
	private String receiptUser;
	private String accountNo;
	private String contractNo;
	private Double paymentAmount = null;
	private Integer sbu;
	private Integer productType;
}
