package controller;

public interface StateController {
    void action(Controller context);
    void nextState(Controller context);
}
