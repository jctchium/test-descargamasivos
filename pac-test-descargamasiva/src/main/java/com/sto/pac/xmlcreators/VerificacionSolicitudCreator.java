package com.sto.pac.xmlcreators;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class VerificacionSolicitudCreator {
	private static String NAMESPACE = "http://DescargaMasivaTerceros.sat.gob.mx";
	
	public static String getVerificacionSolicitud(String idSolicitud, String rfcSolicitante){
		Document document = DocumentHelper.createDocument();
		Namespace namespace = new Namespace("des", NAMESPACE);
		
		QName qNameVerificacionSolicitud = new QName("VerificaSolicitudDescarga", namespace);
		QName qNameSolicitud = new QName("solicitud", namespace);
		
		Element root = document.addElement(qNameVerificacionSolicitud);
		Element solicitud = root.addElement(qNameSolicitud);
		solicitud.addAttribute("IdSolicitud", idSolicitud);
		solicitud.addAttribute("RfcSolicitante", rfcSolicitante);
		
		return document.asXML();
	}
}
