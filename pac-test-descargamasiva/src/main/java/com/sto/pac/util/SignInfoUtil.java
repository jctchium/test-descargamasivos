package com.sto.pac.util;

public class SignInfoUtil {
	private final static String TEMPLATE = "<SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"#_0\"><Transforms><Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><DigestValue>TkB6rHvN5ZQNM8QDbcaOA1Jj32o=</DigestValue></Reference></SignedInfo>";
	private final static int endBlock1 = 351;
	private final static int initBlock02 = 379;
	
	public static String getSignInfo(String digest) {
		String templateTemp = TEMPLATE;
		int length = templateTemp.length();
		templateTemp = templateTemp.substring(0, endBlock1) + digest + templateTemp.substring(initBlock02, length);		
		return templateTemp;
	}
}
