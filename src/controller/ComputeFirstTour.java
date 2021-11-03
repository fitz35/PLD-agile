package controller;

class ComputeFirstTour implements StateController{

    @Override
    public void loadTour(Controller controller)
    {
        try{
            controller.getMap().computeTour(300);
            if(controller.getMap().isFirstTourComputed())
            {
                controller.setStateController(new SetupState());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void back(Controller controller)
    {
        controller.setStateController(new WaitRequest());
        controller.getWindow2().changePanel(1);
        controller.getMap().resetPlanningRequest();
        controller.getMap().resetTour();
    }
}