package lk.dialog.pe.scheduler.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayMode implements Serializable {
	private String cposId;
	private String rbmId;
	private int tbizId;

}
