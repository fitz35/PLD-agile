package controller;

import Model.Tour;
import Model.XML.TourFactory;
import ihm.windowMap.WelcomeWindow;

public class Controller {
    public static void main(String []args)
    {

        Tour tour = TourFactory.create();
        WelcomeWindow a = new WelcomeWindow();
        a.createWindow();
    }


}
