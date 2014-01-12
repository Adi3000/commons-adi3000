package com.adi3000.common.web.faces;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adi3000.common.web.jsf.UtilsBean;

public final class FacesUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacesUtil.class.getName());

	private static String FACES_REDIRECT_PARAMETER = "faces-redirect=true";
	private static String REQUEST_SETTING_COOKIE = "adi3000_cookies";
	private static String FACES_INCLUDE_VIEW_PARAMS_PARAMETER = "includeViewParams=true";
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
	 * Redirect to {@code path} with changing URL
	 * @param path
	 */
	public static void navigationRedirect(String path){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException e) {
			LOGGER.error("Can't redirect to "+ path, e);
		}
	}
	/**
	 * Redirect to {@code path} without changing URL
	 * @param path
	 */
	public static void navigationForward(String path){
		ConfigurableNavigationHandler nav = 
				(ConfigurableNavigationHandler) 
				FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		nav.performNavigation(path);
	}
	
	
	/**
	 * Forward to {@code path} and update the URL
	 * @param path
	 */
	public static String prepareRedirect(String path){
		return prepareRedirect(path, false);
	}
	/**
	 * Forward to {@code path} and update the URL
	 * @param path
	 */
	public static String prepareRedirect(String path, boolean includeViewParams){
		if(!path.contains(FACES_REDIRECT_PARAMETER)){
			path = addParameterToRequest(path,FACES_REDIRECT_PARAMETER);
		}
		if(!path.contains(FACES_INCLUDE_VIEW_PARAMS_PARAMETER)){
			path = addParameterToRequest(path,FACES_INCLUDE_VIEW_PARAMS_PARAMETER);
		}
		return path;
	}
	
	
	private static String addParameterToRequest(String path, String parameter){
		if(!path.contains("?")){
			path = path.concat("?");
		}else{
			path = path.concat("&");
		}
		path = path.concat(parameter);
		return path;
	}
	
	private static Map<String, Object> convertCookieToMap(Cookie cookie){
		Map<String, Object> cookieMap = new HashMap<>();
		if(StringUtils.isNotEmpty(cookie.getDomain())){
			cookieMap.put("comment", cookie.getComment());
		}
		if(StringUtils.isNotEmpty(cookie.getDomain())){
			cookieMap.put("domain", cookie.getDomain());
		}
		cookieMap.put("maxAge", Integer.valueOf(cookie.getMaxAge()));
		cookieMap.put("secure", Boolean.valueOf(cookie.getSecure()));
		if(StringUtils.isNotEmpty(cookie.getPath())){
			cookieMap.put("path", cookie.getPath());
		}
		return cookieMap;
	}
	
	private static void setCookieInCurrentRequest(Cookie cookie){
		@SuppressWarnings("unchecked")
		Map<String, Cookie> cookieMap = (Map<String, Cookie>) 
			((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest())
			.getAttribute(REQUEST_SETTING_COOKIE);
		if(cookieMap == null){
			cookieMap = new HashMap<>();
		}
		cookieMap.put(cookie.getName(), cookie);
		((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest())
		.setAttribute(REQUEST_SETTING_COOKIE, cookieMap);
	}
	private static Cookie getCookieFromRequest(String name){
		@SuppressWarnings("unchecked")
		Map<String, Cookie> cookieMap = (Map<String, Cookie>) 
			((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest())
			.getAttribute(REQUEST_SETTING_COOKIE);
		if(cookieMap != null){
			return cookieMap.get(name);
		}
		return null;
				
	}
	
	
	
	public static void setCookie(String name, String value, int maxAge, boolean secure){
		Cookie cookie = new Cookie(name, UtilsBean.urlEncode(value));
		cookie.setPath("/");
		cookie.setMaxAge(Integer.valueOf(maxAge));
		cookie.setSecure(Boolean.valueOf(secure));
		FacesContext.getCurrentInstance()
		 .getExternalContext().addResponseCookie(name, value, convertCookieToMap(cookie));
		setCookieInCurrentRequest(cookie);
	}
	public static Object getCookie(String name){
		Object cookie =  FacesContext.getCurrentInstance()
							.getExternalContext()
							.getRequestCookieMap().get(name);
		if(cookie == null){
			cookie = getCookieFromRequest(name);
		}
		return cookie;
	}
}
