package com.exp.rest.ws.util;

import java.security.*;

public class Sha256 {
    
	public static String hash256(String data) {
        MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }
    
    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
