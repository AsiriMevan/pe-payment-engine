package lk.dialog.pe.ccbs.util;

public enum HybridStatus {

	Y(1),
	N(2);
	
	private int value;
	private HybridStatus(int val){
		this.value = val;
	}
	public int valueOf()
	{
	    return this.value;
	}
	
	public static HybridStatus getHybridStatus(int value){
		HybridStatus rt = null;
	    for(HybridStatus e : HybridStatus.values()){
	        if(e.valueOf() == value){
	        	rt = e;
	            break;
	        }
	    }
	    return rt;
	}
	public static HybridStatus getHybridStatus(String value){
		HybridStatus rt = null;
		for(HybridStatus e : HybridStatus.values()){
			if(e.name().equalsIgnoreCase(value)){
				rt = e;
	            break;
			}
		}
		return rt;
	}
}
