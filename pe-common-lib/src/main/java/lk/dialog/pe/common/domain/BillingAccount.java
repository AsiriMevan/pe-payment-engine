package lk.dialog.pe.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties({"accountNo","accountType","contractEmail","prCode","prEmail","hybridFlag","conStatus","disconReasonCode","disconReason","productType","billCycle","sbu","contactNo"})
public class BillingAccount extends Account {

	private Double lastBill;	
	private Double totalOust;
	
}
