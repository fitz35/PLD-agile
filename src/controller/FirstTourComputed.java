package controller;

public class FirstTourComputed implements StateController{
    @Override
    public void back(Controller controller)
    {
        controller.setStateController(new RequestLoaded());
        controller.getMap().resetTour();
    }
}