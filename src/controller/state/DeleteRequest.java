package controller.state;

import Model.Address;
import Model.Intersection;
import Model.Map;
import controller.Controller;
import controller.command.DeleteRequestCmd;

/**
 * Delete request
 */
public class DeleteRequest implements StateController{

    /**
     * @param controller
     * @param intersectionToDelete
     */
    @Override
    public void  selectRequestToDelete(Controller controller , Intersection intersectionToDelete){
        try{
            System.out.println("call selectReqToDelete");
            Address addressToDelete = controller.getMap().getPlanningRequest().getAddressById(intersectionToDelete.getId());
            DeleteRequestCmd requestToDelete = new DeleteRequestCmd((Map)controller.getMap(), addressToDelete);
            controller.getListOfCommands().add(requestToDelete);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * @param controller
     */
    @Override
    public void back(Controller controller) {
        controller.setStateController(new FirstTourComputed());
    }

}
