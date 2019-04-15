package com.sto.pac.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.sto.pac.exceptions.SelloException;

public class BuilderUtil {
	private final static String TEMPLATE = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><s:Header><o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"><u:Timestamp u:Id=\"_0\"><u:Created>2019-02-08T06:38:08.000Z</u:Created><u:Expires>2019-02-08T06:43:08.000Z</u:Expires></u:Timestamp><o:BinarySecurityToken u:Id=\"uuid-b246ed31-bfec-804a-5212-095ac6d97d3c-1\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">MIIFljCCA36gAwIBAgIUMjAwMDEwMDAwMDAzMDAwMDM3MDIwDQYJKoZIhvcNAQELBQAwggFmMSAwHgYDVQQDDBdBLkMuIDIgZGUgcHJ1ZWJhcyg0MDk2KTEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMRUwEwYDVQQtEwxTQVQ5NzA3MDFOTjMxITAfBgkqhkiG9w0BCQIMElJlc3BvbnNhYmxlOiBBQ0RNQTAeFw0xNDA2MDYxNDU5MDZaFw0xODA2MDYxNDU5MDZaMIIBBTE3MDUGA1UEAxMuRElTVFJJQlVJRE9SQSBERSBDQVJORVMgU0FOVEEgTUFSQ0VMQSBTQSBERSBDVjE3MDUGA1UEKRMuRElTVFJJQlVJRE9SQSBERSBDQVJORVMgU0FOVEEgTUFSQ0VMQSBTQSBERSBDVjE3MDUGA1UEChMuRElTVFJJQlVJRE9SQSBERSBDQVJORVMgU0FOVEEgTUFSQ0VMQSBTQSBERSBDVjElMCMGA1UELRMcRENTMDAxMjIxMTY0IC8gRlVBQjc3MDExN0JYQTEeMBwGA1UEBRMVIC8gRlVBQjc3MDExN01ERlJOTjA5MREwDwYDVQQLFAhQcnVlYmFfMTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAhVicJ41LJkM/sROtrirXc5z1H10UTNYubjBg3SCAVtWNRA150FX7V1wYx1y+7oUW8ZxR8MZuysP2RyUHRy64j0A0gqvwCJIkJMIqcl9DTSoxDJtiMkudypSiJ3Gdut/dlFo1sIyFrCcafoSn3EXcNz2AIQXV+NK2RQ0DYIDlVV8CAwEAAaMdMBswDAYDVR0TAQH/BAIwADALBgNVHQ8EBAMCBsAwDQYJKoZIhvcNAQELBQADggIBAF2ABRqiyPA4hsuBqvzjMP5X25oQfEYr+gXOtsIEoJsBd2m2A6x6oCbWkoLw29Ey8uQHwKzAW/sBSxuZjagx5ybMxmL4BQ5PFuH7+y2lqRm02Jt0oA9fphsaCPQSGXE/dei3nWIEoBx5Up05Ux7S8RD+PH5NjtZIXDXEVkaYNZ/zNNyXOlqhnFa7YTwNUt5g4lvvXNkb2godyXbFBwZPUY48FP93H+0oY8WsHyghBdL65xkb0AO+oZmcGoYA7AjjxFWCuL3hxHOlrffNuMlzxc279H7BprbMPKbaiMfo2UPjNNxNZ+CtSlJ82ITIFJ2SV9c2uGM5XjDWTrBxxYqxxZHERQEYkHFM76qGwiuoA1pMAsTWWe1KvAY3I7n0qNbMnldlVdZQ1jOtTTsSU/lHtSkQJ6E4m9jiyKga/LkmjNLBa/NMbqbYN0d+NbBCWfWtshXx9go1EDbTfnFFlDPZ6yiY2/U25WozcLS4Y3ILbOH9KJ1J/3f+CD3iSQxnCdHfeSQY2+7sYBFdFwEVxlbzjBSsS5Uod40ETrhwi+1hhV0Um92m9AHnK3OJS5+ZrFqFNosoLrvkXnHcbCf9G0PbV+kZjo0NKCKFnK6AEnhYM1Y9Ldnznc9ZBzaDJ0uxxM/wXbFlpjHJW3K+X1Cd4GPCHcepsJK0w8v3vdlcAxLCtq9t</o:BinarySecurityToken><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"#_0\"><Transforms><Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><DigestValue>TkB6rHvN5ZQNM8QDbcaOA1Jj32o=</DigestValue></Reference></SignedInfo><SignatureValue>ejowgrNqbSJkaXEyueMyQF2nQwAOTrISld/+QDNu/Zb+0oScKnFlE+p984Z7FsQJQrIx4X8FnCT4djrovCqy3/T53N5ennqweqqjkPKEXNDLdAoEkBPfvTWiXH/tKcU2moO7+NlQl5x84cFV8ywUxCKOfUQEaPjpabXvpwwapF4=</SignatureValue><KeyInfo><o:SecurityTokenReference><o:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#uuid-b246ed31-bfec-804a-5212-095ac6d97d3c-1\"/></o:SecurityTokenReference></KeyInfo></Signature></o:Security></s:Header><s:Body><Autentica xmlns=\"http://DescargaMasivaTerceros.gob.mx\"/></s:Body></s:Envelope>";
	
	private final static int endBlock01 = 292;
	private final static int initBlock02 = 423;
	private final static int endBlock02 = 705;
	private final static int initBlock03 = 2617;
	private final static int endBlock03 = 2695;
	private final static int initBlock04 = 3113;
	private final static int endBlock04 = 3129;
	private final static int initBlock05 = 3301;
	
	private final static String TEMPLATESOLICITUD = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:des\"http://DescargaMasivaTerceros.gob.mx\"><soapenv:Header><soapenv:Body></soapenv:Body></soapenv:Envelope>";
	private final static int endBlock01SolicitudDescarga = 154;
	
	public static void buildSOAP() throws SelloException{
		String template = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><s:Header><o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"><u:Timestamp u:Id=\"_0\"><u:Created>2019-02-08T06:38:08.000Z</u:Created><u:Expires>2019-02-08T06:43:08.000Z</u:Expires></u:Timestamp><o:BinarySecurityToken u:Id=\"uuid-b246ed31-bfec-804a-5212-095ac6d97d3c-1\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">MIIFljCCA36gAwIBAgIUMjAwMDEwMDAwMDAzMDAwMDM3MDIwDQYJKoZIhvcNAQELBQAwggFmMSAwHgYDVQQDDBdBLkMuIDIgZGUgcHJ1ZWJhcyg0MDk2KTEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMRUwEwYDVQQtEwxTQVQ5NzA3MDFOTjMxITAfBgkqhkiG9w0BCQIMElJlc3BvbnNhYmxlOiBBQ0RNQTAeFw0xNDA2MDYxNDU5MDZaFw0xODA2MDYxNDU5MDZaMIIBBTE3MDUGA1UEAxMuRElTVFJJQlVJRE9SQSBERSBDQVJORVMgU0FOVEEgTUFSQ0VMQSBTQSBERSBDVjE3MDUGA1UEKRMuRElTVFJJQlVJRE9SQSBERSBDQVJORVMgU0FOVEEgTUFSQ0VMQSBTQSBERSBDVjE3MDUGA1UEChMuRElTVFJJQlVJRE9SQSBERSBDQVJORVMgU0FOVEEgTUFSQ0VMQSBTQSBERSBDVjElMCMGA1UELRMcRENTMDAxMjIxMTY0IC8gRlVBQjc3MDExN0JYQTEeMBwGA1UEBRMVIC8gRlVBQjc3MDExN01ERlJOTjA5MREwDwYDVQQLFAhQcnVlYmFfMTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAhVicJ41LJkM/sROtrirXc5z1H10UTNYubjBg3SCAVtWNRA150FX7V1wYx1y+7oUW8ZxR8MZuysP2RyUHRy64j0A0gqvwCJIkJMIqcl9DTSoxDJtiMkudypSiJ3Gdut/dlFo1sIyFrCcafoSn3EXcNz2AIQXV+NK2RQ0DYIDlVV8CAwEAAaMdMBswDAYDVR0TAQH/BAIwADALBgNVHQ8EBAMCBsAwDQYJKoZIhvcNAQELBQADggIBAF2ABRqiyPA4hsuBqvzjMP5X25oQfEYr+gXOtsIEoJsBd2m2A6x6oCbWkoLw29Ey8uQHwKzAW/sBSxuZjagx5ybMxmL4BQ5PFuH7+y2lqRm02Jt0oA9fphsaCPQSGXE/dei3nWIEoBx5Up05Ux7S8RD+PH5NjtZIXDXEVkaYNZ/zNNyXOlqhnFa7YTwNUt5g4lvvXNkb2godyXbFBwZPUY48FP93H+0oY8WsHyghBdL65xkb0AO+oZmcGoYA7AjjxFWCuL3hxHOlrffNuMlzxc279H7BprbMPKbaiMfo2UPjNNxNZ+CtSlJ82ITIFJ2SV9c2uGM5XjDWTrBxxYqxxZHERQEYkHFM76qGwiuoA1pMAsTWWe1KvAY3I7n0qNbMnldlVdZQ1jOtTTsSU/lHtSkQJ6E4m9jiyKga/LkmjNLBa/NMbqbYN0d+NbBCWfWtshXx9go1EDbTfnFFlDPZ6yiY2/U25WozcLS4Y3ILbOH9KJ1J/3f+CD3iSQxnCdHfeSQY2+7sYBFdFwEVxlbzjBSsS5Uod40ETrhwi+1hhV0Um92m9AHnK3OJS5+ZrFqFNosoLrvkXnHcbCf9G0PbV+kZjo0NKCKFnK6AEnhYM1Y9Ldnznc9ZBzaDJ0uxxM/wXbFlpjHJW3K+X1Cd4GPCHcepsJK0w8v3vdlcAxLCtq9t</o:BinarySecurityToken><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"#_0\"><Transforms><Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><DigestValue>TkB6rHvN5ZQNM8QDbcaOA1Jj32o=</DigestValue></Reference></SignedInfo><SignatureValue>ejowgrNqbSJkaXEyueMyQF2nQwAOTrISld/+QDNu/Zb+0oScKnFlE+p984Z7FsQJQrIx4X8FnCT4djrovCqy3/T53N5ennqweqqjkPKEXNDLdAoEkBPfvTWiXH/tKcU2moO7+NlQl5x84cFV8ywUxCKOfUQEaPjpabXvpwwapF4=</SignatureValue><KeyInfo><o:SecurityTokenReference><o:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#uuid-b246ed31-bfec-804a-5212-095ac6d97d3c-1\"/></o:SecurityTokenReference></KeyInfo></Signature></o:Security></s:Header><s:Body><Autentica xmlns=\"http://DescargaMasivaTerceros.gob.mx\"/></s:Body></s:Envelope>";
		String first = template.substring(0, 3046);
		String second = template.substring(3074, 3129);
		String third = template.substring(3301, 3676);
		StringBuffer sbr = new StringBuffer();
		sbr.append(first);
		String digest = "";
		String cadena = "<u:Timestamp xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" u:Id=\"_0\"><u:Created>2019-02-08T06:38:08.000Z</u:Created><u:Expires>2019-02-08T06:43:08.000Z</u:Expires></u:Timestamp>";
		try {
			digest = DigestUtil.digest(cadena);
		}
		catch(SelloException e) {
			throw e;
		}
		sbr.append(digest);
		sbr.append(second);
		
		String sello = "";
		cadena = "<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></CanonicalizationMethod><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod><Reference URI=\"#_0\"><Transforms><Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></Transform></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod><DigestValue>TkB6rHvN5ZQNM8QDbcaOA1Jj32o=</DigestValue></Reference></SignedInfo>";
		String fileName = "C:\\Users\\jtemugin\\Downloads\\material-1549894787098\\material\\csd.key.pem";
		String password = "12345678a";
		try {
			sello = SignUtil.generaSello(fileName, password, cadena);
		}
		catch(SelloException e) {
			throw e;
		}
		
		sbr.append(sello);
		sbr.append(third);
		
		File f = new File("C:\\Users\\jtemugin\\Downloads\\material-1549894787098\\material\\Resultado.xml");
		FileOutputStream fos = null;
		PrintWriter out = null;
		try {
			fos = new FileOutputStream(f);
			out = new PrintWriter(fos);
			out.print(sbr.toString());
			out.flush();
			fos.close();
		}
		catch(IOException e) {
			throw new SelloException("Error IO: " + e.getMessage());
		}
	}
	
	public static String buildSOAP(String timeStamp, String strCertificate, String signInfo, String signedInfo){
		String templateTemp = TEMPLATE;
		int length = templateTemp.length();
		
		templateTemp = templateTemp.substring(0, endBlock01) + timeStamp + templateTemp.substring(initBlock02, endBlock02) + strCertificate + templateTemp.substring(initBlock03, endBlock03) + signInfo + templateTemp.substring(initBlock04, endBlock04) + signedInfo + templateTemp.substring(initBlock05, length);
				
		return templateTemp;
	}
	
	public static String buildSOAPSolicitudDescarga(String xml) {
		String templateTemp = TEMPLATESOLICITUD;
		int length = templateTemp.length();
		templateTemp = templateTemp.substring(0, endBlock01SolicitudDescarga) + xml + templateTemp.substring(endBlock01SolicitudDescarga, length);
		return templateTemp;
	}
	
	public static String buildSOAPVerificacionSolicitud(String xml) {
		String templateTemp = TEMPLATESOLICITUD;
		int length = templateTemp.length();
		templateTemp = templateTemp.substring(0, endBlock01SolicitudDescarga) + xml + templateTemp.substring(endBlock01SolicitudDescarga, length);
		return templateTemp;
	}
	
	public static String buildSOAPEjecutarDescarga(String xml) {
		String templateTemp = TEMPLATESOLICITUD;
		int length = templateTemp.length();
		templateTemp = templateTemp.substring(0, endBlock01SolicitudDescarga) + xml + templateTemp.substring(endBlock01SolicitudDescarga, length);
		return templateTemp;
	}
}