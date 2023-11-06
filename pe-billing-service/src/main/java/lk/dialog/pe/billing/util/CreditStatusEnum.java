package lk.dialog.pe.billing.util;

public enum CreditStatusEnum {

	B(2),
	C(1);
	
	private int value;
	private CreditStatusEnum(int val){
		this.value = val;
	}
	public int valueOf()
	{
	    return this.value;
	}
	
	public static CreditStatusEnum getCreditStatus(int value){
		CreditStatusEnum rt = null;
	    for(CreditStatusEnum e : CreditStatusEnum.values()){
	        if(e.valueOf() == value){
	        	rt = e;
	            break;
	        }
	    }
	    return rt;
	}
	public static CreditStatusEnum getCreditStatus(String value){
		CreditStatusEnum rt = null;
		for(CreditStatusEnum e : CreditStatusEnum.values()){
			if(e.name().equalsIgnoreCase(value)){
				rt = e;
	            break;
			}
		}
		return rt;
	}
	
}
