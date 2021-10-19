package Model.XML;

import Model.Map;

public class MapFactory {

    public static MapInterface create(){
     return new Map();
    }

}
