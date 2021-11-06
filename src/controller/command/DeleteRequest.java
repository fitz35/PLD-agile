package controller.command;

import Model.Address;
import Model.Map;

public class DeleteRequest implements Command {
    private Map map;
    private Address addressToDelete;
/**
 * ajouter tt les attibut de la methode addNewRequest pour le undo et etre capable de remettre tt ds l'ordre
 * 
 */
    /**
     *
     * @param map
     * @param addressToDelete
     */

    public DeleteRequest(Map map, Address addressToDelete) {
        this.map = map;
        this.addressToDelete = addressToDelete;
    }

    @Override
    public void doCommand() {
        //call the methode from the back
        //map.deleteRequest(addressToDelete);
    }

    @Override
    public void undoCommand() {

        //add request => must find a way to store all the needed address

    }
}
