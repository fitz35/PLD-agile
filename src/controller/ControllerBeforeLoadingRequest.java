package controller;

public class ControllerBeforeLoadingRequest implements StateController{
    @Override
    public void action(Controller context){
        System.out.println("State: before loading request ");
    }
}
