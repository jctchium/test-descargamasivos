package com.sto.pac.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.cert.Certificate;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sto.pac.exceptions.CifradoException;

public class SignXMLUtil {
	public static String signXML(byte [] keyPFX, String passPFX, String xml) throws CifradoException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		}
		catch(ParserConfigurationException e) {
			throw new CifradoException("Error ParserConfiguration: " + e.getMessage());
		}
		
		Document doc = null;
		try {
			doc = builder.parse(new InputSource(new StringReader(xml)));
		}
		catch(IOException e) {
			throw new CifradoException("Error IO: " + e.getMessage());
		}
		catch(SAXException e) {
			throw new CifradoException("Error SAX: " + e.getMessage());
		}
		
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
		
		Reference ref = null;
		try {
			ref = fac.newReference("", 
					fac.newDigestMethod(DigestMethod.SHA1, null), 
					Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec)null)), null, null);
		}
		catch(InvalidAlgorithmParameterException e) {
			throw new CifradoException("Error InvalidAlgorithmParameter: " + e.getMessage());
		}
		catch(NoSuchAlgorithmException e) {
			throw new CifradoException("Error NoSuchAlgorithm Obtener Reference: " + e.getMessage());
		}
		
		SignedInfo si = null;
		try {
			si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec)null), fac.newSignatureMethod(SignatureMethod.RSA_SHA1,  null), Collections.singletonList(ref));
		}
		catch(InvalidAlgorithmParameterException e) {
			throw new CifradoException("Error InvalidAlgorithmParameter: " + e.getMessage());
		}
		catch(NoSuchAlgorithmException e) {
			throw new CifradoException("Error NoSuchAlgorithm Obtener Sign Info: " + e.getMessage());
		}
		
		KeyStore ks = null;
		try {
			ks = KeyStore.getInstance("pkcs12");
		}
		catch(KeyStoreException e) {
			throw new CifradoException("Error KeyStore: " + e.getMessage());
		}
		
		try {
			ks.load(new ByteArrayInputStream(keyPFX), passPFX.toCharArray());
		}
		catch(CertificateException e) {
			throw new CifradoException("Error Certificate: " + e.getMessage());
		}
		catch(NoSuchAlgorithmException e) {
			throw new CifradoException("Error NoSuchAlgorithm Cargar KeyStore: " + e.getMessage());
		}
		catch(IOException e) {
			throw new CifradoException("Error IO: " + e.getMessage());
		}
		
		String alias = null;
		try {
			alias = ks.aliases().nextElement();
		}
		catch(KeyStoreException e) {
			throw new CifradoException("Error KeyStore: " + e.getMessage());
		}
		
		PrivateKey privateKey = null;		
		try {
			privateKey = (PrivateKey)ks.getKey(alias, passPFX.toCharArray());
		}
		catch(UnrecoverableKeyException e) {
			throw new CifradoException("Error Unrecoverable: " + e.getMessage());
		}
		catch(NoSuchAlgorithmException e) {
			throw new CifradoException("Error NoSuchAlgorithm obtenerPrivateKey: " + e.getMessage());
		}
		catch(KeyStoreException e) {
			throw new CifradoException("Error KeyStore: " + e.getMessage());
		}
		
		Certificate cert = null;
		try {
			cert = ks.getCertificate(alias);
		}
		catch(KeyStoreException e) {
			throw new CifradoException("Error KeyStore: " + e.getMessage());
		}
		
		KeyInfoFactory kif = fac.getKeyInfoFactory();
		
		KeyValue kv = null;
		try {
			kv = kif.newKeyValue(cert.getPublicKey());
		}
		catch(KeyException e) {
			throw new CifradoException("Error Key: " + e.getMessage());
		}
		
		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));
		
		List <Certificate>x509List = new ArrayList<Certificate>();		
		x509List.add(cert);
		
		X509Data x509data = kif.newX509Data(x509List);
		
		List items = new ArrayList();		
		items.add(x509data);
		if(kv != null)
			items.add(kv);
		
		ki = kif.newKeyInfo(items);
		
		DOMSignContext dsc = new DOMSignContext(privateKey, doc.getDocumentElement().getFirstChild());
		
		XMLSignature xmlSignature = fac.newXMLSignature(si, ki);
		
		try {
			xmlSignature.sign(dsc);
		}
		catch(XMLSignatureException e) {
			throw new CifradoException("Error XMLSignature: " + e.getMessage());
		}
		catch(MarshalException e) {
			throw new CifradoException("Error Marshal: " + e.getMessage());
		}
		
		StringWriter sw = new StringWriter();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = null;
		try {
			trans = tf.newTransformer();
		}
		catch(TransformerConfigurationException e) {
			throw new CifradoException("Error TransformerConfiguration: " + e.getMessage());
		}
		
		try {
			trans.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(sw));
		}
		catch(TransformerException e) {
			throw new CifradoException("Error Transformer: " + e.getMessage());
		}
		
		String res = sw.toString();
		if(res.length() >= 38)
			return res.substring(38);
		else
			return res;
	}
}