package controller.command;

import Model.Address;
import Model.Map;

public class AddNewRequest implements Command {
    private Map map;
    private Address newPickup;
    private Address newDelivery;
    private Address beforNewPickup;
    private Address beforNewDelivery;

    public AddNewRequest(Map map, Address newPickup, Address newDelivery, Address beforNewPickup, Address beforNewDelivery ) {
        this.map = map;
        this.newPickup = newPickup;
        this.newDelivery = newDelivery;
        this.beforNewPickup = beforNewPickup;
        this.beforNewDelivery = beforNewDelivery;
    }

    public void doCommand() {
        try {
            map.addRequest(beforNewPickup,newPickup,beforNewDelivery,newDelivery);
        } catch (Exception e) {
            throw (e);
        }
    }

    @Override
    public void undoCommand() {
        //plan.remove(shape);
    }

}