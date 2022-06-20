package de.dhbw.woped.process2text.dataModel.bpmnReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import de.dhbw.woped.process2text.dataModel.process.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class BPMNReader {
    public ProcessModel getProcessModelFromBPMNString(InputStream input){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(input);
            doc.getDocumentElement().normalize();
            ProcessModel model = new ProcessModel();



            return model;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static void extractFlow(Document doc, ProcessModel model){

    }

    private static void extractEvents(Document doc, ProcessModel model, Pool pool, Lane lane) {
        NodeList start_event = doc.getElementsByTagName("bpmn:startEvent");
        NodeList end_event = doc.getElementsByTagName("bpmn:endEvent");
        int newId;

        for (int i = 0; i < start_event.getLength(); i++) {
            Node fstNode = start_event.item(i);
            newId = model.getNewId();
            model.addEvent(new Event(newId, "", lane, pool, EventType.START_EVENT));
        }

        for (int i = 0; i < end_event.getLength(); i++) {
            Node fstNode = end_event.item(i);
            newId = model.getNewId();
            model.addEvent(new Event(newId, "", lane, pool, EventType.END_EVENT));
        }




    }
    private static void extractGateways(Document doc, ProcessModel model, Pool pool, Lane lane){

        /*
        To-Do:
            - Analysieren, ob im BPMN Format noch weitere Gateway-Operatoren? (Inklusives OR)
            - Pool und Lane des jeweiligen Gateways bestimmen --> Aufruf aus Pool und Lane Methode
        */

        NodeList list_AND = doc.getElementsByTagName("bpmn:parallelGateway");
        NodeList list_XOR = doc.getElementsByTagName("bpmn:exclusiveGateway");
        //NodeList list_OR;
        int newId;

        for (int i = 0; i < list_AND.getLength(); i++) {
            Node fstNode = list_AND.item(i);
            newId = model.getNewId();
            model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.AND));
        }

        for (int i = 0; i < list_XOR.getLength(); i++) {
            Node scdNode = list_AND.item(i);
            newId = model.getNewId();
            model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.XOR));
        }


    }



}
