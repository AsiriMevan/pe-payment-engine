package lk.dialog.pe.common.util;

import java.util.Date;
import java.util.List;

import lk.dialog.pe.common.domain.BaseRequest;
import lombok.Data;
@Data
public class ChequeRealizeRequest extends BaseRequest {

	private String chqNo;
	private String chqBank;
	private String chqBranch;
	private String posCreatedDate;
	private List<Receipt> receiptsInfo;
	private Date physicalPaymentDate;
	private Integer connectionStatus;

}
