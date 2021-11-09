package controller.command;

import Model.Address;
import Model.Map;

public class AddNewRequestCmd implements Command {
    private Map map;
    private Address newPickup;
    private Address newDelivery;
    private Address beforNewPickup;
    private Address beforNewDelivery;

    public AddNewRequestCmd(Map map, Address newPickup, Address newDelivery, Address beforNewPickup,
                            Address beforNewDelivery ) {
        this.map = map;
        this.newPickup = newPickup;
        this.newDelivery = newDelivery;
        this.beforNewPickup = beforNewPickup;
        this.beforNewDelivery = beforNewDelivery;
    }

    @Override
    public void doCommand() throws Exception {
        try {
            map.addRequest(beforNewPickup,newPickup,beforNewDelivery,newDelivery);
        } catch (Exception e) {
            throw (e);
        }
    }

    @Override
    public void undoCommand() {
        //delete the request that have juste been added
        try {
          map.deleteRequest(newPickup);
        }catch (Exception e) {
          throw (e);
        }
    }

}