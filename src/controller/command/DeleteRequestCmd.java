package controller.command;

import Model.Address;
import Model.Map;

import java.util.List;

/**
 * Command implemented to delete a request from the tour
 */
public class DeleteRequestCmd implements Command {
    private Map map;
    private Address addressToDelete;
    private Address newPickup;
    private Address newDelivery;
    private Address beforNewPickup;
    private Address beforNewDelivery;


    /**
     * Constructor
     * @param map
     * @param addressToDelete
     */

    public DeleteRequestCmd(Map map, Address addressToDelete) {
        this.map = map;
        List<Address> addresses = map.addressForUndoDelete(addressToDelete);
        newPickup = addresses.get(0);
        newDelivery = addresses.get(1);
        beforNewPickup = addresses.get(2);
        beforNewDelivery = addresses.get(3);
        this.addressToDelete = addressToDelete;
    }

    @Override
    public void doCommand() {
        map.deleteRequest(addressToDelete);
    }

    @Override
    public void undoCommand() throws Exception {
        try {
            map.addRequest(beforNewPickup,newPickup,beforNewDelivery,newDelivery);
        } catch (Exception e) {
            throw (e);
        }
    }
}
