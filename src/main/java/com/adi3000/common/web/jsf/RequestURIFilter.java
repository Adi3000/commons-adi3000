package com.adi3000.common.web.jsf;

import java.util.Map;

public interface RequestURIFilter {
	/**
	 * Return an URI Request parameter representation of the filter
	 * @return
	 */
	public String getRequestURI();
	
	/**
	 * Set filter from a Request URI parameter map without authorization on Status filter
	 * @param parameterMap
	 */
	public void setFilters(Map<String,String> parameterMap);
}
