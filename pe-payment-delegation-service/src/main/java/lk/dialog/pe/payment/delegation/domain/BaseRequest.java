package lk.dialog.pe.payment.delegation.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BaseRequest {

	private Integer productCategory = null; 
	
	private Long trxID;
	
	public Integer getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(Integer productCategory) {
		this.productCategory = productCategory;
	}
	public Long getTrxID() {
		return trxID;
	}
	public void setTrxID(Long trxID) {
		this.trxID = trxID;
	}
}
