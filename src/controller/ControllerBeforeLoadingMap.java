package controller;

public class ControllerBeforeLoadingMap implements StateController{
    @Override
    public void action(Controller context){
        System.out.println("State: before loading map ");
    }

    @Override
    public void nextState(Controller controller){
        System.out.println("No condition to go to next state ");
        controller.setState(new ControllerBeforeLoadingRequest());
    }
}
