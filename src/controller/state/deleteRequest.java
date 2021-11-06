package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;
import controller.command.DeleteRequest;
import controller.command.ListOfCommands;

public class deleteRequest implements StateController{

    @Override
    public void  deleteRequest(Controller controller , Intersection intersectionToDelete, ListOfCommands listOfCommands)
    {
        Address addressToDelete = controller.getMap().getPlanningRequest().getAddressById(intersectionToDelete.getId());
        DeleteRequest requestToDelete = new DeleteRequest((Model.Map)controller.getMap(), addressToDelete);
        controller.setStateController(new FirstTourComputed());
    }

    @Override
    public void back(Controller controller)
    {
        controller.setStateController(new FirstTourComputed());
        System.out.println("Go back to state FirstTourComputed");
    }

}
