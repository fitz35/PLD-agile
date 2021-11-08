package controller.state;

import Model.Address;
import Model.Intersection;
import Model.Map;
import controller.Controller;
import controller.command.DeleteRequestCmd;

public class DeleteRequest implements StateController{

    @Override
    public void  selectRequestToDelete(Controller controller , Intersection intersectionToDelete){
        try{
            System.out.println("call selectReqToDelete");
            Address addressToDelete = controller.getMap().getPlanningRequest().getAddressById(intersectionToDelete.getId());
            DeleteRequestCmd requestToDelete = new DeleteRequestCmd((Map)controller.getMap(), addressToDelete);
            controller.getListOfCommands().add(requestToDelete);
            controller.setStateController(new FirstTourComputed());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void back(Controller controller) {
        controller.setStateController(new FirstTourComputed());
        System.out.println("Go back to state FirstTourComputed");
    }

}
