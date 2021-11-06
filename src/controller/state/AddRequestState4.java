package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;

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
    public void chooseBeforNewDelivery(Controller controller, Intersection theBeforNewDelivery)
    {
        try{
            beforNewDelivery = controller.getMap().getPlanningRequest().getAddressById(theBeforNewDelivery.getId());
            controller.getMap().addRequest(beforNewPickup, newPickup, beforNewDelivery, newDelivery);
            controller.setStateController(new FirstTourComputed());
        }catch (Exception e){
            throw(e);
        }
    }

    @Override
    public void back(Controller controller)
    {
        System.out.println("Go back to state AddRequestState3");
    }
}
