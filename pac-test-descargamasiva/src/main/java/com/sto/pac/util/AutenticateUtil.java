package com.sto.pac.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.sto.pac.exceptions.SelloException;

public class AutenticateUtil {
	private static String URL_SERVICE = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/Autenticacion/Autenticacion.svc";
	
	public static String autentica(String message) throws SelloException{
		//Code to make a webservice HTTP request
		String responseString = "";
		String outputString = "";
		String wsURL = URL_SERVICE;
		
		try {
			deshabilitaValidacionSSL();
		}
		catch(KeyManagementException e) {
			throw new SelloException("Error al deshabilitar SSL: " + e.getMessage());
		}
		catch(NoSuchAlgorithmException e) {
			throw new SelloException("Error al deshabilitar SSL: " + e.getMessage());
		}
		
		URL url = null;
		try {
			url = new URL(wsURL);
		}
		catch(MalformedURLException e) {
			throw new SelloException("Error Malformed URL: " + e.getMessage());
		}
		URLConnection connection = null;
		try {
			connection = url.openConnection();
		}
		catch(IOException e) {
			throw new SelloException("Error IO: " + e.getMessage());
		}
		HttpsURLConnection httpConn = (HttpsURLConnection)connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		String xmlInput = message;

		byte[] buffer = new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		try {
			bout.write(buffer);
		}
		catch(IOException e) {
			throw new SelloException("Error IO: " + e.getMessage());
		}
		
		byte[] b = bout.toByteArray();
		String SOAPAction = "http://DescargaMasivaTerceros.gob.mx/IAutenticacion/Autentica";
		// Set the appropriate HTTP parameters.
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("SOAPAction", SOAPAction);
		
		try {
			httpConn.setRequestMethod("POST");
		}
		catch(ProtocolException e) {
			throw new SelloException("Error Protocol: " + e.getMessage());
		}
		
		File fSalida = new File("C:\\Users\\jtemugin\\Downloads\\material-1549894787098\\material\\RequestSoap.xml");
		FileOutputStream fos = null;
		PrintWriter pwout = null;
		try {
			fos = new FileOutputStream(fSalida);
			pwout = new PrintWriter(fos);
			pwout.print(new String(b));
			pwout.flush();
			fos.close();
		}
		catch(IOException e) {
			throw new SelloException("Error IO: " + e.getMessage());
		}
		
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = null;
		try {			
			//Write the content of the request to the outputstream of the HTTP Connection.
			out = httpConn.getOutputStream();
			out.write(b);
			out.close();
		}
		catch(IOException e) {
			throw new SelloException("Error Conexion SAT Input: " + e.getMessage());
		}	
		
		//Ready with sending the request.

		//Read the response.
		InputStreamReader isr = null;
		try {
			if (httpConn.getResponseCode() == 200) {
			  isr = new InputStreamReader(httpConn.getInputStream());
			} else {
			  isr = new InputStreamReader(httpConn.getErrorStream());
			}
		}
		catch(IOException e) {
			throw new SelloException("Error Conexion SAT Output: " + e.getMessage());
		}

		BufferedReader in = new BufferedReader(isr);

		try {
			//Write the SOAP message response to a String.
			while ((responseString = in.readLine()) != null) {
				outputString = outputString + responseString;
			}
		}
		catch(IOException e) {
			throw new SelloException("Error Conexion SAT Reader: " + e.getMessage());
		}
		//Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
		//Document document = parseXmlFile(outputString); // Write a separate method to parse the xml input.
		//NodeList nodeLst = document.getElementsByTagName("<TagName of the element to be retrieved>");
		//String elementValue = nodeLst.item(0).getTextContent();
		//System.out.println(elementValue);

		//Write the SOAP message formatted to the console.
		//String formattedSOAPResponse = formatXML(outputString); // Write a separate method to format the XML input.
		//System.out.println(formattedSOAPResponse);
		//return elementValue;
		return outputString;
	}
	
	public static void deshabilitaValidacionSSL() throws NoSuchAlgorithmException, KeyManagementException {
		System.setProperty("jsse.enableSNIExtension", "false");

		TrustManager[] certificados;
		certificados = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		HostnameVerifier host;
		host = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		SSLContext contexto;
		contexto = SSLContext.getInstance("SSL");

		contexto.init(null, certificados, new SecureRandom());

		HttpsURLConnection.setDefaultSSLSocketFactory(contexto.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(host);
	}
}