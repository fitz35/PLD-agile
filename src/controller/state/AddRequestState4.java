package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;
import controller.command.AddNewRequestCmd;
import controller.command.ListOfCommands;

public class AddRequestState4 implements StateController{
    private Address newPickup;
    private Address beforNewPickup;
    private Address newDelivery;
    private Address beforNewDelivery;

    public AddRequestState4(Address newPickup, Address beforNewPickup, Address newDelivery) {
        this.newPickup = newPickup;
        this.beforNewPickup = beforNewPickup;
        this.newDelivery = newDelivery;
    }

    @Override
    public void chooseBeforNewDelivery(Controller controller, Intersection theBeforNewDelivery){
        try{
            beforNewDelivery = controller.getMap().getPlanningRequest().getAddressById(theBeforNewDelivery.getId());
            AddNewRequestCmd myCommandToExecute = new AddNewRequestCmd((Model.Map)controller.getMap(),newPickup,newDelivery,beforNewPickup,beforNewDelivery);
            controller.getListOfCommands().add(myCommandToExecute);
            controller.setStateController(new FirstTourComputed());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void back(Controller controller){
        controller.setStateController(new AddRequestState3(newPickup,beforNewPickup));
        System.out.println("Go back to state AddRequestState3");
    }
}
