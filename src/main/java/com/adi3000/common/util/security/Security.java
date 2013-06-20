package com.adi3000.common.util.security;

import java.net.SocketAddress;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Logger;


import com.adi3000.common.util.optimizer.CommonValues;





/**
 * Utilities of static methods to provide encrypt and decrypt procedures.
 * @author adi
 *
 */
public class Security {
	
	private static final Logger LOGGER = Logger.getLogger(Security.class.getName());
	
	public static final int ERROR = CommonValues.ERROR_OR_INFINITE;  
	
	private static Set<Integer> ANONYMOUS_SESSION_ID = new TreeSet<Integer>();

	public static int generateSessionID(int clientHashCode,SocketAddress client) {
		LOGGER.info("Anonymous UUID Session asked from " + client.toString());
		UUID uuid = UUID.randomUUID(); 
		int uuidHash = uuid.hashCode();
		LOGGER.finer("UUID computed : " + uuid);
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
		LOGGER.finer("UUID computed : " + uuid);
		if (ANONYMOUS_SESSION_ID.add(uuidHash) )
		{
			return uuidHash;
		}
		return ERROR;
	}
	
	public static boolean isUserLogged(User user){
		return user != null && user.getId() != null;
	}
	
	public static void checkUserLogged(User user){
		if(!isUserLogged(user)){
			throw new IllegalArgumentException("User is not logged in ! ");
		}
	}

}