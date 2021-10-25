package controller;

import ihm.windowMap.WindowMap;

public interface StateController {
    public default void  loadMap(Controller context, String path){};
    public  default void loadRequest(Controller controller, String path){};

}
