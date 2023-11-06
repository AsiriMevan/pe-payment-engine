package lk.dialog.pe.ccbs.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "DBT_CONTRACTBILLSUMMARY_V")
public class DBTContractBillSummaryV {

	@Id
	private Long id;
	
	@Column(name = "ACCOUNT_NUM")
	private String accountNo;
	
	@Column(name = "INVOICE_NUM")
	private String billInvoiceNo;
	
}
