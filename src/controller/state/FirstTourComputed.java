package controller.state;

import controller.Controller;
import controller.state.RequestLoaded;
import controller.state.StateController;

public class FirstTourComputed implements StateController {
    @Override
    public void back(Controller controller)
    {
        controller.setStateController(new RequestLoaded());
        controller.getMap().resetTour();
    }
}