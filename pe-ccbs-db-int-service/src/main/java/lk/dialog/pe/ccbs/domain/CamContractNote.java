package lk.dialog.pe.ccbs.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "CAM_CONTRACT_NOTE",schema = "CCBS")
public class CamContractNote {
	
	@Id
	@Column(name = "CONTRACT_ID")
	private Long contractNo;
	
	@Column(name = "NOTE")
	private String remark;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_USER")
	private String createdUser;
	
}
