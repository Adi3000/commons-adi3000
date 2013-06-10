package com.adi3000.common.web.faces;

import java.util.Map;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;

public class FacesUtils {
	/**
	 * Return the string value of the parameter {@link name}
	 * @param name
	 */
	public static String getStringParameter(String name){
		FacesContext context = FacesContext.getCurrentInstance(); 
		Map<String, String> map = context.getExternalContext().getRequestParameterMap(); 
		return  (String) map.get(name);
	}
	/**
	 * Return the string value of the parameter {@link name}
	 * @param name
	 */
	public static Integer getIntParameter(String name){
		FacesContext context = FacesContext.getCurrentInstance(); 
		Map<String, String> map = context.getExternalContext().getRequestParameterMap(); 
		Integer value = null;
		try{
			Integer.valueOf((String) map.get(name));
		}catch(NumberFormatException e){
			//TODO log part
		}
		
		return value;
	}
	public static void navigationRedirect(String path){
		ConfigurableNavigationHandler nav 
		   = (ConfigurableNavigationHandler) 
				   FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		nav.performNavigation(path);
	}
}
