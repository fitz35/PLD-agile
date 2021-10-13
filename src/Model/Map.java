package Model;

import java.util.ArrayList;

public class Map {
    private ArrayList<Segment> segmentList;
    private ArrayList<Intersection> intersectionList;

    public Map() {
        segmentList = new ArrayList<Segment>();
        intersectionList = new ArrayList<Intersection>();
    }

    public void loadMap(String fileName)
    {
        String[] words = fileName.split(".");
        assert(words[words.length-1] == 1);
    }

}
