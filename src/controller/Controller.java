package controller;

import Model.Tour;
import Model.XML.MapFactory;
import Model.XML.MapInterface;
import ihm.windowMap.WelcomeWindow;
import ihm.windowMap.WindowMapLoadRequest;
import org.junit.platform.engine.support.descriptor.FileSystemSource;

public class Controller {
    private static MapInterface map;
    private static Tour tour;
    private static WelcomeWindow firstWindow;
    private static WindowMapLoadRequest window2;
    public static void main(String []args)
    {
        map= MapFactory.create();
        tour=map.getTour();
        firstWindow = new WelcomeWindow();
        map.addObserver(firstWindow);
    }
    public static void  loadMap(String mapPath)
    {
        try {
            map.loadMap(mapPath);
            window2 = new WindowMapLoadRequest();
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
            window2.changePanel();
            map.notifyObservers();
        }catch(Exception e)
        {
            e.printStackTrace();

        }
    }


}
