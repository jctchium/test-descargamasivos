package com.sto.pac.xmlcreators;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class EjecutarDescargaCreator {
	private static String NAMESPACE = "http://DescargaMasivaTerceros.sat.gob.mx";
	
	public static String getEjecutarDescarga(String idPaquete, String rfcSolicitante){		
		Document document = DocumentHelper.createDocument();
		Namespace namespace = new Namespace("des", NAMESPACE);
		
		QName qNamePeticionDescargaMasivosTerceros = new QName("PeticionDescargaMasivaTercerosEntrada", namespace);
		QName qNamePeticionDescarga = new QName("peticionDescarga", namespace);
		
		Element root = document.addElement(qNamePeticionDescargaMasivosTerceros);
		Element solicitud = root.addElement(qNamePeticionDescarga);
		solicitud.addAttribute("IdPaquete", idPaquete);
		solicitud.addAttribute("RfcSolicitante", rfcSolicitante);
		
		return document.asXML();
	}
}
