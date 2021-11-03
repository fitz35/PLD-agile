package controller;

class RequestLoaded implements StateController{

    @Override
    public void loadTour(Controller controller)
    {
        try{
            controller.getMap().computeTour(300);
            if(controller.getMap().getTimedOutError() == 0)
            {
                controller.setStateController(new FirstTourComputed());
            }else if(controller.getMap().getTimedOutError() == 1)
            {
                controller.setStateController(new WaitOrder());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void back(Controller controller)
    {
        controller.setStateController(new MapLoaded());
        controller.getWindow2().changePanel(1);
        controller.getMap().resetPlanningRequest();
    }
}