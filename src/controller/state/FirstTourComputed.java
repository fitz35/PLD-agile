package controller.state;

import controller.Controller;
import controller.command.ListOfCommands;

public class FirstTourComputed implements StateController {

    @Override
    public void addNewRequest(Controller controller) {
        controller.setStateController(new AddRequestState1());
    }

    @Override
    public void deleteRequest(Controller controller) { controller.setStateController(new DeleteRequest());}

    @Override
    public void redo(ListOfCommands listOfCommands){
        listOfCommands.redo();
    };

    @Override
    public void undo(ListOfCommands listOfCommands){
        listOfCommands.undo();
    };

    @Override
    public void back(Controller controller){
        controller.setStateController(new RequestLoaded());
        controller.getMap().resetTour();
    }
}
