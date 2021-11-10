package controller.state;

import controller.Controller;
import controller.command.ListOfCommands;

/**
 * main state, it reached after having load a Map some request and manage to compute a tour
 * has methode addNewRequest deleteRequest redo undo and back
 */
public class FirstTourComputed implements StateController {

    /**
     * leads to the state AddRequest1
     * @param controller
     */
    @Override
    public void addNewRequest(Controller controller) {
        controller.setStateController(new AddRequestState1());
    }

    /**
     * leads to the state DeleteRequest
     * @param controller
     */
    @Override
    public void deleteRequest(Controller controller) {
        controller.setStateController(new DeleteRequest());
    }

    /**
     * redo a action that have been done and undo befor
     * @param listOfCommands
     * @throws Exception
     */
    @Override
    public void redo(ListOfCommands listOfCommands) throws Exception {
        listOfCommands.redo();
    };

    /**
     * cancel the last loaded action
     * @param listOfCommands
     * @throws Exception
     */
    @Override
    public void undo(ListOfCommands listOfCommands) throws Exception {
        listOfCommands.undo();
    };

    /**
     * bring to state MapLoaded
     * delete the Tour and the planning request
     * @param controller
     */
    @Override
    public void back(Controller controller){
        controller.setStateController(new MapLoaded());
        controller.getMap().resetTour();
        controller.getMap().resetPlanningRequest();
        controller.getListOfCommands().resetList();
    }
}
