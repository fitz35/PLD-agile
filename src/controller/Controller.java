package controller;

import Model.MapFactory;
import Model.MapInterface;
import Model.Tour;
import controller.state.InitialState;
import controller.state.StateController;
import ihm.windowMap.WelcomeWindow;
import ihm.windowMap.WindowMap;

public class Controller {
    private StateController stateController;
    private MapInterface map;
    private Tour tour;
    private WelcomeWindow firstWindow;
    private WindowMap window2;

    public Controller()
    {
        stateController = new InitialState();
        this.createMap();
        window2 = new WindowMap(this);
        window2.setVisible(false);
        firstWindow = new WelcomeWindow(this);
        map.addObserver(getFirstWindow());
        map.addObserver(getWindow2());
    }

    //set state method
    protected void setCurrentState(StateController state){
        stateController = state;
    }

    //overrided method
    public void loadMap(String path){ this.stateController.loadMap(this, path);}

    public void loadRequest(String path){ this.stateController.loadRequest(this, path);}

    public void loadTour() { this.stateController.loadTour(this); }

    public void stopComputing() {this.stateController.stopComputing(this); }

    public void continueComputing() {this.stateController.continueTour(this, 10000); }

    public void back() {this.stateController.back(this);}

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

    //--------------- setter ---------------
    public void setStateController(StateController stateController) {
        this.stateController = stateController;
    }

    public void setMap(MapInterface map) {
        this.map = map;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public void createMap()
    {
        map = MapFactory.create();
    }

    //--------------- main ---------------
    public static void main(String []args)
    {
        new Controller();
    }

}
