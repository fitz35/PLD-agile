package controller;

import Model.MapFactory;
import ihm.windowMap.WelcomeWindow;

public class WaitRequest implements StateController{

    @Override
    public void loadRequest(Controller controller, String path)
    {
        try{
            Controller.getMap().loadRequest(path);
            //load requests back method
            Controller.getWindow2().changePanel(0);
            Controller.getMap().notifyObservers();
            if(Controller.getMap().isPlanningLoaded())
            {
                controller.setStateController(new ComputeFirstTour());
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void back(Controller controller)
    {
        Controller.getWindow2().dispose();
        Controller.setFirstWindow(new WelcomeWindow(controller));
        Controller.setMap( MapFactory.create());
        controller.setStateController(new InitialState());
    }
}
