package controller.command;

import Model.Address;
import Model.Map;



public class AddNewRequestCmd implements Command {
    private Map map;
    private Address newPickup;
    private Address newDelivery;
    private Address beforNewPickup;
    private Address beforNewDelivery;

    /**
     *
     * @param map the map obtained by the parsing of the xml file
     * @param newPickup the address of the new pickup point to be added
     * @param newDelivery the address of the new delivery point to be added
     * @param beforNewPickup the address of the point of interest to be visited before visiting the new pickup
     * @param beforNewDelivery the address of the point of interest to be visited before visiting the new delivery
     */

    public AddNewRequestCmd(Map map, Address newPickup, Address newDelivery, Address beforNewPickup,
                            Address beforNewDelivery ) {
        this.map = map;
        this.newPickup = newPickup;
        this.newDelivery = newDelivery;
        this.beforNewPickup = beforNewPickup;
        this.beforNewDelivery = beforNewDelivery;
    }

    /**
     * tries to add a new request to the list of existing requests
     * @throws Exception if the new request could not be added to the list of existing requests
     */
    public void doCommand() throws Exception {
        try {
            map.addRequest(beforNewPickup,newPickup,beforNewDelivery,newDelivery);
        } catch (Exception e) {
            throw (e);
        }
    }

    /**
     * deletes the request that have juste been added
     */

    @Override
    public void undoCommand() {
        try {
          map.deleteRequest(newPickup);
        }catch (Exception e) {
          throw (e);
        }
    }

}