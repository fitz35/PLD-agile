package controller.command;

public interface Command {

    /**
     * Execute the command this
     */
    void doCommand() throws Exception;

    /**
     * Execute the reverse command of this
     */
    void undoCommand() throws Exception;
/**
 *
 * Controller => state en question
 *
 * sate.ListOfCommand.addCommand(new AddCommand 4 param)
 * add commaand = new point
 * delete point
 *
 *
 */




}

