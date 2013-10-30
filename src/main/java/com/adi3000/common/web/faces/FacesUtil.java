package com.adi3000.common.web.faces;

import java.util.Map;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;

public final class FacesUtil {
	private static String FACES_REDIRECT_PARAMETER = "faces-redirect=true";
	private FacesUtil(){};
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
	/**
	 * Redirect to {@code path} without changing URL
	 * @param path
	 */
	public static void navigationForward(String path){
		ConfigurableNavigationHandler nav 
		   = (ConfigurableNavigationHandler) 
				   FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		nav.performNavigation(path);
	}
	/**
	 * Forward to {@code path} and update the URL
	 * @param path
	 */
	public static String prepareRedirect(String path){
		if(!path.contains(FACES_REDIRECT_PARAMETER)){
			if(!path.contains("?")){
				path = path.concat("?");
			}else{
				path = path.concat("&");
			}
			path = path.concat(FACES_REDIRECT_PARAMETER);
				
		}
		return path;
	}
	
}
