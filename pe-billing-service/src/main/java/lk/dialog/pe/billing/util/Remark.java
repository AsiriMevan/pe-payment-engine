package lk.dialog.pe.billing.util;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
@Data
@JsonPropertyOrder({"remarks","createdUser","createdDate"})
public class Remark {
	
	@JsonProperty("remark")
	private String remarks;
	private String createdUser;	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
	private Calendar createdDate;
	
}
