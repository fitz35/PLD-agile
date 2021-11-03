package controller;

import Model.Tour;

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
        //plan.add(shape);
    }

    @Override
    public void undoCommand() {
        //plan.remove(shape);
    }

}