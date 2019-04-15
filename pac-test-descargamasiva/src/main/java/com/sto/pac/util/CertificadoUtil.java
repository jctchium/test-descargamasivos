package com.sto.pac.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CertificadoUtil {
	public static String parseCertificado(String fileName) throws IOException{
		File f = new File(fileName);
		long length = f.length();
		FileInputStream fis = null;
		byte [] bytes = new byte[(int)length];
		try {
			fis = new FileInputStream(f);
			fis.read(bytes, 0, (int)length);
			fis.close();
		}
		catch(IOException e) {
			throw e;
		}
		String res = new String(bytes);
		res = res.replaceAll("\n", "");
		res = res.replaceAll("\r", "");
		res = res.replaceAll("-----BEGIN CERTIFICATE-----", "");
		res = res.replaceAll("-----END CERTIFICATE-----", "");
		
		return res;
	}
}
