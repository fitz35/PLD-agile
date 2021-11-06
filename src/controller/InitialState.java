package controller;

public class InitialState implements StateController{

    @Override
    public void loadMap(Controller controller, String mapPath) {
        try {
            controller.getMap().loadMap(mapPath);
            if(controller.getMap().isMapLoaded())
            {
                controller.setStateController(new WaitRequest());
                controller.getWindow2().setVisible(true);
                controller.getFirstWindow().setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
