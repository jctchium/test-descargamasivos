package com.sto.pac.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import com.sto.pac.exceptions.CifradoException;
import com.sto.pac.exceptions.SelloException;
import com.sto.pac.util.AutenticateUtil;
import com.sto.pac.util.BuilderUtil;
import com.sto.pac.util.CertificadoUtil;
import com.sto.pac.util.DigestUtil;
import com.sto.pac.util.SignInfoUtil;
import com.sto.pac.util.SignUtil;
import com.sto.pac.util.SignXMLUtil;
import com.sto.pac.util.TimeStampUtil;
import com.sto.pac.xmlcreators.EjecutarDescargaCreator;
import com.sto.pac.xmlcreators.SolicitudDescargaCreator;
import com.sto.pac.xmlcreators.VerificacionSolicitudCreator;

public class App {
	public static void main(String [] args) {
		//testDigest();
		//testSign();
		//testBuildSOAP();
		//testAutenticate();
		testMyCertificate();
		//testSolicitudDescargaCreator();
		//testVerificacionSolicitud();
		//testEjecutarDescarga();
	}
	
	private static void testSign() {
		String sello = "";
		String cadena = "<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></CanonicalizationMethod><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod><Reference URI=\"#_0\"><Transforms><Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></Transform></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod><DigestValue>TkB6rHvN5ZQNM8QDbcaOA1Jj32o=</DigestValue></Reference></SignedInfo>";
		String fileName = "C:\\Users\\jtemugin\\Downloads\\material-1549894787098\\material\\csd.key.pem";
		String password = "12345678a";
		try {
			sello = SignUtil.generaSello(fileName, password, cadena);
		}
		catch(SelloException e) {
			System.out.println("Error al generar sello: " + e.getMessage());
			return;
		}
		
		System.out.print(sello);
		System.out.println("\nOK");
	}
	
	private static void testDigest() {
		String digest = "";
		String cadena = "<u:Timestamp xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" u:Id=\"_0\"><u:Created>2019-02-08T06:38:08.000Z</u:Created><u:Expires>2019-02-08T06:43:08.000Z</u:Expires></u:Timestamp>";
		try {
			digest = DigestUtil.digest(cadena);
		}
		catch(SelloException e) {
			System.out.println("Error al generar sello: " + e.getMessage());
			return;
		}
		
		System.out.print(digest);
		System.out.println("\nOK");
	}
	
	private static void testBuildSOAP() {
		try {
			BuilderUtil.buildSOAP();
		}
		catch(SelloException e) {
			System.out.println("Error al generar sello: " + e.getMessage());
			return;
		}
		System.out.println("OK");
	}
	
	private static void testAutenticate() {
		String respuesta = "";
		String mensaje = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><s:Header><o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"><u:Timestamp u:Id=\"_0\"><u:Created>2019-02-08T06:38:08.000Z</u:Created><u:Expires>2019-02-08T06:43:08.000Z</u:Expires></u:Timestamp><o:BinarySecurityToken u:Id=\"uuid-b246ed31-bfec-804a-5212-095ac6d97d3c-1\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">MIIFljCCA36gAwIBAgIUMjAwMDEwMDAwMDAzMDAwMDM3MDIwDQYJKoZIhvcNAQELBQAwggFmMSAwHgYDVQQDDBdBLkMuIDIgZGUgcHJ1ZWJhcyg0MDk2KTEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMRUwEwYDVQQtEwxTQVQ5NzA3MDFOTjMxITAfBgkqhkiG9w0BCQIMElJlc3BvbnNhYmxlOiBBQ0RNQTAeFw0xNDA2MDYxNDU5MDZaFw0xODA2MDYxNDU5MDZaMIIBBTE3MDUGA1UEAxMuRElTVFJJQlVJRE9SQSBERSBDQVJORVMgU0FOVEEgTUFSQ0VMQSBTQSBERSBDVjE3MDUGA1UEKRMuRElTVFJJQlVJRE9SQSBERSBDQVJORVMgU0FOVEEgTUFSQ0VMQSBTQSBERSBDVjE3MDUGA1UEChMuRElTVFJJQlVJRE9SQSBERSBDQVJORVMgU0FOVEEgTUFSQ0VMQSBTQSBERSBDVjElMCMGA1UELRMcRENTMDAxMjIxMTY0IC8gRlVBQjc3MDExN0JYQTEeMBwGA1UEBRMVIC8gRlVBQjc3MDExN01ERlJOTjA5MREwDwYDVQQLFAhQcnVlYmFfMTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAhVicJ41LJkM/sROtrirXc5z1H10UTNYubjBg3SCAVtWNRA150FX7V1wYx1y+7oUW8ZxR8MZuysP2RyUHRy64j0A0gqvwCJIkJMIqcl9DTSoxDJtiMkudypSiJ3Gdut/dlFo1sIyFrCcafoSn3EXcNz2AIQXV+NK2RQ0DYIDlVV8CAwEAAaMdMBswDAYDVR0TAQH/BAIwADALBgNVHQ8EBAMCBsAwDQYJKoZIhvcNAQELBQADggIBAF2ABRqiyPA4hsuBqvzjMP5X25oQfEYr+gXOtsIEoJsBd2m2A6x6oCbWkoLw29Ey8uQHwKzAW/sBSxuZjagx5ybMxmL4BQ5PFuH7+y2lqRm02Jt0oA9fphsaCPQSGXE/dei3nWIEoBx5Up05Ux7S8RD+PH5NjtZIXDXEVkaYNZ/zNNyXOlqhnFa7YTwNUt5g4lvvXNkb2godyXbFBwZPUY48FP93H+0oY8WsHyghBdL65xkb0AO+oZmcGoYA7AjjxFWCuL3hxHOlrffNuMlzxc279H7BprbMPKbaiMfo2UPjNNxNZ+CtSlJ82ITIFJ2SV9c2uGM5XjDWTrBxxYqxxZHERQEYkHFM76qGwiuoA1pMAsTWWe1KvAY3I7n0qNbMnldlVdZQ1jOtTTsSU/lHtSkQJ6E4m9jiyKga/LkmjNLBa/NMbqbYN0d+NbBCWfWtshXx9go1EDbTfnFFlDPZ6yiY2/U25WozcLS4Y3ILbOH9KJ1J/3f+CD3iSQxnCdHfeSQY2+7sYBFdFwEVxlbzjBSsS5Uod40ETrhwi+1hhV0Um92m9AHnK3OJS5+ZrFqFNosoLrvkXnHcbCf9G0PbV+kZjo0NKCKFnK6AEnhYM1Y9Ldnznc9ZBzaDJ0uxxM/wXbFlpjHJW3K+X1Cd4GPCHcepsJK0w8v3vdlcAxLCtq9t</o:BinarySecurityToken><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"#_0\"><Transforms><Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><DigestValue>TkB6rHvN5ZQNM8QDbcaOA1Jj32o=</DigestValue></Reference></SignedInfo><SignatureValue>ejowgrNqbSJkaXEyueMyQF2nQwAOTrISld/+QDNu/Zb+0oScKnFlE+p984Z7FsQJQrIx4X8FnCT4djrovCqy3/T53N5ennqweqqjkPKEXNDLdAoEkBPfvTWiXH/tKcU2moO7+NlQl5x84cFV8ywUxCKOfUQEaPjpabXvpwwapF4=</SignatureValue><KeyInfo><o:SecurityTokenReference><o:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#uuid-b246ed31-bfec-804a-5212-095ac6d97d3c-1\"/></o:SecurityTokenReference></KeyInfo></Signature></o:Security></s:Header><s:Body><Autentica xmlns=\"http://DescargaMasivaTerceros.gob.mx\"/></s:Body></s:Envelope>";
		try {
			respuesta = AutenticateUtil.autentica(mensaje);
		}
		catch(SelloException e) {
			System.out.println("Error al generar sello: " + e.getMessage());
			return;
		}
		System.out.println(respuesta);
		System.out.println("OK");
	}
	
	private static void testMyCertificate() {
		//CertificadoDao certificadoDao = new CertificadoDao();
		
		/*CertificadoBean certificadoBean = null;
		try {
			certificadoBean = certificadoDao.getCertificado();
		}
		catch(DAOException e) {
			System.out.println("Error DAO: " + e.getMessage());
			return;
		}*/
		
		/*Certificado certificado = null;
		try {
			certificado = certificadoDao.buildCertificado();
		}
		catch(DAOException e) {
			System.out.println("Error DAO: " + e.getMessage());
			return;
		}*/
						
		String timeStamp = TimeStampUtil.getTimeStamp();
		System.out.println("TimeStamp: [" + timeStamp + "]");
		String digest = "";
		try {
			digest = DigestUtil.digest(timeStamp);
		}
		catch(SelloException e) {
			System.out.println("Error Sello: " + e.getMessage());
			return;
		}
		System.out.println("Digest: [" + digest + "]");
		
		String signInfo = SignInfoUtil.getSignInfo(digest);
		System.out.println("SignInfo: [" + signInfo + "]");
		String signedInfo = "";
		
		//System.out.println("Certificado: [" + new String(certificadoBean.getOrigenFuente()) + "]");
		//System.out.println("Certificado: [" + certificado.getX509Certificate().getPublicKey() + "]");
		
		try {
			//signedInfo = SignUtil.generaSello(certificadoBean.getKey(), certificadoBean.getContrasena(), signInfo);
			//signedInfo = SignUtil.generaSello(certificado, signInfo);
			
			String fileName = "C:\\Users\\jtemugin\\Downloads\\material-1549894787098\\material\\csd.key.pem";
			//String fileName = "C:\\Users\\jtemugin\\Downloads\\openssl-1.0.2j-fips-x86_64\\OpenSSL\\bin\\Claveprivada_FIEL_STO020301G28_20190205_181600.key.pem";
			//String fileName = "C:\\Users\\jtemugin\\Downloads\\material-1549894787098\\material\\FIEL_Pruebas_MAG041126GT8.key.pem";
			//String fileName = "C:\\Users\\jtemugin\\Downloads\\material-1549894787098\\material\\keyprod.pem";
			String password = "12345678a";
			//String password = "password";
			
			signedInfo = SignUtil.generaSello(fileName, password, signInfo);
		}
		catch(SelloException e) {
			System.out.println("Error Sello: " + e.getMessage());
			return;
		}
		
		System.out.println("SignedInfo: [" + signedInfo + "]");
		
		//String mensajeSoap = BuilderUtil.buildSOAP(timeStamp, Base64.getEncoder().encodeToString(certificadoBean.getOrigenFuente()), signInfo, signedInfo);
		String strCertificado = "";
		try {
			strCertificado = CertificadoUtil.parseCertificado("C:\\Users\\jtemugin\\Downloads\\material-1549894787098\\material\\csd.cer.pem");
			//strCertificado = CertificadoUtil.parseCertificado("C:\\Users\\jtemugin\\Downloads\\material-1549894787098\\material\\FIEL_Pruebas_MAG041126GT8.cer.pem");
			//strCertificado = CertificadoUtil.parseCertificado("C:\\Users\\jtemugin\\Downloads\\material-1549894787098\\material\\certificateprod.pem");
			//strCertificado = CertificadoUtil.parseCertificado("C:\\Users\\jtemugin\\Downloads\\openssl-1.0.2j-fips-x86_64\\OpenSSL\\bin\\00001000000413411625.pem");
		}
		catch(IOException e) {
			System.out.println("Error IO: " + e.getMessage());
			return;
		}
		System.out.println("Certificado: [" + strCertificado + "]");
		String mensajeSoap = BuilderUtil.buildSOAP(timeStamp, strCertificado, signInfo, signedInfo);
						
		/*System.out.println("TimeStamp: " + timeStamp);
		System.out.println("Digest: " + digest);
		System.out.println("SignInfo: " + signInfo);
		System.out.println("SignedInfo: " + signedInfo);
		System.out.println("mensajeSOAP: " + mensajeSoap);
		System.out.println("OK");*/
		
		System.out.println("mensajeSOAP: " + mensajeSoap);
		
		String respuesta = "";
		try {
			respuesta = AutenticateUtil.autentica(mensajeSoap);
		}
		catch(SelloException e) {
			System.out.println("Error al autenticar sello: " + e.getMessage());
			return;
		}
		
		System.out.println(respuesta);
		System.out.println("OK");
	}
	
	private static void testSolicitudDescargaCreator() {
		String rfcEmisor = "PESH8805072Q5";
		String rfcReceptor = "";
		String rfcSolicitante = "PESH8805072Q5";
		String fechaInicial = "2018-08-01T00:00:12";
		String fechaFinal = "2018-10-20T00:00:12";
		String tipoSolicitud = "CFDI";
		
		String xmlSolicitudDescarga = "";
		
		try {
			xmlSolicitudDescarga = SolicitudDescargaCreator.getSolicitudDescarga(rfcEmisor, rfcReceptor, rfcSolicitante, fechaInicial, fechaFinal, tipoSolicitud);
			//System.out.print(xmlSolicitudDescarga);
			//System.out.println("\nOK");
		}
		catch(IOException e) {
			System.out.println("Error IO: " + e.getMessage());
			return;
		}
		
		String fileName = "C:\\Users\\jtemugin\\Downloads\\openssl-1.0.2j-fips-x86_64\\OpenSSL\\bin\\FIEL_Pruebas_MAG041126GT8.pfx";
		String passPFX = "password";
		File f = new File(fileName);
		FileInputStream fis = null;
		byte [] keyPFX = new byte[(int)f.length()];
		try {
			fis = new FileInputStream(f);
			fis.read(keyPFX, 0, keyPFX.length);
			fis.close();
		}
		catch(IOException e) {
			System.out.println("Error IO: " + e.getMessage());
			return;
		}
		
		String signedInfo = null;
		try {
			signedInfo = SignXMLUtil.signXML(keyPFX, passPFX, xmlSolicitudDescarga);
		}
		catch(CifradoException e) {
			System.out.println("Error Cifrado: " + e.getMessage());
			return;
		}
		
		System.out.println("SignedInfo: [" + signedInfo + "]");
		
		String strSOAP = BuilderUtil.buildSOAPSolicitudDescarga(signedInfo);
		System.out.println("SOAP: [" + strSOAP + "]");
	}
	
	private static void testVerificacionSolicitud() {
		String rfcSolicitante = "PESH8805072Q5";
		String idSolicitud = "";
		String token = "";
		
		String fileName = "C:\\Users\\jtemugin\\Downloads\\openssl-1.0.2j-fips-x86_64\\OpenSSL\\bin\\FIEL_Pruebas_MAG041126GT8.pfx";
		String passPFX = "password";
		File f = new File(fileName);
		FileInputStream fis = null;
		byte [] keyPFX = new byte[(int)f.length()];
		try {
			fis = new FileInputStream(f);
			fis.read(keyPFX, 0, keyPFX.length);
			fis.close();
		}
		catch(IOException e) {
			System.out.println("Error IO: " + e.getMessage());
			return;
		}
		
		String xmlVerificacionSolicitud = VerificacionSolicitudCreator.getVerificacionSolicitud(idSolicitud, rfcSolicitante);
		
		String signedInfo = null;
		try {
			signedInfo = SignXMLUtil.signXML(keyPFX, passPFX, xmlVerificacionSolicitud);
		}
		catch(CifradoException e) {
			System.out.println("Error Cifrado: " + e.getMessage());
			return;
		}
		
		System.out.println("SignedInfo: [" + signedInfo + "]");
		
		String strSOAP = BuilderUtil.buildSOAPVerificacionSolicitud(signedInfo);
		System.out.println("SOAP: [" + strSOAP + "]");		
	}
	
	private static void testEjecutarDescarga() {
		String rfcSolicitante = "";
		String idPaquete = "";
		
		String fileName = "C:\\Users\\jtemugin\\Downloads\\openssl-1.0.2j-fips-x86_64\\OpenSSL\\bin\\FIEL_Pruebas_MAG041126GT8.pfx";
		String passPFX = "password";
		File f = new File(fileName);
		FileInputStream fis = null;
		byte [] keyPFX = new byte[(int)f.length()];
		try {
			fis = new FileInputStream(f);
			fis.read(keyPFX, 0, keyPFX.length);
			fis.close();
		}
		catch(IOException e) {
			System.out.println("Error IO: " + e.getMessage());
			return;
		}
		
		String xmlEjecutarDescarga = EjecutarDescargaCreator.getEjecutarDescarga(idPaquete, rfcSolicitante);
		
		String signedInfo = null;
		try {
			signedInfo = SignXMLUtil.signXML(keyPFX, passPFX, xmlEjecutarDescarga);
		}
		catch(CifradoException e) {
			System.out.println("Error Cifrado: " + e.getMessage());
			return;
		}
		
		System.out.println("SignedInfo: [" + signedInfo + "]");
		
		String strSOAP = BuilderUtil.buildSOAPEjecutarDescarga(signedInfo);
		System.out.println("SOAP: [" + strSOAP + "]");
	}
}