package controller;

public interface StateController {
    public default  void action(Controller context){};
    void nextState(Controller context);
}
