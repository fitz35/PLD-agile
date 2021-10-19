package controller;

class ControllerBeforeComputeFirstTour implements StateController{
    @Override
    public void action(Controller context){
        System.out.println("State: before compute first tour");
    }

    @Override
    public void nextState(Controller controller){
        System.out.println("Last implemented state go back to first one");
        controller.setState(new ControllerBeforeLoadingMap());
    }
}
