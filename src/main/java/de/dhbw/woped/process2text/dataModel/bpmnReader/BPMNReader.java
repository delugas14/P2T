package de.dhbw.woped.process2text.dataModel.bpmnReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import de.dhbw.woped.process2text.dataModel.process.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BPMNReader {
    public ProcessModel getProcessModelFromBPMNString(InputStream input){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(input);
            doc.getDocumentElement().normalize();
            ProcessModel model = new ProcessModel();
            extractPool(doc, model);
            extractArc(doc, model);



            return model;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void extractActivity(Document doc, ProcessModel model, Pool pool, Lane lane){
        NodeList list = doc.getElementById(pool.getName()).getChildNodes();
        String label;
        int type = 1;
        NodeList participants = doc.getElementsByTagName("bpmn:Participant");
        for (int i = 0; i < participants.getLength(); i++) {
            Element participant = (Element) participants.item(i);
            if (participant.getAttribute("processRef") == pool.getBPMNId()) {
                if (participant.getAttribute("name") == "Company" || participant.getAttribute("name") == "User") {
                    type = 2;
                }
            }
        }
        for (int i = 0; i < list.getLength(); i++) {
            Element frstNode = (Element) list.item(i);
            if (frstNode.getTagName() == "bpmn:task") {
                model.addActivity(new Activity(model.getNewId(), frstNode.getAttribute("name"), lane, pool, type));
            }

        }
    }

    private static void extractEvents(Document doc, ProcessModel model, Pool pool, Lane lane) {
        Element helpPool = doc.getElementById(pool.getName());
        NodeList start_event = helpPool.getElementsByTagName("bpmn:startEvent");
        NodeList end_event = helpPool.getElementsByTagName("bpmn:endEvent");
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
        Element helpPool = doc.getElementById(pool.getName());
        NodeList list_AND = helpPool.getElementsByTagName("bpmn:parallelGateway");
        NodeList list_XOR = helpPool.getElementsByTagName("bpmn:exclusiveGateway");
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
    private static void extractLane(Document doc, ProcessModel model, Pool pool){
        Element helpPool = doc.getElementById(pool.getName());

        NodeList lanes = helpPool.getElementsByTagName("bpmn:lane");
        for (int i =0; i < lanes.getLength(); i++) {
            Element frstNode = (Element) lanes.item(i);
            Lane lane = new Lane(frstNode.getAttribute("name"), pool.getName());
            model.addLane(frstNode.getAttribute("name"));
            extractActivity(doc, model , pool, lane);
            extractEvents(doc, model, pool, lane);
            extractGateways(doc, model, pool, lane);
        }
    }
    private static void extractPool(Document doc, ProcessModel model){
        NodeList pools = doc.getElementsByTagName("bpmn:process");
        for (int i =0; i < pools.getLength(); i++) {
            Element frstNode = (Element) pools.item(i);
            NodeList lanes = frstNode.getElementsByTagName("bpmn:laneSet");
            Pool pool = new Pool(frstNode.getAttribute("id"));
            model.addPool(frstNode.getAttribute("id"));
            if (lanes.getLength() == 0){
                extractActivity(doc, model , pool, null);
                extractEvents(doc, model, pool, null);
                extractGateways(doc, model, pool, null);
            } else {
                extractLane(doc, model, pool);
            }
        }
    }
    private static void extractArc(Document doc, ProcessModel model) {
        NodeList list = doc.getElementsByTagName("bpmn:sequenceFlow");
        for (int i = 0; i < list.getLength(); i++) {
            Node fstNode = list.item(i);
            Element arc = (Element) fstNode;
            String sourceRef = arc.getAttribute("sourceRef");
            String targetRef = arc.getAttribute("targetRef");
            String label = arc.getAttribute("id");
            de.dhbw.woped.process2text.dataModel.process.Element source = null;
            de.dhbw.woped.process2text.dataModel.process.Element target = null;
            HashMap<Integer, Activity> activities = model.getActivites();
            HashMap<Integer, Event> events = model.getEvents();
            HashMap<Integer, Gateway> gateways = model.getGateways();
            for (Map.Entry e : activities.entrySet()) {
                Activity activity = (Activity) e.getValue();
                if (activity.getLabel() == sourceRef) {
                    source = activity;
                }
                if (activity.getLabel() == targetRef) {
                    target = activity;
                }
            }
            if (source.equals(null) || target.equals(null)) {
                for (Map.Entry e : events.entrySet()) {
                    Event event = (Event) e.getValue();
                    if (event.getLabel() == sourceRef) {
                        source = event;
                    }
                    if (event.getLabel() == targetRef) {
                        target = event;
                    }
                }
                if (source.equals(null) || target.equals(null)) {
                    for (Map.Entry e : gateways.entrySet()) {
                        Gateway gateway = (Gateway) e.getValue();
                        if (gateway.getLabel() == sourceRef) {
                            source = gateway;
                        }
                        if (gateway.getLabel() == targetRef) {
                            target = gateway;
                        }
                    }
                }

                model.addArc(new Arc(model.getNewId(), label, source, target));
            }
        }
    }



}
