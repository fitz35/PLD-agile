package controller;

public class InitialState implements StateController{

    @Override
    public void loadMap(Controller controller, String mapPath) {
        try {
            controller.getMap().loadMap(mapPath);
            controller.getWindow2().setVisible(true);
            controller.getFirstWindow().setVisible(false);
            if(controller.getMap().isMapLoaded())
            {
                controller.setStateController(new MapLoaded());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
