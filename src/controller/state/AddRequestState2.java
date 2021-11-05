package controller.state;

import Model.Address;
import Model.Intersection;
import controller.Controller;

public class AddRequestState2 implements StateController{
    Address newPickup;

    public AddRequestState2 (Address newPickup){
        this.newPickup = newPickup;
    }

    @Override
    public void chooseBeforNewPickup(Controller controller, Intersection theBeforNewPickup)
    {
        try{
            //newPickup = controller.getMap().getPlanningRequest().getAddressById(newPickup.getId());
            //controller.setStateController(new AddRequestState(newPickup));
        }catch (Exception e){
            throw(e);
        }
    }

    @Override
    public void back(Controller controller)
    {
        System.out.println("Go back to state AddRequestState1");
    }

}
