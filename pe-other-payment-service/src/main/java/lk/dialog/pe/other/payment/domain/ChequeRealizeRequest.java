package lk.dialog.pe.other.payment.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;
@Data
public class ChequeRealizeRequest extends BaseRequest {
	
	private String chqNo;
	private String chqBank;
	private String chqBranch;
	private Date posCreatedDate;
	private List<Receipt> receiptsInfo;
	private Date physicalPaymentDate;
	private Integer connectionStatus;
		
}