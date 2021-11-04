package controller.state;

import controller.Controller;
import controller.state.InitialState;
import controller.state.RequestLoaded;
import controller.state.StateController;

public class MapLoaded implements StateController {

    @Override
    public void loadRequest(Controller controller, String path)
    {
        //System.out.println("In state 2");
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
        controller.getMap().resetMap();
        controller.setStateController(new InitialState());
    }
}
