package controller.command;

import Model.Tour;
import controller.command.Command;

public class AddCommand implements Command {

    private Tour tour;

    /**
     * Create the command which adds the shape s to the plan p
     * @param t the plan to which f is added
     */
    public AddCommand(Tour t){
        this.tour = t;
    }

    @Override
    public void doCommand() {
        //Map.add(shape);
    }

    @Override
    public void undoCommand() {
        //plan.remove(shape);
    }

}