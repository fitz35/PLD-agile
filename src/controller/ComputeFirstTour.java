package controller;

class ComputeFirstTour implements StateController{

    @Override
    public void loadTour(Controller controller)
    {
        controller.getMap().computeTour(300);
        System.out.println("tour loaded");
        if(controller.getMap().isFirstTourComputed())
        {
            controller.setStateController(new SetupState());
        }
    }

    @Override
    public void back(Controller controller)
    {
        controller.setStateController(new WaitRequest());
        controller.getWindow2().changePanel(1);
        controller.getMap().resetPlanningRequest();
    }
}