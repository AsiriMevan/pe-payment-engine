package lk.dialog.pe.customer.info.util;
import java.util.List;

public class AndCriteria implements FilterCriteria  {

	private FilterCriteria first;
	private FilterCriteria second;
	
	public AndCriteria(FilterCriteria first,FilterCriteria second){
		this.first = first;
		this.second = second ;
	}
	
	@Override
	public <E> List<E> execute(List<E> list) {
		return second.execute(first.execute(list));
	}

}
