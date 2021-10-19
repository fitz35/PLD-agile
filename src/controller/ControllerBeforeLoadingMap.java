package controller;

public class ControllerBeforeLoadingMap implements StateController{
    @Override
    public void action(Controller context){
        System.out.println("State: before loading map ");
    }
}
