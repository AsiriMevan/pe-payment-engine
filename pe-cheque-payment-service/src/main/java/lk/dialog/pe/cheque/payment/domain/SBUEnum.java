package lk.dialog.pe.cheque.payment.domain;

public enum SBUEnum {

	SBU_ALL(0), 
	SBU_DTV_PRE(1),
	SBU_DTV_POST(2),
	SBU_GSM(3), 
	SBU_DBN(4), 

	REQ_TYPE_ALL(1), 
	REQ_TYPE_LIMITED(0);

	public final int sbu;

	private SBUEnum(int sbu) {
		this.sbu = sbu;
	}

	public int getSbu() {
		return sbu;
	}
}
