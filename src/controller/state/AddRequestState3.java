package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;

public class AddRequestState3 implements StateController{
    private Address newPickup;
    private Address beforNewPickup;
    private Address newDelivery;


    public AddRequestState3(Address newPickup, Address beforNewPickup) {
        this.newPickup = newPickup;
        this.beforNewPickup = beforNewPickup;
    }

    @Override
    public void chooseNewDelivery(Controller controller, Intersection theNewDelivery, int deliveryDuration) {
        try{
            // ajouter une vérification que l'intersection existe ?
            newDelivery = new Address(theNewDelivery, deliveryDuration,2);
            controller.setStateController(new AddRequestState4(newPickup, beforNewPickup, newDelivery));
        }catch (Exception e){
            throw(e);
        }
    }

    @Override
    public void back(Controller controller){
        controller.setStateController(new AddRequestState2(newPickup));
        System.out.println("Go back to state AddRequestState2");
    }
}
