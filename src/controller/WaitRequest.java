package controller;

public class WaitRequest implements StateController{

    @Override
    public void loadRequest(Controller controller, String path)
    {
        try{
            controller.getMap().loadRequest(path);
            //load requests back method
            controller.getWindow2().changePanel(0);
            controller.getMap().notifyObservers();
            if(controller.getMap().isPlanningLoaded())
            {
                controller.setStateController(new ComputeFirstTour());
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
