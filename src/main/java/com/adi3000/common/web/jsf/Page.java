package com.adi3000.common.web.jsf;

import org.apache.commons.lang.StringUtils;

import com.adi3000.common.util.optimizer.CommonValues;

public class Page<F extends RequestURIFilter> {

	private final int index;
	private final F filter;
	public static final String PAGE_PARAM="p"; 
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @return the itemFilter
	 */
	public F getFilter() {
		return filter;
	}
	public Page(int index, F filter){
		this.filter = filter;
		this.index = index;
	}
	public String getRequestURI(){
		String filterRequestURI = filter.getRequestURI();
		if(StringUtils.isNotEmpty(filterRequestURI)){
			filterRequestURI= filterRequestURI.concat(CommonValues.URI_PARAMETER_SEPARATOR);
		}
		return filterRequestURI
				.concat(PAGE_PARAM)
				.concat(CommonValues.URI_VALUE_SEPARATOR)
				.concat(String.valueOf(index));
	}
}
