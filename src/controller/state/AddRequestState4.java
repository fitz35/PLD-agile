package controller.state;

import controller.Controller;

public class AddRequestState4 implements StateController{


    @Override
    public void back(Controller controller)
    {
        System.out.println("Go back to state AddRequestState3");
    }
}
