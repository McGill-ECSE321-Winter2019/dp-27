package ca.mcgill.cooperator.service;

import java.util.ArrayList;
import java.util.List;

public class ServiceUtils {
	
	/**
	 * Converts an Iterable to a List
	 * 
	 * @param <T>
	 * @param iterable
	 * @return Iterable in List format
	 */
	public static <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
