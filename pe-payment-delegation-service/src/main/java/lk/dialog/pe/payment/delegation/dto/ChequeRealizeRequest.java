package lk.dialog.pe.payment.delegation.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;
@Data
public class ChequeRealizeRequest extends BaseRequest {
	
	private String chqNo;
	private String chqBank;
	private String chqBranch;
	private Date posCreatedDate;
	private List<Receipt> receiptsInfo;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = Shape.NUMBER_INT)
	private Date physicalPaymentDate;
	private Integer connectionStatus;
	
}
