package lk.dialog.pe.payment.delegation.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Receipt {

	private String receiptNo;
	private String connRef;
	private String receiptBranch;
	private String branchCounter;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = Shape.NUMBER_INT)
	private Date receiptDate;
	private String receiptUser;
	private String accountNo;
	private String contractNo;
	private Double paymentAmount = null;
	private Integer sbu;
	private Integer productType;
	
}
