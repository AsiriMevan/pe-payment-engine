package lk.dialog.pe.common.dto;

import lk.dialog.pe.common.domain.CRMProfileRequest;
import lombok.Data;

@Data
public class ValidatorResponse {

	private String status;
	private String crmSystem;
	private CRMProfileRequest crmProfileRequest;
}
