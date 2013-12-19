package com.adi3000.common.web.jsf;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.BreakIterator;

public final class UtilsBean {
	/**
	 * Return an encoded String for url from value
	 * @param value string to encode
	 * @return encoded string
	 */
	public static String urlEncode(String value){
		if(value == null) return value;
		try {
			return URLEncoder.encode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String urlDecode(String value, boolean escapePlus){
		if(escapePlus){
			return urlDecode(value.replace("+", "%2B"));
		}else{
			return urlDecode(value);
		}
	}
	public static String urlDecode(String value){
		if(value == null) return value;
		try {
			return URLDecoder.decode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String limit(String value, Integer length){
		if(value == null || value.length() <= length) return value;
		String s = value;
		int number_chars = length;
		BreakIterator bi = BreakIterator.getWordInstance();
		bi.setText(s);
		int first_after = bi.following(number_chars);
		// to truncate:
		s = s.substring(0, first_after).concat("...");
		return s;
	}
	
}
