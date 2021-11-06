package controller.state;

import Model.Intersection;
import controller.Controller;
import controller.command.ListOfCommands;
import ihm.windowMap.WindowMap;

/**
 * Interface implemented from the contoller design pattern
 */
public interface StateController {
    public default void  loadMap(Controller context, String path){};
    public default void loadRequest(Controller controller, String path){};
    public default void loadTour(Controller controller){};
    public default void back(Controller controller){};
    public default void continueTour(Controller controller, int timeout){};
    public default void stopComputing(Controller controller){};
    public default void addNewRequest(Controller controller, ListOfCommands listeOfCommands, Intersection newPickup, Intersection beforNewPickup, int pickupDuration, Intersection newDelivery, Intersection beforNewDelivery, int deliveryDuration){};
    public default void chooseNewPickup(Controller controller, Intersection theNewPickup){};
    public default void chooseBeforNewPickup(Controller controller, Intersection theBeforNewPickup){};
}
