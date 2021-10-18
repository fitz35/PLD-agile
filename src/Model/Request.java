package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Request {
    private long pickupAddress; //find Intersection
    private int pickupDuration;
    private long deliveryAddress; //find Intersection
    private int deliveryDuration;

    public Request(long pickupAddress, int pickupDuration, long deliveryAddress, int deliveryDuration) {
        this.pickupAddress = pickupAddress;
        this.pickupDuration = pickupDuration;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDuration = deliveryDuration;
    }
}
