package Model;

public class Segment {
    private Intersection origin;
    private Intersection destination;
    private String name;
    private double length;

    public Segment(Intersection origin, Intersection destination, String name, double length) {
        this.origin = origin;
        this.destination = destination;
        this.name = name;
        this.length = length;
    }

}