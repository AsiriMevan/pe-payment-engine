package lk.dialog.pe.ccbs.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "CAM_CONTRACT_PACKAGE" , schema = "CCBS2")
public class CamContractPackage {

	@Id
	@Column(name = "CONTRACT_PACKAGE_ID")
	private Long contractPackageId;
	
	@Column(name = "CONTRACT_ID")
	private Long contractNo;
}
