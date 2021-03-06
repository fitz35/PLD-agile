package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;

public class AddRequestState1 implements StateController{
    private Address newPickup;
    private boolean arrivedCauseIssue;

    public AddRequestState1() {
        arrivedCauseIssue = false;
    }

    public AddRequestState1(boolean issue) {
        arrivedCauseIssue = issue;
    }

    @Override
    public void chooseNewPickup(Controller controller, Intersection theNewPickup, int pickupDuration){
        try{
            // ajouter une vérification que l'intersection existe ?
            newPickup = new Address(theNewPickup, pickupDuration,1);
            controller.setStateController(new AddRequestState2(newPickup));
        }catch (Exception e){
            throw(e);
        }
    }

    @Override
    public void back(Controller controller){
        System.out.println("Go back to state FirstTourComputed");
        controller.setStateController(new FirstTourComputed());
    }

    public boolean isArrivedCauseIssue() {
        return arrivedCauseIssue;
    }
}
