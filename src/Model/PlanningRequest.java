package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class PlanningRequest {
    private ArrayList<Request> requestList;
    private HashMap<Address, Integer> addressRequestHashMap;
    private Date departureTime;
    private Intersection startingPoint;
    private ArrayList<Address> addressList;
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
        if(adressRCreated){
            // System.out.println("Si pb lorsqu'on efface un planning puis on en créer un nouveau voir ic (PlanningRequest l57)");
            return addressList;
        }else {
            //return all Intersection from the request and add the starting point address at the first place
            addressList = new ArrayList<>(1 + requestList.size());
            Address startingAddress = new Address(startingPoint.getId(), startingPoint.getLatitude(),
                    startingPoint.getLongitude(), 0, 0/*for depot*/);
            addressList.add(startingAddress);

            for (Request req : requestList) {
                addressList.add(req.getPickupAddress());
                addressList.add(req.getDeliveryAddress());
            }
            adressRCreated = true;
            return addressList;
        }
    }

    public Address getAddressById(long id){
        for(Address address : addressList){
            if(id == address.getId()){
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
    public Intersection getStartingPoint() {
        return startingPoint;
    }
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
