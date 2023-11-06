package lk.dialog.pe.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PaymodeMapID  implements Serializable {
	
		private static final long serialVersionUID = 1L;

		@Column(name = "CPOS_ID")
	    String cposId;
		
	    @Column(name="RBM_ID")
	    String rbmId;
	    
	    @Column(name="TBIZ_ID")
	    int tbizId;

		public PaymodeMapID(String cposId, String rbmId, int tbizId) {
			this.cposId = cposId;
			this.rbmId = rbmId;
			this.tbizId = tbizId;
		}
	
	    
}
