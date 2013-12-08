package com.adi3000.common.util.security;

import java.sql.Timestamp;

public interface User {
	/** Not logged in **/
	public static final Integer ANONYMOUS = -1;
	/** Login is not allowed. Security log must be written **/
	public static final Integer BLOCKED = 0;
	/** User are only allowed to post draft **/
	public static final Integer NOT_VALIDATED = 1;
	/** Above this value user are allowed to post things **/
	public static final Integer NEW_USER_VALIDATED = 2;
	/** Tips are disabled **/
	public static final Integer VALIDATED = 3;
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
	 * Set token to a specific value, must be used by Security service only
	 * @param uselessValue
	 * @deprecated use setToken() to generate token
	 */
	@Deprecated
	public void setToken(String token) ;
	/**
	 * Get the generated token
	 * @return the token
	 */
	public String getToken() ;
	/**
	 * @return the password
	 */
	public String getPassword() ;
	public void setPassword(String password) ;
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
	/**
	 * @return Login stat value 
	 */
	public Integer getLoginState();
	/**
	 * @return salt String
	 */
	public String getSalt();
	/**
	 * @return salt String
	 */
	public void setSalt(String salt);
	
}
