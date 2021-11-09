package controller.state;

import controller.Controller;

/**
 * 3rd State
 * has methode loadTour and back
 */
public class RequestLoaded implements StateController {

    /**
     * Compute the first tour during at most 10 second and propose a first tour
     * @param controller
     */
    @Override
    public void loadTour(Controller controller) {
        try{
            controller.getMap().computeTour(10000);
            if(controller.getMap().getTimedOutError() == 0){
                controller.setStateController(new FirstTourComputed());
            }else if(controller.getMap().getTimedOutError() == 1){
                controller.setStateController(new WaitOrder());
                controller.getWindow2().askToContinueTour();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * bring to the state MapLoaded and delete the planning request
     * @param controller
     */
    @Override
    public void back(Controller controller){
        controller.setStateController(new MapLoaded());
        controller.getMap().resetPlanningRequest();
    }
}