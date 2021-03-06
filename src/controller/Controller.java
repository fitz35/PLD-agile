package controller;

import Model.Intersection;
import Model.MapFactory;
import Model.MapInterface;
import Model.Tour;
import controller.command.ListOfCommands;
import controller.state.InitialState;
import controller.state.StateController;
import ihm.windowMap.WelcomeWindow;
import ihm.windowMap.WindowMap;

/**
 * Class Controller in the MVC (Model View Controller) architecture
 */
public class Controller {
    /**
     * List of command stored to enable undo / redo
     */
    private ListOfCommands listOfCommands;
    /**
     * Controller state from the design pattern Controller
     */
    private StateController stateController;
    /**
     * Map of the app
     */
    private MapInterface map;
    /**
     * Calculated tour for the biker
     */
    private Tour tour;
    /**
     * First displayed window
     */
    private WelcomeWindow firstWindow;
    /**
     * Second displayed window
     */
    private WindowMap window2;

    /**
     * Constructor
     */
    public Controller(){
        stateController = new InitialState();
        listOfCommands = new ListOfCommands();
        this.createMap();
        window2 = new WindowMap(this);
        window2.setVisible(false);
        firstWindow = new WelcomeWindow(this);
        map.addObserver(getFirstWindow());
        map.addObserver(getWindow2());
    }

    /**
     * load map methode
     * available in state InitialState
     * @param path
     */
    //overrided method
    public void loadMap(String path){
        this.stateController.loadMap(this, path);
    }

    /**
     * load request methode
     * available in state MapLoaded
     * @param path
     */
    public void loadRequest(String path){
        this.stateController.loadRequest(this, path);
    }

    /**
     * load tour methode it calculates the first tour with the loaded request
     * available in state RequestLoaded
     */
    public void loadTour() {
        this.stateController.loadTour(this);
    }

    /**
     * Stop the tour calculation and keep the best calculated tour
     * Leads to FirstTourComputed
     * available in state WaitOrder
     */
    public void stopComputing() {
        this.stateController.stopComputing(this);
    }

    /**
     * Continue the tour calculation for at least 10 seconds
     * Leads to FirstTourComputed
     * available in state WaitOrder
     */
    public void continueComputing() {
        this.stateController.continueTour(this, 10000);
    }

    /**
     * First step for adding a new request, leads to the state AddRequestState1()
     * available in state FirstTourComputed
     */
    public void addNewRequest(){this.stateController.addNewRequest(this);}

    /**
     * Select the new pickup of the new request to add
     * leads to state AddRequestState2
     * available in state AddRequestState1
     * @param theNewPickup
     * @param pickupDuration
     */
    public void chooseNewPickup(Intersection theNewPickup, int pickupDuration){
        this.stateController.chooseNewPickup(this, theNewPickup, pickupDuration );
    }


    /**
     * Select the point (pickup or delivery) who will precedes the new request's pickup
     * leads to state AddRequestState3
     * available in state AddRequestState2
     * @param theBeforNewPickup
     */
    public void chooseBeforNewPickup(Intersection theBeforNewPickup){
        this.stateController.chooseBeforNewPickup(this, theBeforNewPickup);
    }

    /**
     * Select the delivery of the new request to add
     * leads to state AddRequestState4
     * available in state AddRequestState3
     * @param theNewDelivery
     * @param deliveryDuration
     */
    public void chooseNewDelivery(Intersection theNewDelivery, int deliveryDuration){
        this.stateController.chooseNewDelivery(this, theNewDelivery, deliveryDuration);
    }

    /**
     * Select the point (pickup or delivery) who will precedes the new request's delivery
     * leads to state FirstTourComputed
     * available in state AddRequestState4
     * @param theBeforNewDelivery
     */
    public void chooseBeforNewDelivery(Intersection theBeforNewDelivery){
        this.stateController.chooseBeforNewDelivery(this, theBeforNewDelivery);
    };

    /**
     * Delete a request from the tour
     * available in state FirstTourComputed
     */
    public void deleteRequest(){
        this.stateController.deleteRequest(this);
    }

    /**
     * Select the pickup or delivery to delete and will delet the whole request
     * available in state deleteRequest
     * @param intersection
     */
    public void selectRequestToDelete(Intersection intersection){this.stateController.selectRequestToDelete(this, intersection);}

    /**
     * leads to the prevous state
     * available in almost every controller's state
     */
    public void back() {
        this.stateController.back(this);
    }

    /**
     * redo from undo/redo functionnalie
     * available in state FirstTourComputed
     * @throws Exception
     */
    public void redo() throws Exception {
        this.stateController.redo(listOfCommands);
    }

    /**
     * undo from undo/redo functionnalie
     * available in state FirstTourComputed
     * @throws Exception
     */
    public void undo() throws Exception {
        this.stateController.undo(listOfCommands);
    }

    //--------------- getter ---------------

    public StateController getStateController() {
        return stateController;
    }

    public MapInterface getMap() {
        return map;
    }

    public Tour getTour() {
        return tour;
    }

    public WelcomeWindow getFirstWindow() {
        return firstWindow;
    }

    public WindowMap getWindow2() {
        return window2;
    }

    public ListOfCommands getListOfCommands() {
        return listOfCommands;
    }

    //--------------- setter ---------------
    public void setStateController(StateController stateController) {
        this.stateController = stateController;
        window2.updatePanel();
    }

    public void setMap(MapInterface map) {
        this.map = map;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public void createMap(){
        map = MapFactory.create();
    }

    //--------------- main ---------------
    public static void main(String []args){
        new Controller();
    }

}
