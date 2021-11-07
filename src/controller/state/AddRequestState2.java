package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;

public class AddRequestState2 implements StateController{
    Address newPickup;
    Address beforNewPickup;

    public AddRequestState2 (Address newPickup){
        this.newPickup = newPickup;
    }

    @Override
    public void chooseBeforNewPickup(Controller controller, Intersection theBeforNewPickup){
        try{
            beforNewPickup = controller.getMap().getPlanningRequest().getAddressById(theBeforNewPickup.getId());
            controller.setStateController(new AddRequestState3(newPickup, beforNewPickup));
        }catch (Exception e){
            throw(e);
        }
    }

    @Override
    public void back(Controller controller){
        controller.setStateController(new AddRequestState1());
        System.out.println("Go back to state AddRequestState1");
    }

}
