package lk.dialog.pe.customer.info.util;

import java.util.List;

public interface FilterCriteria {

	/**
	 * @param <T>
	 * @param list : list of instances that needs to filter
	 * @return filtered T list
	 * List<T>
	 */
	public <E> List<E> execute(List<E> list);

}
