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
    public HashMap<Integer, String> transformedElemsRev;
    public ProcessModel getProcessModelFromBPMNString(InputStream input){
        try {
            transformedElemsRev = new HashMap<>();
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

    private void extractActivity(Document doc, ProcessModel model, Pool pool, Lane lane){
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
        NodeList list = doc.getElementsByTagName("bpmn:process");
        for (int i = 0; i < list.getLength(); i++) {
            Node fstNode = list.item(i);
            Element helpPool = (Element) fstNode;
            if (helpPool.getAttribute("id") == pool.getName()) {
                NodeList activities = helpPool.getElementsByTagName("bpmn:task");
                for (int j = 0; j < activities.getLength(); j++) {
                    Element scdNode = (Element) activities.item(j);
                    if (scdNode.getTagName() == "bpmn:task") {
                        Activity activity = new Activity(model.getNewId(), scdNode.getAttribute("name"), lane, pool, type);
                        activity.addBPMNId(scdNode.getAttribute("id"));
                        model.addActivity(activity);
                        this.transformedElemsRev.put(model.getNewId(), scdNode.getAttribute("id"));
                    }

                }
            }
        }


    }

    private void extractEvents(Document doc, ProcessModel model, Pool pool, Lane lane) {
        NodeList list = doc.getElementsByTagName("bpmn:process");
        for (int i = 0; i < list.getLength(); i++) {
            Node fstNode = list.item(i);
            Element helpPool = (Element) fstNode;
            if (helpPool.getAttribute("id") == pool.getName()) {
                NodeList intermediate_event = helpPool.getElementsByTagName("bpmn:intermediateCatchEvent");
                NodeList start_event = helpPool.getElementsByTagName("bpmn:startEvent");
                NodeList end_event = helpPool.getElementsByTagName("bpmn:endEvent");
                int newId;
                for (int j = 0; j < intermediate_event.getLength(); j++){
                    Node scdNode = intermediate_event.item(j);
                    Element event = (Element) scdNode;
                    newId = model.getNewId();
                    Event interelement = new Event(newId, "", lane, pool, EventType.INTM_MSG_THR);
                    interelement.addBPMNId(event.getAttribute("id"));
                    model.addEvent(interelement);
                    //model.addEvent(new Event(newId, "", lane, pool, EventType.START_EVENT));
                    this.transformedElemsRev.put(model.getNewId(), event.getAttribute("id"));
                }

                for (int j = 0; j < start_event.getLength(); j++) {
                    Node scdNode = start_event.item(j);
                    Element event = (Element) scdNode;
                    newId = model.getNewId();
                    Gateway gateway = new Gateway(newId, "", lane, pool, 0);
                    gateway.addBPMNId(event.getAttribute("id"));
                    model.addGateway(gateway);
                    //model.addEvent(new Event(newId, "", lane, pool, EventType.START_EVENT));
                    this.transformedElemsRev.put(model.getNewId(), event.getAttribute("id"));
                }

                for (int j = 0; j < end_event.getLength(); j++) {
                    Node scdNode = end_event.item(j);
                    Element event = (Element) scdNode;
                    newId = model.getNewId();
                    Activity activity = new Activity(newId, "complete process", lane, pool, 0);
                    activity.addBPMNId(event.getAttribute("id"));
                    model.addActivity(activity);
                    //model.addEvent(new Event(newId, "", lane, pool, EventType.END_EVENT));
                    this.transformedElemsRev.put(model.getNewId(), event.getAttribute("id"));
                }
            }
        }




    }
    private void extractGateways(Document doc, ProcessModel model, Pool pool, Lane lane){

        /*
        To-Do:
            - Analysieren, ob im BPMN Format noch weitere Gateway-Operatoren? (Inklusives OR)
            - Pool und Lane des jeweiligen Gateways bestimmen --> Aufruf aus Pool und Lane Methode

        */
        NodeList list = doc.getElementsByTagName("bpmn:process");
        for (int i = 0; i < list.getLength(); i++) {
            Node fstNode = list.item(i);
            Element helpPool = (Element) fstNode;
            if (helpPool.getAttribute("id") == pool.getName()) {
                NodeList list_AND = helpPool.getElementsByTagName("bpmn:parallelGateway");
                NodeList list_XOR = helpPool.getElementsByTagName("bpmn:exclusiveGateway");
                //NodeList list_OR;
                int newId;

                for (int j = 0; j < list_AND.getLength(); j++) {
                    Node scdNode = list_AND.item(j);
                    Element gw = (Element) scdNode;
                    newId = model.getNewId();
                    Gateway gateway = new Gateway(newId, "", lane, pool, GatewayType.AND);
                    gateway.addBPMNId(gw.getAttribute("id"));
                    model.addGateway(gateway);
                    this.transformedElemsRev.put(model.getNewId(), gw.getAttribute("id"));
                }

                for (int j = 0; j < list_XOR.getLength(); j++) {
                    Node scdNode = list_XOR.item(j);
                    Element gw = (Element) scdNode;
                    newId = model.getNewId();
                    Gateway gateway = new Gateway(newId, "", lane, pool, GatewayType.XOR);
                    gateway.addBPMNId(gw.getAttribute("id"));
                    model.addGateway(gateway);
                    this.transformedElemsRev.put(model.getNewId(), gw.getAttribute("id"));
                }
            }
        }


    }
    private void extractLane(Document doc, ProcessModel model, Pool pool){
        NodeList list = doc.getElementsByTagName("bpmn:process");
        for (int i = 0; i < list.getLength(); i++) {
            Node fstNode = list.item(i);
            Element helpPool = (Element) fstNode;
            if (helpPool.getAttribute("id") == pool.getName()) {
                NodeList lanes = helpPool.getElementsByTagName("bpmn:lane");
                for (int j = 0; j < lanes.getLength(); j++) {
                    Element frstNode = (Element) lanes.item(j);
                    Lane lane = new Lane(frstNode.getAttribute("name"), pool.getName());
                    model.addLane(frstNode.getAttribute("name"));
                    this.transformedElemsRev.put(model.getNewId(), frstNode.getAttribute("id"));
                    extractActivity(doc, model, pool, lane);
                    extractEvents(doc, model, pool, lane);
                    extractGateways(doc, model, pool, lane);
                }
            }
        }
    }
    private void extractPool(Document doc, ProcessModel model){
        NodeList pools = doc.getElementsByTagName("bpmn:process");
        for (int i =0; i < pools.getLength(); i++) {
            Element frstNode = (Element) pools.item(i);
            NodeList lanes = frstNode.getElementsByTagName("bpmn:laneSet");
            Pool pool = new Pool(frstNode.getAttribute("id"));
            model.addPool(frstNode.getAttribute("id"));
            this.transformedElemsRev.put(model.getNewId(), frstNode.getAttribute("id"));
            if (lanes.getLength() == 0){
                extractActivity(doc, model , pool, null);
                extractEvents(doc, model, pool, null);
                extractGateways(doc, model, pool, null);
            } else {
                extractLane(doc, model, pool);
            }
        }
    }
    private void extractArc(Document doc, ProcessModel model) {
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
                if (activity.getBpmnId().equals(sourceRef)) {
                    source = activity;
                }
                if (activity.getBpmnId().equals(targetRef)) {
                    target = activity;
                }
            }
            if (source == (null) || target == (null)) {
                for (Map.Entry e : events.entrySet()) {
                    Event event = (Event) e.getValue();
                    if (event.getBpmnId().equals(sourceRef)) {
                        source = event;
                    }
                    if (event.getBpmnId().equals(targetRef)) {
                        target = event;
                    }
                }
                if (source == (null) || target == (null)) {
                    for (Map.Entry e : gateways.entrySet()) {
                        Gateway gateway = (Gateway) e.getValue();
                        if (gateway.getBpmnId().equals(sourceRef)) {
                            source = gateway;
                        }
                        if (gateway.getBpmnId().equals(targetRef)) {
                            target = gateway;
                        }
                    }
                    if (source == (null)) {
                        System.out.println("*****************************************************");
                        System.out.println("Missing Source: " + sourceRef);
                    }
                    if (target == (null)) {
                        System.out.println("Missing Target: " + targetRef);
                    }
                }

            }
            model.addArc(new Arc(model.getNewId(), label, source, target));
            this.transformedElemsRev.put(model.getNewId(), arc.getAttribute("id"));
        }
    }



}
