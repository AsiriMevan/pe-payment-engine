package lk.dialog.pe.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "subscriber" , schema = "CCBS")
public class Subscriber {
	@Id
	@Column(name = "SUBSCRIBER_SEQNO")
	private Long subscriberSeqNumber;
	
	@Column(name = "ACCOUNT_NUM")
	private String accountNumber;
	
	@Column(name="SUB_NUMBER")
	private String subNumber;

	@Column(name = "MAIN_NUMBER")
	private String mainNumber;

}