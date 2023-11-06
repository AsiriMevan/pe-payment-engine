package lk.dialog.pe.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "DUAL")
public class Dual {

	@Id
	@Column(name = "DUMMY")
	private String dummy;
	

}
