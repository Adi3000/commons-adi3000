package com.adi3000.common.util.security;

import java.sql.Timestamp;

public interface User {

	/**
	 * id of the user
	 * @return
	 */
	public Integer getId();
	/**
	 * @return the login
	 */
	public String getLogin() ;
	/**
	 * @return the token
	 */
	public Integer getToken() ;
		/**
	 * @return the password
	 */
	public String getPassword() ;
	/**
	 * @return the lastHostNameLogin
	 */
	public String getLastHostNameLogin();
	/**
	 * @return the mail
	 */
	public String getMail();
	/**
	 * @return the lastDateLogin
	 */
	public Timestamp getLastDateLogin();
	/**
	 * @return the lastIpLogin
	 */
	public String getLastIpLogin();
	
}
