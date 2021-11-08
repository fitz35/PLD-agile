package controller.command;

import controller.command.Command;

import java.util.LinkedList;

public class ListOfCommands {
    private LinkedList<Command> list;
    private int currentIndex;

    public ListOfCommands(){
        currentIndex = -1;
        list = new LinkedList<Command>();
    }

    /**
     * Add command c to this
     * @param c the command to add
     */
    public void add(Command c){
        int i = currentIndex+1;
        while(i<list.size()){
            list.remove(i);
        }
        currentIndex++;
        list.add(currentIndex, c);
        c.doCommand();
    }

    /**
     * Temporary remove the last added command (this command may be reinserted again with redo)
     */
    public void undo(){
        if (currentIndex >= 0){
            Command cde = list.get(currentIndex);
            currentIndex--;
            cde.undoCommand();
        }
    }

    /**
     * Permanently remove the last added command (this command cannot be reinserted again with redo)
     */
    public void cancel(){
        if (currentIndex >= 0){
            Command cde = list.get(currentIndex);
            list.remove(currentIndex);
            currentIndex--;
            cde.undoCommand();
        }
    }

    /**
     * Reinsert the last command removed by undo
     */
    public void redo(){
        if (currentIndex < list.size()-1){
            currentIndex++;
            Command cde = list.get(currentIndex);
            cde.doCommand();
        }
    }

    /**
     * Permanently remove all commands from the list
     */
    public void reset(){
        currentIndex = -1;
        list.clear();
    }

    /**
     * return 0 no undo no redo
     * return 1    undo no redo
     * return 2 no undo    redo
     * return 3    undo    redo
     *
     * @return
     */
    public int undoRedoAvailability(){
        /*
        0 = vide
        2 = liste > 0 && current = 0
        1 = liste > 0 && current = size
        3 = liste > 0 && current != size && current != 0
         */
        if(list.size()==0)
        {
            return 0;
        }else{
            if (currentIndex == 0){
                return 2;
            }else if(currentIndex == list.size()){
                return 1;
            }else {
                return 3;
            }
        }
    }

    public int getListSize(){
        return list.size();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}
