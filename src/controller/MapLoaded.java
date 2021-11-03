package controller;

import Model.MapFactory;

public class MapLoaded implements StateController{

    @Override
    public void loadRequest(Controller controller, String path)
    {
        System.out.println("In state 2");
        try{
            controller.getMap().loadRequest(path);
            //load requests back method
            controller.getWindow2().changePanel(0);
            controller.getMap().notifyObservers();
            if(controller.getMap().isPlanningLoaded())
            {
                controller.setStateController(new RequestLoaded());
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void back(Controller controller)
    {
        controller.getWindow2().setVisible(false);
        controller.getFirstWindow().setVisible(true);
        controller.setMap( MapFactory.create());
        controller.setStateController(new InitialState());
    }
}
