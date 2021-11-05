package controller;

public class WaitOrder implements StateController{

    @Override
    public void continueTour(Controller controller, int timeout){
        try {
            controller.getMap().continueTour(timeout);
            if(controller.getMap().getTimedOutError() == 0)
            {
                controller.getMap().resetTimedOutError();
                controller.setStateController(new FirstTourComputed());
            }else if(controller.getMap().getTimedOutError() == 1)
            {
                controller.getMap().resetTimedOutError();
                controller.setStateController(new WaitOrder());
                controller.getWindow2().askToContinueTour();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopComputing(Controller controller)
    {
        controller.getMap().resetTimedOutError();
        controller.setStateController(new FirstTourComputed());
    }

    @Override
    public void back(Controller controller) {
        try {
            controller.getMap().resetTour();
            controller.getMap().resetTimedOutError();
            controller.setStateController(new RequestLoaded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
