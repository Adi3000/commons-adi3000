package com.adi3000.common.web.jsf;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class UtilsBean {
	/**
	 * Return an encoded String for url from value
	 * @param value string to encode
	 * @return encoded string
	 */
	public static String urlEncode(String value){
		try {
			return URLEncoder.encode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String urlDecode(String value){
		try {
			return URLDecoder.decode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}