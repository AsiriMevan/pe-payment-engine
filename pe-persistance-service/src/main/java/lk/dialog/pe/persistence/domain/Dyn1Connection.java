package lk.dialog.pe.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "DYN_1_CONNECTION" , schema = "CCBS2")
public class Dyn1Connection {

	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "SIM")
	private String sim;
	
	@Column(name = "IMSI")
	private String imsi;
	
	@Column(name = "IMEI")
	private String imei;
	
	@Column(name = "SERVICE_ID")
	private Long serviceId;
	
	@Column(name = "MOBILE_NO")
	private String mobileNo;
	
	@Id
	@Column(name = "CONNECTION_ID")
	private Long connectionId;
	
	@Column(name = "PRE_POST")
	private String prePost;
	
	@Column(name = "FORMAT_ID")
	private Long formatId;
	
	@Column(name = "UPDATED_USER")
	private String updatedUser;
	
	@Column(name = "FORCE_RATING")
	private String forceRating;
	
	@Column(name = "BILL_RUN_CODE")
	private String billRunCode;
	
	@Column(name = "PART_KEY")
	private String partKey;
	
	@Column(name = "CREATE_FULFILLMENT_ORDER_ID")
	private Long createFulfillmentOrderId;
	
	@Column(name = "MODIFY_FULFILLMENT_ORDER_ID")
	private Long modifyFulfillmentOrderId;

}
