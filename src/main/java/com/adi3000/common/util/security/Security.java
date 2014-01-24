package com.adi3000.common.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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
	private static final String PASSWORD_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The following constants may be changed without breaking existing hashes.
    public static final int SALT_BYTE_SIZE = 24;
    public static final int HASH_BYTE_SIZE = 24;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;

    /**
     * Generate a random token
     * @param user
     * @return
     */
    public static String generateTokenID() {
    	return generateTokenID(null);
    }
    /**
     * Generate a random token
     * @param user
     * @return
     */
	public static String generateTokenID(User user) {
		SecureRandom random = new SecureRandom();
        byte[] hash = new byte[HASH_BYTE_SIZE];
        random.nextBytes(hash);
		String token = digest(hash);
		LOGGER.trace("Token generated computed : " + token);
		return token;
	}
	
	/**
	 * Return if user is considered as logged
	 * @param user
	 * @return
	 */
	public static boolean isUserLogged(User user){
		return user != null && user.getId() != null;
	}
	
	/**
	 * Throw an error if user is not logged
	 * @param user
	 */
	public static void checkUserLogged(User user){
		if(!isUserLogged(user)){
			throw new IllegalStateException("User is not logged in ! ");
		}
	}
	
	/**
	 * Check if login state is valid (loginStat > 0)
	 * @param user
	 * @return
	 */
	public static boolean checkLoginState(User user){
		return user != null && user.getLoginState() != null && user.getLoginState() > 0;
	}
	
	public static void encryptPassword(User user) {
		try {
			createHash(user);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Invalid configuration for password hashing",e);
			throw new IllegalArgumentException("Invalid configuration for password hashing",e);
		}
	}

	private static String digest(byte[] phrase){
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.error("Can't find "+DIGEST_ALGORITHM+" algorithm for digest", e1);
			throw new IllegalArgumentException("Can't find "+DIGEST_ALGORITHM+" algorithm for digest", e1);
		}
		String digestedPhrase = Base64.encodeBase64URLSafeString(messageDigest.digest(Base64.decodeBase64(phrase)));
		return digestedPhrase;
	}
	
	

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param   password    the password to hash
     * @return              user with setted salt and password a salted PBKDF2 hash of the password
     */
    private static User createHash(User user)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return createHash(user.getPassword().toCharArray(), user);
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param   password    the password to hash
     * @return              a salted PBKDF2 hash of the password
     */
    private static User createHash(char[] password, User user)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);

        // Hash the password
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        // format iterations:salt:hash
        user.setSalt(PBKDF2_ITERATIONS + ":" +toBase64(salt)); 
        user.setPassword(toBase64(hash));
        return user;
    }

    /**
     * Validates a password using a hash.
     *
     * @param   password        the password to check
     * @param   correctHash     the hash of the valid password
     * @return                  true if the password is correct, false if not
     */
    public static boolean validatePassword(String password, User user)
    {
        try {
			return validatePassword(password.toCharArray(), user);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Invalid configuration for password validation",e);
			throw new IllegalArgumentException("Invalid configuration for password validation",e);
		}
    }

    /**
     * Validates a password using a hash.
     *
     * @param   password        the password to check
     * @param   correctHash     the hash of the valid password
     * @return                  true if the password is correct, false if not
     */
    public static boolean validatePassword(char[] password, User user)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Decode the hash into its parameters
        String[] params = user.getSalt().split(":");
        int iterations = Integer.parseInt(params[ITERATION_INDEX]);
        byte[] salt = fromBase64(params[SALT_INDEX]);
        byte[] hash = fromBase64(user.getPassword());
        // Compute the hash of the provided password, using the same salt, 
        // iteration count, and hash length
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, testHash);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line 
     * system using a timing attack and then attacked off-line.
     * 
     * @param   a       the first byte array
     * @param   b       the second byte array 
     * @return          true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++){
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /**
     *  Computes the PBKDF2 hash of a password.
     *
     * @param   password    the password to hash.
     * @param   salt        the salt
     * @param   iterations  the iteration count (slowness factor)
     * @param   bytes       the length of the hash to compute in bytes
     * @return              the PBDKF2 hash of the password
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PASSWORD_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param   hex         the hex string
     * @return              the hex string decoded into a byte array
     */
    private static byte[] fromBase64(String hex)
    {
    	return Base64.decodeBase64(hex);
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param   array       the byte array to convert
     * @return              a length*2 character string encoding the byte array
     */
    private static String toBase64(byte[] array)
    {
    	return Base64.encodeBase64URLSafeString(array);
    }
}
