package controller;

import Model.Tour;
import Model.XML.MapFactory;
import Model.XML.MapInterface;
import ihm.windowMap.WelcomeWindow;
import ihm.windowMap.WindowMap;

public class Controller {
    private StateController stateController;
    private static MapInterface map;
    private static Tour tour;
    private static WelcomeWindow firstWindow;
    private static WindowMap window2;

    public void setState(StateController newEtat) {
        this.stateController = newEtat;
    }

    public void action() {
        stateController.action(this);
    }

    public static void main(String []args)
    {
        Controller controller = new Controller();
        controller.setState(new ControllerBeforeLoadingMap());
        controller.action();
        controller.setState(new ControllerBeforeLoadingRequest());
        controller.action();
        map= MapFactory.create();
        tour=map.getTour();
        firstWindow = new WelcomeWindow();
        map.addObserver(firstWindow);
    }
    public static void  loadMap(String mapPath)
    {
        try {
            map.loadMap(mapPath);
            window2 = new WindowMap();
            map.addObserver(window2);
            firstWindow.dispose();
            map.notifyObservers();
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

    }
    public static void  loadRequest(String mapPath)
    {
        try{
            map.loadRequest(mapPath);
            //load requests back method
            window2.changePanel(0);
            map.notifyObservers();
        }catch(Exception e)
        {
            e.printStackTrace();

        }
    }
    public static void backToWelcomeWindow()
    {
        window2.dispose();
        firstWindow= new WelcomeWindow();
        map= MapFactory.create();
    }


}
