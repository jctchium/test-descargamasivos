package com.sto.pac.xmlcreators;

import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class SolicitudDescargaCreator {
	private static String NAMESPACE = "http://DescargaMasivaTerceros.sat.gob.mx";
	
	public static String getSolicitudDescarga(String rfcEmisor, String rfcReceptor, String rfcSolicitante, String fechaInicial, String fechaFinal, String tipoSolicitud) throws IOException{
		Document document = DocumentHelper.createDocument();
		Namespace namespace = new Namespace("des", NAMESPACE);
		
		QName qNameSolicitudDescarga = new QName("SolicitudDescarga", namespace);
		QName qNameSolicitud = new QName("solicitud", namespace);
		
		Element root = document.addElement(qNameSolicitudDescarga);
		Element solicitud = root.addElement(qNameSolicitud);
		solicitud.addAttribute("RfcEmisor", rfcEmisor);
		solicitud.addAttribute("RfcReceptor", rfcReceptor);
		solicitud.addAttribute("RfcSolicitante", rfcSolicitante);
		solicitud.addAttribute("FechaInicial", fechaInicial);
		solicitud.addAttribute("FechaFinal", fechaFinal);
		solicitud.addAttribute("TipoSolicitud", tipoSolicitud);
		
		return document.asXML();
	}
}
