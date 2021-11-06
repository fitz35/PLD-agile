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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class PlanningRequest {
    private ArrayList<Request> requestList;
    private HashMap<Address, Integer> addressRequestHashMap;
    private Date departureTime;
    private Intersection startingPoint;
    private ArrayList<Address> AddressList;
    private boolean adressRCreated = false;

    public PlanningRequest() {
        requestList = new ArrayList<>();
        initAddressMap();
    }

    public PlanningRequest(ArrayList<Request> requestArrayList, Date departureTime, Intersection startingPoint) {
        this.requestList = requestArrayList;
        this.departureTime = departureTime;
        this.startingPoint = startingPoint;
        initAddressMap();
    }

    /**
     * Finds a request with the desired delivery or pickup address
     * @param pickupOrDelivery The address in the request to be found
     * @return The request, containing pickupOrDelivery and de pick up or delivery that corresponds
     */
    public Request getRequestByAddress(Address pickupOrDelivery){
        return requestList.get(addressRequestHashMap.get(pickupOrDelivery));
    }

    public void addRequest(Request newRequest) {
        requestList.add(newRequest);
    }

    public void setStartingPoint(Intersection startingPoint) {
        this.startingPoint = startingPoint;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public void setRequestList(ArrayList<Request> requestList) {
        this.requestList = requestList;
    }

    public ArrayList<Address> getListAddress() {

        if(adressRCreated)
        {
            System.out.println("Si pb lorsqu'on efface un planning puis on en créer un nouveau voir ic (PlanningRequest l57)");
            return AddressList;
        }else {
            //return all Intersection from the request and add the starting point address at the first place
            AddressList = new ArrayList<>(1 + requestList.size());
            Address startingAddress = new Address(startingPoint.getId(), startingPoint.getLatitude(), startingPoint.getLongitude(), 0, 0/*for depot*/);
            AddressList.add(startingAddress);

            for (Request req : requestList) {
                AddressList.add(req.getPickupAddress());
                AddressList.add(req.getDeliveryAddress());
            }
            adressRCreated = true;
            return AddressList;
        }
    }

    public Address getAddressById(long id){
        for(Address address : AddressList)
        {
            if(id == address.getId())
            {
                return address ;
            }
        }
        Address defaultAdress = new Address();
        return defaultAdress;
    }

    public int size(){
        return requestList.size();
    }

    public ArrayList<Request> getRequestList() {
        return requestList;
    }
    public Intersection getStartingPoint()
    {
        return startingPoint;
    }
    public Date getDepartureTime()
    {
        return departureTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanningRequest planningTest= (PlanningRequest) o;
        return Objects.equals(startingPoint,planningTest.getStartingPoint())
                && Objects.equals(departureTime,planningTest.departureTime)
                && Objects.equals(requestList,planningTest.getRequestList());
    }

    private void initAddressMap(){
        addressRequestHashMap = new HashMap<>();
        int i = 0;
        for(Request req : requestList){
            addressRequestHashMap.put(req.getDeliveryAddress(), i);
            addressRequestHashMap.put(req.getPickupAddress(), i);
            i++;
        }
    }
    public static void main(String[] args){
        PlanningRequest planning =new PlanningRequest();
    }


}
