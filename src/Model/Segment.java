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

    public Intersection getOrigin() {
        return origin;
    }

    public Intersection getDestination() {
        return destination;
    }

    public double getLength() {
        return length;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Segment segment = (Segment) object;
        return java.lang.Double.compare(segment.length, length) == 0 && java.util.Objects.equals(origin, segment.origin) && java.util.Objects.equals(destination, segment.destination) && java.util.Objects.equals(name, segment.name);
    }

}
