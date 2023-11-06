package lk.dialog.pe.customer.info.domain;

import java.util.List;

import lombok.Data;

@Data
public class CRMProfileRequest extends BaseRequest{

	private String custRef;
	private String oldCustRef;
	private String custRefType;
	private Integer sbu;
	private String billInvoiceNo;
	private Integer reqType;
	private List<ConnectionRef> accounts;
	private String requestUserId;
	private String remoteIP;
	private String sourceSystem;
}
