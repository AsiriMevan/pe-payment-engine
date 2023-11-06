package lk.dialog.pe.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "CAM_CONTRACT" , schema = "CCBS")
public class CamContract {
	
	@Id
	@Column(name = "CONTRACT_ID")
	private Long contractId;
	
	@Column(name = "CONTRACT_SUBSIDIARY_TYPE")
	private String contractSubsidiaryType;
	
}