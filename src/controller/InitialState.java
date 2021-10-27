package controller;

import ihm.windowMap.WindowMap;

public class InitialState implements StateController{
    @Override
    public void loadMap(Controller controller, String mapPath) {
        try {
            controller.getMap().loadMap(mapPath);
            controller.setWindow2(new WindowMap());
            controller.getMap().addObserver(controller.getWindow2());
            controller.getFirstWindow().dispose();
            controller.getMap().notifyObservers();
            if(controller.getMap().isMapLoaded())
            {
                controller.setStateController(new WaitRequest());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
