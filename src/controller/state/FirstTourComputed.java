package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;
import controller.state.RequestLoaded;
import controller.state.StateController;

public class FirstTourComputed implements StateController {

    @Override
    public void addNewRequest(Controller controller, Intersection newPickup, Intersection beforNewPickup, int pickupDuration, Intersection newDelivery, Intersection beforNewDelivery, int deliveryDuration)
    {
        Address pickup = new Address(newPickup, pickupDuration);
        Address delivery = new Address(newDelivery, deliveryDuration);
        System.out.println("Adding new request");
        //getAddressById dans map *2 !! erreur si l address n'existe pas
        //controller.getMap().addNewRequest();
    }

    @Override
    public void back(Controller controller)
    {
        controller.setStateController(new RequestLoaded());
        controller.getMap().resetTour();
    }
}