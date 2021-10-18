package controller;

import Model.Map;
import Model.Tour;
import Model.XML.TourFactory;
import ihm.windowMap.WelcomeWindow;
import ihm.windowMap.WindowMapLoadRequest;

public class Controller {
    private static Map map;
    private static Tour tour;
    private static WelcomeWindow firstWindow;
    private static WindowMapLoadRequest window2;
    public static void main(String []args)
    {
        tour= TourFactory.create();
        map=tour.getMap();
        firstWindow = new WelcomeWindow();
    }
    public static void  loadMap(String mapPath)
    {
       map.loadMap(mapPath);
       window2 = new WindowMapLoadRequest();
       tour.addObserver(window2);
        firstWindow.dispose();
        window2.createWindow();

    }


}
