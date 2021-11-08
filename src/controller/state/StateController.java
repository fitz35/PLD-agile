package controller.state;

import Model.Intersection;
import controller.Controller;
import controller.command.ListOfCommands;
import ihm.windowMap.MapPanel;
import ihm.windowMap.WindowMap;

/**
 * Interface implemented from the contoller design pattern
 */
public interface StateController {
    public default void loadMap(Controller context, String path){};
    public default void loadRequest(Controller controller, String path){};
    public default void loadTour(Controller controller){};
    public default void back(Controller controller){};
    public default void continueTour(Controller controller, int timeout){};
    public default void stopComputing(Controller controller){};
    public default void addNewRequest(Controller controller){};
    public default void deleteRequest(Controller controller){};
    public default void setIntersection(Controller controller, Intersection newIntersection){};
    public default void setDuration(Controller controller, int duration){};
    public default void deleteRequest(Controller controller , Intersection intersectionToDelete,
                                      ListOfCommands listOfCommands){};
    public default void redo(ListOfCommands listOfCommands){};
    public default void undo(ListOfCommands listOfCommands){};

}
