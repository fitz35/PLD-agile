package controller;

class ComputeFirstTour implements StateController{

    @Override
    public void loadTour(Controller controller)
    {
        try{
            Controller.getMap().computeTour(300);
            if(Controller.getMap().isFirstTourComputed())
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
        Controller.getWindow2().changePanel(1);
        Controller.getMap().resetPlanningRequest();
    }
}