package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;

public class AddRequestState1 implements StateController{
    private Address newPickup;
    private Intersection newIntersection;

    public AddRequestState1() {
        System.out.println("Clique on the new Delivery ");
    }

    @Override
    public void setIntersection(Controller controller, Intersection intersection){
        newIntersection = intersection;
    }

    @Override
    public void setDuration(Controller controller, int duration){
        try{
            newPickup = new Address(newIntersection, duration,1);
            controller.setStateController(new AddRequestState2(newPickup));
        }catch (Exception e){
            throw(e);
        }
    }

    /*@Override
    public void chooseNewPickup(Controller controller, int pickupDuration){
        try{
            // ajouter une vérification que l'intersection existe ?

            controller.setStateController(new AddRequestState2(newPickup));
        }catch (Exception e){
            throw(e);
        }
    }*/

    @Override
    public void back(Controller controller){
        System.out.println("Go back to state FirstTourComputed");
        controller.setStateController(new FirstTourComputed());
    }
}
