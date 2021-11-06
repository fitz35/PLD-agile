package controller.state;

import controller.Controller;
import controller.state.InitialState;
import controller.state.RequestLoaded;
import controller.state.StateController;

/**
 * 2nd state
 * has methode loadRequest and back
 */
public class MapLoaded implements StateController {

    /**
     * load the planning request, notifie the observer and go to the next state if everything went well
     * @param controller
     * @param path
     */
    @Override
    public void loadRequest(Controller controller, String path)
    {
        //System.out.println("In state 2");
        try{
            controller.getMap().loadRequest(path);
            //load requests back method
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

    /**
     * go to the previous state and reset the map 
     * @param controller
     */
    @Override
    public void back(Controller controller)
    {
        controller.getWindow2().setVisible(false);
        controller.getFirstWindow().setVisible(true);
        controller.getMap().resetMap();
        controller.setStateController(new InitialState());
    }
}
