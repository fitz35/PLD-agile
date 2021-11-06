package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;

public class AddRequestState1 implements StateController{
    private Address newPickup;

    public AddRequestState1() {
        System.out.println("Clique on the new Delivery ");
    }

    @Override
    public void chooseNewPickup(Controller controller, Intersection theNewPickup, int pickupDuration)
    {
        try{
            // ajouter une vérification que l'intersection existe ?
            newPickup = new Address(theNewPickup, pickupDuration);
            controller.setStateController(new AddRequestState2(newPickup));
        }catch (Exception e){
            throw(e);
        }
    }

    @Override
    public void back(Controller controller)
    {
        System.out.println("Go back to state FirstTourComputed");
    }
}
