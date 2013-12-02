package com.adi3000.common.util.security;

import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.adi3000.common.util.optimizer.CommonValues;





/**
 * Utilities of static methods to provide encrypt and decrypt procedures.
 * @author adi
 *
 */
public class Security {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Security.class);
	
	public static final int ERROR = CommonValues.ERROR_OR_INFINITE;  
	
	private static final String DIGEST_ALGORITHM = "SHA-1";
	
	private static Set<Integer> ANONYMOUS_SESSION_ID = new TreeSet<Integer>();

	public static int generateSessionID(int clientHashCode,SocketAddress client) {
		LOGGER.info("Anonymous UUID Session asked from " + client.toString());
		UUID uuid = UUID.randomUUID(); 
		int uuidHash = uuid.hashCode();
		LOGGER.trace("UUID computed : " + uuid);
		if (ANONYMOUS_SESSION_ID.add(uuidHash) )
		{
			return uuidHash;
		}
		return ERROR;
	}
	public static int generateSessionID(int clientHashCode,User user) {
		LOGGER.info("User UUID Session asked from " + user.toString());
		return generateSessionID(clientHashCode);
	}
	private static int generateSessionID(int clientHashCode) {
		UUID uuid = UUID.randomUUID(); 
		int uuidHash = uuid.hashCode();
		LOGGER.trace("UUID computed : " + uuid);
		if (ANONYMOUS_SESSION_ID.add(uuidHash) )
		{
			return uuidHash;
		}
		return ERROR;
	}
	
	public static String generateTokenID(User user) {
		UUID uuid = UUID.randomUUID(); 
		String token = digestString(user.getLogin().concat(digestString(uuid.toString())));
		LOGGER.trace("Token generated computed : " + token);
		return token;
	}
	
	public static boolean isUserLogged(User user){
		return user != null && user.getId() != null;
	}
	
	public static void checkUserLogged(User user){
		if(!isUserLogged(user)){
			throw new IllegalStateException("User is not logged in ! ");
		}
	}
	
	public static boolean checkLoginState(User user){
		return user != null && user.getLoginState() != null && user.getLoginState() > 0;
	}
	
	public static String encryptPassword(String password, String salt){
		return password;
	}

	private static String digestString(String phrase){
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.error("Can't find "+DIGEST_ALGORITHM+" algorithm for ".concat(phrase), e1);
			return null;
		}
		String digestedPhrase = Base64.encodeBase64String(messageDigest.digest(Base64.decodeBase64(phrase)));
		return digestedPhrase;
	}
}
