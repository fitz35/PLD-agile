package controller.state;

import Model.Address;
import Model.Intersection;
import Model.Map;
import controller.Controller;
import controller.command.AddNewRequest;
import controller.command.ListOfCommands;
import controller.state.RequestLoaded;
import controller.state.StateController;

import java.io.IOException;

public class FirstTourComputed implements StateController {

    @Override
    public void addNewRequest(Controller controller, ListOfCommands listeOfCommand, Intersection newPickup, Intersection beforNewPickup, int pickupDuration, Intersection newDelivery, Intersection beforNewDelivery, int deliveryDuration)
    {
        try {
            Address pickup = new Address(newPickup, pickupDuration);
            Address delivery = new Address(newDelivery, deliveryDuration);
            Address addressBeforNewPickup = controller.getMap().getPlanningRequest().getAddressById(beforNewPickup.getId());
            Address addressBeforNewDelivery = controller.getMap().getPlanningRequest().getAddressById(beforNewDelivery.getId());

            AddNewRequest requestToAdd = new AddNewRequest((Map) controller.getMap(), pickup, delivery, addressBeforNewPickup, addressBeforNewDelivery );
            listeOfCommand.add(requestToAdd);
        } catch (Exception e){
            throw (e);
        }
    }

    @Override
    public void back(Controller controller)
    {
        controller.setStateController(new RequestLoaded());
        controller.getMap().resetTour();
    }
}