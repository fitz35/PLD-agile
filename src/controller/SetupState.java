package controller;

public class SetupState implements StateController{
    @Override
    public void back(Controller controller)
    {
        controller.setStateController(new ComputeFirstTour());
    }
}