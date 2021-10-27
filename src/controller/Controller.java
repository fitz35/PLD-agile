package controller;

import Model.Tour;
import Model.MapFactory;
import Model.MapInterface;
import ihm.windowMap.WelcomeWindow;
import ihm.windowMap.WindowMap;

public class Controller {
    private StateController stateController;
    private static MapInterface map;
    private static Tour tour;
    private static WelcomeWindow firstWindow;
    private static WindowMap window2;

    //set state method
    protected void setCurrentState(StateController state){
        stateController = state;
    }

    //overrided method
    public void loadMap(String path){ this.stateController.loadMap(this, path);}

    public void  loadRequest(String path){ this.stateController.loadRequest(this, path); }

    public void loadTour() { this.stateController.loadTour(this); }

    // UNKNOWN
    public static void backToWelcomeWindow()
    {
        window2.dispose();
        firstWindow= new WelcomeWindow();
        map= MapFactory.create();
    }
    public static void backToWindowLoadRequest()
    {
        window2.changePanel(1);
        map.resetPlanningRequest();
    }


    //--------------- getter ---------------

    public StateController getStateController() {
        return stateController;
    }

    public static MapInterface getMap() {
        return map;
    }

    public static Tour getTour() {
        return tour;
    }

    public static WelcomeWindow getFirstWindow() {
        return firstWindow;
    }

    public static WindowMap getWindow2() {
        return window2;
    }

    //--------------- setter ---------------
    public void setStateController(StateController stateController) {
        this.stateController = stateController;
    }

    public static void setMap(MapInterface map) {
        Controller.map = map;
    }

    public static void setTour(Tour tour) {
        Controller.tour = tour;
    }

    public static void setFirstWindow(WelcomeWindow firstWindow) {
        Controller.firstWindow = firstWindow;
    }

    public static void setWindow2(WindowMap window2) {
        Controller.window2 = window2;
    }


    //--------------- main ---------------
    public static void main(String []args)
    {
        Controller controller = new Controller();

        map= MapFactory.create();
        tour=map.getTour();
        firstWindow = new WelcomeWindow();
        map.addObserver(firstWindow);
    }

}
