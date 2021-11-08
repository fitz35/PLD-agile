package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;
import controller.command.AddNewRequestCmd;
import controller.command.DeleteRequestCmd;

public class DeleteRequest implements StateController{
    private Address addressToDelete;

    @Override
    public void setIntersection(Controller controller, Intersection theBeforNewDelivery){
        try{
            addressToDelete = controller.getMap().getPlanningRequest().getAddressById(theBeforNewDelivery.getId());
            DeleteRequestCmd myCommandToExecute = new DeleteRequestCmd((Model.Map)controller.getMap(), addressToDelete);
            controller.getListOfCommands().add(myCommandToExecute);
            controller.setStateController(new FirstTourComputed());
        }catch (Exception e){
            throw(e);
        }
    }

    @Override
    public void back(Controller controller) {
        controller.setStateController(new FirstTourComputed());
        System.out.println("Go back to state FirstTourComputed");
    }

}
