package controller.command;

/**
 * Interface for commands
 */
public interface Command {

    /**
     * Execute the command this
     */
    void doCommand() throws Exception;

    /**
     * Execute the reverse command of this
     */
    void undoCommand() throws Exception;

}

