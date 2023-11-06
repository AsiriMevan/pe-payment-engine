package lk.dialog.pe.scheduler.dto;

import lombok.Data;

@Data
public class TelbizMinPayResponse {

	private double totalOs;
	private double totalBill;
	private double minPayToConnect;
	private String status;
}
