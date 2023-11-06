package lk.dialog.pe.cheque.payment.domain;

public enum ProductCategoryEnum {

	CCBS(1),
	TELBIZ(2),
	NFC(3),
	PC_PE(4);
	
	private int value;
	private ProductCategoryEnum(int val){
		this.value = val;
	}
	public int valueOf()
	{
	    return this.value;
	}
	
	public static ProductCategoryEnum getProductCategory(int value){
		ProductCategoryEnum rt = null;
	    for(ProductCategoryEnum e : ProductCategoryEnum.values()){
	        if(e.valueOf() == value){
	        	rt = e;
	            break;
	        }
	    }
	    return rt;
	}
	public static ProductCategoryEnum getProductCategory(String value){
		ProductCategoryEnum rt = null;
		for(ProductCategoryEnum e : ProductCategoryEnum.values()){
			if(e.name().equalsIgnoreCase(value)){
				rt = e;
	            break;
			}
		}
		return rt;
	}
}
