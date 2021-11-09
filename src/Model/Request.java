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
import java.util.Objects;

/**
 * Class that represents each request. It has a pick up address and a delivery address
 */
public class Request {
    /**
     * The pickup address of the request
     */
    private Address pickupAddress;

    /**
     * The delivery address of the request
     */
    private Address deliveryAddress;


    /**
     * Constructor
     * @param pickupAddress
     * @param deliveryAddress
     */
    public Request(Address pickupAddress, Address deliveryAddress) {
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
    }

    // ------- Getters -------

    /**
     * @return pickupAddress
     */
    public Address getPickupAddress() {
        return pickupAddress;
    }

    /**
     * @return deliveryAddress
     */
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())){
            return false;
        }
        Request requestTest = (Request) o;
        return (Objects.equals(pickupAddress, requestTest.getPickupAddress()))
                    && (Objects.equals(deliveryAddress, requestTest.getDeliveryAddress()));
    }
}
