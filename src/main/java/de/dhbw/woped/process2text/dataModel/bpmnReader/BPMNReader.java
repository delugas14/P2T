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

}
