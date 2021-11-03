package controller;

import ihm.windowMap.WindowMap;

public class InitialState implements StateController{

    @Override
    public void loadMap(Controller controller, String mapPath) {
        try {
            controller.getMap().loadMap(mapPath);
            controller.getWindow2().setVisible(true);
            controller.getFirstWindow().setVisible(false);
            if(controller.getMap().isMapLoaded())
            {
                controller.setStateController(new WaitRequest());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}