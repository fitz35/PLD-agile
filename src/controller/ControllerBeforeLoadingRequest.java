package controller;

public class ControllerBeforeLoadingRequest implements StateController{


    @Override
    public void action(Controller context){
        System.out.println("State: before loading request ");
    }

    @Override
    public void nextState(Controller controller){
        System.out.println("No condition to go to next state ");
        controller.setState(new ControllerBeforeComputeFirstTour());
    }

}
