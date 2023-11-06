package lk.dialog.pe.persistence.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="PAYMODE_MAP")
public class PaymodeMap {
	
	@EmbeddedId 
	private PaymodeMapID paymodeMapId;
	
	@Column(name="IS_DEFAULT")
	private String isDefault;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="UPDATE_DATE")
	private Date updateDate;
	
	@Column(name="UPDATED_USER")
	private String updateUser;
	
	@Column(name="RBM_PAYMENT_METHOD_ID")
	private int rbmPaymentMethodId;

}