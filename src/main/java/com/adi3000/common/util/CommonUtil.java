package com.adi3000.common.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adi3000.common.database.hibernate.data.DataObject;
import com.adi3000.common.util.optimizer.CommonValues;

public final class CommonUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class.getName());
	private CommonUtil(){}
	
	/**
	 * Parse string to {@link List} of {@link String} using the {@link CommonValues}{@code .SEPARATOR}
	 * @param stringList 
	 * @return {@link List} of all string after cut off {@link CommonValues}{@code .SEPARATOR}
	 */
	public static List<String> parseStringToList(String stringList){
		return parseStringToList(stringList, CommonValues.SEPARATOR);
	}
	/**
	 * Parse string to {@link List} of {@link String} using the {@code separator}
	 * @param stringList 
	 * @return {@link List} of all string after cut off the {@code separator}
	 */
	public static List<String> parseStringToList(String stringList, String separator){
		if(StringUtils.isEmpty(stringList)){
			return null;
		}else{
			return Arrays.asList(stringList.split(separator));
		}
	}
	/**
	 * Parse string to {@link List} of {@link Integer} using the {@code separator}
	 * @param stringList 
	 * @return {@link List} of all string after cut off the {@code separator}
	 */
	public static List<Integer> parseStringToIntegerList(String stringList){
		return parseStringToIntegerList(stringList, CommonValues.SEPARATOR);
	}
	/**
	 * Parse string to {@link List} of {@link Integer} using the {@link CommonValues}{@code .SEPARATOR}
	 * @param stringList 
	 * @return {@link List} of all string after cut off the {@link CommonValues}{@code .SEPARATOR}
	 */
	public static List<Integer> parseStringToIntegerList(String stringList, String separator){
		List<String> listString = parseStringToList(stringList, separator);
		List<Integer> listInt = new ArrayList<Integer>(listString.size());
		try{
			for(String s : listString){
				listInt.add(Integer.valueOf(s));
			}
		}catch(NumberFormatException e){
			LOGGER.warn("One of the input cannot be converted" + stringList,e );
			listInt = null;
		}
		return listInt;
	}
	
	/**
	 * Format {@link List} to a string using the {@code separator}
	 * @param list to format
	 * @return {@link String} of all object of list separated by {@code separator}
	 */
	public static String formatListToString(Collection<?> list){
		return formatListToString(list, CommonValues.SEPARATOR);
	}
	/**
	 * Format {@link List} to a string using the {@code separator}
	 * @param list to format
	 * @return {@link String} of all object of list separated by {@code separator}
	 */
	public static String formatListToString(Collection<?> list, String separator){
		if(list == null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean firstLoop = true;
		for(Object obj : list ){
			if(!firstLoop){
				sb.append(separator);
			}
			sb.append(obj.toString());
			firstLoop = false;
		}
		return sb.toString();
	}

	/**
	 * Return a value {@link CommonValues}{@code .TRUE} or {@link CommonValues}{@code .FALSE} 
	 * assiociated to {@code b} 
	 * @param b
	 * @return
	 */
	public static Character toChar(Boolean b){
		return b != null && b ? CommonValues.TRUE : CommonValues.FALSE; 
	}
	/**
	 * @param b
	 * @return true if {@code b} is equals to {@link CommonValues}{@code .TRUE} or false
	 */
	public static Boolean isTrue(CharSequence b){
		return Boolean.valueOf(b != null && CommonValues.TRUE.toString().equals(b.toString())); 
	}
	/**
	 * @param b
	 * @return true if {@code b} is equals to {@link CommonValues}{@code .TRUE} or false
	 */
	public static Boolean isTrue(Character b){
		return Boolean.valueOf(CommonValues.TRUE.equals(b)) ; 
	}
	/**
	 * Get timestamp for Now
	 * @return timestamp
	 */
	public static Timestamp getTimestamp(){
		return new Timestamp(new Date().getTime());
	}
	
	/**
	 * Find {@link DataObject} within a {@link Collection} with the id {@code id}
	 * @param list
	 * @param id
	 * @return
	 */
	public static DataObject findById(Collection<? extends DataObject> list,Serializable id){
		if(id == null){
			return null;
		}
		for(DataObject o : list){
			if(o != null && o.getId() != null){
				if(id.equals(o.getId()) || id.toString().equals(o.getId().toString())){
					return o;
				}
			}
		}
		return null;
	}
	public static Criteria setCriteriaPage(Criteria req, int page, int nbResultToLoad){
		int cursor = CommonValues.ERROR_OR_INFINITE;
		cursor = nbResultToLoad * (page -1);
		req.setFirstResult(cursor).setMaxResults(nbResultToLoad);
		return req;
	}
	public static Query setCriteriaPage(Query req, int page, int nbResultToLoad){
		int cursor = CommonValues.ERROR_OR_INFINITE;
		cursor = nbResultToLoad * (page -1);
		req.setFirstResult(cursor).setMaxResults(nbResultToLoad);
		return req;
	}
	public static String parsePlainTextToHtml(String text){
		if(text != null){
			return StringEscapeUtils.escapeXml(text).replace("\n", "<br />");
		}
		return null;
	}
}