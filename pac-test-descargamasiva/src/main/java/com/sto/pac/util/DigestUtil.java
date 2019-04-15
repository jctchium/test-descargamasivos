package com.sto.pac.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.sto.pac.exceptions.SelloException;

public class DigestUtil {
	public static String digest(String cadena) throws SelloException{
		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA1");
		}
		catch(NoSuchAlgorithmException e) {
			throw new SelloException("Error No SuchAlgorithm: " + e.getMessage());
		}
				
		byte[] digest = sha1.digest((cadena).getBytes());
		return Base64.getEncoder().encodeToString(digest);
	}
}