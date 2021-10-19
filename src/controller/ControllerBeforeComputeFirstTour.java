package controller;

public class ControllerBeforeComputeFirstTour implements StateController{
    @Override
    public void action(Controller context){
        System.out.println("State: before compute first tor");
    }
}
