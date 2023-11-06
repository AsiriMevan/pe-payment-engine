package lk.dialog.pe.ccbs.util;

public enum ProductCategory {
	CCBS(1),
	TELBIZ(2),
	NFC(3);
	
	private int value;
	private ProductCategory(int val){
		this.value = val;
	}
	public int valueOf()
	{
	    return this.value;
	}
	
	public static ProductCategory getProductCategory(int value){
		ProductCategory rt = null;
	    for(ProductCategory e : ProductCategory.values()){
	        if(e.valueOf() == value){
	        	rt = e;
	            break;
	        }
	    }
	    return rt;
	}
	public static ProductCategory getProductCategory(String value){
		ProductCategory rt = null;
		for(ProductCategory e : ProductCategory.values()){
			if(e.name().equalsIgnoreCase(value)){
				rt = e;
	            break;
			}
		}
		return rt;
	}
}
