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
    private Intersection pickupAddress;
    private int pickupDuration;
    private Intersection deliveryAddress;
    private int deliveryDuration;

    public Request(Intersection pickupAddress, int pickupDuration, Intersection deliveryAddress, int deliveryDuration) {
        this.pickupAddress = pickupAddress;
        this.pickupDuration = pickupDuration;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDuration = deliveryDuration;
    }

    public Intersection getPickupAddress() {
        return pickupAddress;
    }

    public int getPickupDuration() {
        return pickupDuration;
    }

    public Intersection getDeliveryAddress() {
        return deliveryAddress;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }
}
