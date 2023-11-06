package lk.dialog.pe.ccbs.dto;

import java.util.List;

import lombok.Data;
@Data
public class CCBSProfileRequest {

	private List<String> mobileList;
	private List<String> contractList;
	private String requestUserId;
	private String remoteIP;
	private String sourceSystem;
	
}
