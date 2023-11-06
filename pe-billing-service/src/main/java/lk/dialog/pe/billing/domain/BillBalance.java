package lk.dialog.pe.billing.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class BillBalance extends Balance {

	private double interimUsage;
	private double totalOS;
	private double reconFee;
	private double minAmtToConnect;
}
