package lk.dialog.pe.persistence.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "WIFI_ACCOUNT" , schema = "WIFI")
public class WifiAccount {

	@Id
	@Column(name = "ACCOUNT_ID")
	private Long accountId;
	
	@Column(name = "BILL_RUN_CODE")
	private String billRunCode;
	
	@Column(name = "BILLING_EMAIL")
	private Long billingEmail;
	
	@Column(name = "STATUS")
	private String status;
	private Long contractNo;
	
	@Column(name = "REMARKS")
	private String remark;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_USER")
	private String createdUser;

}
