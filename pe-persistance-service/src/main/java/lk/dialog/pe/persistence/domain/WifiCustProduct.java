package lk.dialog.pe.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "WIFI_CUST_PRODUCT" , schema = "WIFI")
public class WifiCustProduct {

	@Id
	@Column(name = "CUST_PRODUCT_ID")
	private Integer custProductId;

	@Column(name = "CUST_IDENTIFIER")
	private String mobile;
	
	@Column(name = "ACCOUNT_ID")
	private Long contractId;
}
