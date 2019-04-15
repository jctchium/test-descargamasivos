package com.sto.pac.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import org.apache.commons.ssl.PKCS8Key;
//import org.apache.commons.ssl.PKCS8Key;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.sto.pac.exceptions.SelloException;

public class SignUtil {
	public static String generaSello(String file, String password, String cadenaOriginal) throws SelloException{
		Security.addProvider(new BouncyCastleProvider());
		File f = new File(file);
		byte [] clavePrivada = null;
		try {
			clavePrivada = Files.readAllBytes(f.toPath());
		}
		catch(IOException e) {
			throw new SelloException("Error IO: " + e.getMessage());
		}
		
		PKCS8Key pkcs8key = null;
		try {
			pkcs8key = new PKCS8Key(clavePrivada, password.toCharArray());
		}
		catch(GeneralSecurityException e) {
			throw new SelloException("Error GeneralSecurity: " + e.getMessage());
		}
		
		PrivateKey privateKey = pkcs8key.getPrivateKey();
		Signature firma = null;
		
		try {
			firma = Signature.getInstance("SHA1WithRSA");
		}
		catch(NoSuchAlgorithmException e) {
			throw new SelloException("Error NoSuchAlgorithm: " + e.getMessage());
		}
		
		try {
			firma.initSign(privateKey);
		}
		catch(InvalidKeyException e) {
			throw new SelloException("Error InvalidKey: " + e.getMessage());
		}
		
		String selloDigital = null;
		try {
			firma.update(cadenaOriginal.getBytes("UTF-8"));
		}
		catch(SignatureException e) {
			throw new SelloException("Error Signature: " + e.getMessage());
		}
		catch(UnsupportedEncodingException e) {
			throw new SelloException("Error UnsupportedEncoding: " + e.getMessage());
		}
		
		try {
			selloDigital = Base64.getEncoder().encodeToString(firma.sign());
		}
		catch(SignatureException e) {
			throw new SelloException("Error Signature: " + e.getMessage());
		}
		
		selloDigital = selloDigital.replaceAll("\n", "");
		selloDigital = selloDigital.replaceAll("\r", "");
		
		return selloDigital;
	}
	
	public static String generaSello(byte [] llave, String password, String cadenaOriginal) throws SelloException{
		System.out.println("Longitud Llave Privada Base: " + llave.length);
		System.out.println("LlavePrivada: [" + new String(llave) + "]");
		
		Security.addProvider(new BouncyCastleProvider());
		PKCS8Key pkcs8key = null;
		try {
			pkcs8key = new PKCS8Key(llave, password.toCharArray());
		}
		catch(GeneralSecurityException e) {
			throw new SelloException("Error GeneralSecurity: " + e.getMessage());
		}
		
		PrivateKey privateKey = pkcs8key.getPrivateKey();
		Signature firma = null;
		
		try {
			firma = Signature.getInstance("SHA1WithRSA");
		}
		catch(NoSuchAlgorithmException e) {
			throw new SelloException("Error NoSuchAlgorithm: " + e.getMessage());
		}
		
		try {
			firma.initSign(privateKey);
		}
		catch(InvalidKeyException e) {
			throw new SelloException("Error InvalidKey: " + e.getMessage());
		}
		
		String selloDigital = null;
		try {
			firma.update(cadenaOriginal.getBytes("UTF-8"));
		}
		catch(SignatureException e) {
			throw new SelloException("Error Signature: " + e.getMessage());
		}
		catch(UnsupportedEncodingException e) {
			throw new SelloException("Error UnsupportedEncoding: " + e.getMessage());
		}
		
		try {
			selloDigital = Base64.getEncoder().encodeToString(firma.sign());
		}
		catch(SignatureException e) {
			throw new SelloException("Error Signature: " + e.getMessage());
		}
		
		selloDigital = selloDigital.replaceAll("\n", "");
		selloDigital = selloDigital.replaceAll("\r", "");
		
		return selloDigital;
	}
	
	/*public static String generaSello(Certificado certificado, String cadenaOriginal) throws SelloException{
		PrivateKey privateKey = certificado.getPrivateKey();
		Signature firma = null;
		
		try {
			firma = Signature.getInstance("SHA1WithRSA");
		}
		catch(NoSuchAlgorithmException e) {
			throw new SelloException("Error NoSuchAlgorithm: " + e.getMessage());
		}
		
		try {
			firma.initSign(privateKey);
		}
		catch(InvalidKeyException e) {
			throw new SelloException("Error InvalidKey: " + e.getMessage());
		}
		
		String selloDigital = null;
		try {
			firma.update(cadenaOriginal.getBytes("UTF-8"));
		}
		catch(SignatureException e) {
			throw new SelloException("Error Signature: " + e.getMessage());
		}
		catch(UnsupportedEncodingException e) {
			throw new SelloException("Error UnsupportedEncoding: " + e.getMessage());
		}
		
		try {
			selloDigital = Base64.getEncoder().encodeToString(firma.sign());
		}
		catch(SignatureException e) {
			throw new SelloException("Error Signature: " + e.getMessage());
		}
		
		selloDigital = selloDigital.replaceAll("\n", "");
		selloDigital = selloDigital.replaceAll("\r", "");
		
		return selloDigital;
	}*/
}