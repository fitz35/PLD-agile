package controller;

public class WaitOrder implements StateController{

    @Override
    public void continueTour(Controller controller, int timeout){
        try {
            controller.getMap().continueTour(timeout);
            if(controller.getMap().getTimedOutError() == 0)
            {
                controller.setStateController(new FirstTourComputed());
            }else if(controller.getMap().getTimedOutError() == 1)
            {
                controller.setStateController(new WaitOrder());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void back(Controller controller) {
        try {
            controller.getMap().resetTour();
            controller.setStateController(new RequestLoaded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
