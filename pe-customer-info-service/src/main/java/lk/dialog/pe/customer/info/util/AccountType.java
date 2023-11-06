package lk.dialog.pe.customer.info.util;

public enum AccountType {

	POSTPAID(2),
	PREPAID(1);
	private int value;
	private AccountType(int val){
		this.value = val;
	}
	public int valueOf()
	{
	    return this.value;
	}
	
	public static AccountType getAccountType(int value){
		AccountType rt = null;
	    for(AccountType e : AccountType.values()){
	        if(e.valueOf() == value){
	        	rt = e;
	            break;
	        }
	    }
	    return rt;
	}
	public static AccountType getAccountType(String value){
		AccountType rt = null;
		for(AccountType e : AccountType.values()){
			if(e.name().equalsIgnoreCase(value)){
				rt = e;
	            break;
			}
		}
		return rt;
	}

}
