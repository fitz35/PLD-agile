package controller;

import ihm.windowMap.WindowMap;

public class InitialState implements StateController{
    @Override
    public void loadMap(Controller controller, String mapPath) {
        try {
            Controller.getMap().loadMap(mapPath);
            Controller.setWindow2(new WindowMap());
            Controller.getMap().addObserver(Controller.getWindow2());
            Controller.getFirstWindow().dispose();
            Controller.getMap().notifyObservers();
            if(Controller.getMap().isMapLoaded())
            {
                controller.setStateController(new WaitRequest());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
