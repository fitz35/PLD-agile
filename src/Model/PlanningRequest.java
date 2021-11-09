package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class that represents all the requests that need to be satisfied
 */
public class PlanningRequest {
    /**
     * List with all requests
     */
    private ArrayList<Request> requestList;

    /**
     * A map to find a given Request by one of the address on it
     */
    private HashMap<Address, Request> addressRequestHashMap;
    /**
     * A map to find a given Address by it's index
     */
    private HashMap<Long, Address> idAddressHashMap;

    /**
     * The time of departure of the tour
     */
    private Date departureTime;

    /**
     * The starting point of the tour
     */
    private Intersection startingPoint;

    /**
     * List with all addresses
     */
    private ArrayList<Address> addressList;
    /**
     * Original planning request, as loaded from an XML file
     */
    private ArrayList<Request> originalPlanningRequest;


    /**
     * Default constructor
     */
    public PlanningRequest() {
        requestList = new ArrayList<>();
        initAddressMap();
        createAddresses();
    }

    /**
     * Constructor
     * @param requestArrayList All the requests that need to be satisfied
     * @param departureTime The time at which the driver will start it's tour
     * @param startingPoint Depot, place from which the driver will start and end it's tour
     */
    public PlanningRequest(ArrayList<Request> requestArrayList, Date departureTime, Intersection startingPoint) {
        this.requestList = requestArrayList;
        this.originalPlanningRequest = requestArrayList;
        this.departureTime = departureTime;
        this.startingPoint = startingPoint;
        initAddressMap();
        createAddresses();
    }

    /**
     * Finds a request with the desired delivery or pickup address
     * @param pickupOrDelivery The address in the request to be found
     * @return The request, containing pickupOrDelivery and de pick up or delivery that corresponds
     */
    public Request getRequestByAddress(Address pickupOrDelivery){
        return addressRequestHashMap.get(pickupOrDelivery);
    }

    /**
     * Add a new request to the requestList and updates all the HashMaps
     * @param newRequest request to be added
     */
    public void addRequest(Request newRequest) {
        requestList.add(newRequest);
        addressRequestHashMap.put(newRequest.getPickupAddress(), newRequest);
        addressRequestHashMap.put(newRequest.getDeliveryAddress(), newRequest);
        idAddressHashMap.put(newRequest.getPickupAddress().getId(), newRequest.getPickupAddress());
        idAddressHashMap.put(newRequest.getDeliveryAddress().getId(), newRequest.getPickupAddress());
    }

    /**
     * Set the startingPoint of the tour
     * @param startingPoint
     */
    public void setStartingPoint(Intersection startingPoint) {
        this.startingPoint = startingPoint;
    }

    public boolean isEmpty(){
        return requestList.isEmpty();
    }

    /**
     * Set the departureTime of the tour
     * @param departureTime
     */
    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * Set the requestList
     * @param requestList
     */
    public void setRequestList(ArrayList<Request> requestList) {
        this.requestList = requestList;
    }

    /**
     * @return addressList, an ordered list beginning with the starting point, then the pickup and the deliver
     * point of each request
     */
    public ArrayList<Address> getListAddress() {
        createAddresses();
        return addressList;
    }

    /**
     * Will create the addres List, in this list we will find all the addresses that need to be visited
     * And update the HashMaps for searching. It is very important to have the starting node at index 0
     * and always at index p=2n+1 the pickup address for the delivery address at index q=2n+2
     * As this order will be used when computing the TSP
     */
    private void createAddresses(){
        //return all Intersection from the request and add the starting point address at the first place
        addressList = new ArrayList<>(1 + requestList.size());
        idAddressHashMap = new HashMap<>();
        if(startingPoint != null) {
            Address startingAddress = new Address(startingPoint.getId(), startingPoint.getLatitude(),
                    startingPoint.getLongitude(), 0, 0/*for depot*/);
            addressList.add(startingAddress);

            for (Request req : requestList) {
                addressList.add(req.getPickupAddress());
                addressList.add(req.getDeliveryAddress());
                idAddressHashMap.put(req.getPickupAddress().getId(), req.getPickupAddress());
                idAddressHashMap.put(req.getDeliveryAddress().getId(), req.getDeliveryAddress());
            }
        }
    }

    /**
     * Get an address by its id
     * @param id
     * @return address
     */
    public Address getAddressById(long id){
        return idAddressHashMap.get(id);
    }

    /**
     * @return the size of the requestList
     */
    public int size(){
        return requestList.size();
    }


    /**
     * @return requestList
     */
    public ArrayList<Request> getRequestList() {
        return requestList;
    }

    /**
     * @return startingPoint
     */
    public Intersection getStartingPoint() {
        return startingPoint;
    }


    /**
     * @return departureTime
     */
    public Date getDepartureTime() {
        return departureTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        PlanningRequest planningTest= (PlanningRequest) o;
        return (Objects.equals(startingPoint,planningTest.getStartingPoint()))
                    && (Objects.equals(departureTime,planningTest.departureTime))
                    && (Objects.equals(requestList,planningTest.getRequestList()));
    }

    /**
     * Initialize the map used to find a request by a address
     */
    private void initAddressMap(){
        addressRequestHashMap = new HashMap<>();
        int i = 0;
        for(Request req : requestList){
            addressRequestHashMap.put(req.getDeliveryAddress(), req);
            addressRequestHashMap.put(req.getPickupAddress(), req);
            i++;
        }
    }

    /**
     * Remove a request from requestList
     * @param requestToRemove
     */
    public void removeRequest(Request requestToRemove){
        this.requestList.remove(requestToRemove);
        addressRequestHashMap.remove(requestToRemove.getPickupAddress());
        addressRequestHashMap.remove(requestToRemove.getDeliveryAddress());
        idAddressHashMap.remove(requestToRemove.getPickupAddress().getId());
        idAddressHashMap.remove(requestToRemove.getDeliveryAddress().getId());
    }

    /**
     * Go back to the original planning request, loaded from an XML file
     */
    public void resetToOriginal(){
        this.requestList = this.originalPlanningRequest;
        initAddressMap();
        createAddresses();
    }

}
