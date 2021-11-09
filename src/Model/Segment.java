package Model;
import java.util.Objects;

import java.util.Objects;

/**
 * Class that represents each segment (c.f our report)
 */
public class Segment {
    /**
     * The origin of the segment
     */
    private Intersection origin;

    /**
     * The destination of the segment
     */
    private Intersection destination;

    /**
     * The name of the segment
     */
    private String name;

    /**
     * The length of the segment
     */
    private double length;

    /**
     * Constructor
     * @param origin Origin of the segment
     * @param destination Destination of the segment
     * @param name Street name of the segment
     * @param length Length of the segment in meters
     */
    public Segment(Intersection origin, Intersection destination, String name, double length) {
        this.origin = origin;
        this.destination = destination;
        this.name = name;
        this.length = length;
    }

    // ------- Getters -------

    /**
     * @return origin
     */
    public Intersection getOrigin() {
        return origin;
    }

    /**
     * @return destination
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     * @return length
     */
    public double getLength() {
        return length;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())){
            return false;
        }
        Segment s = (Segment) o;
        return (Objects.equals(origin, s.origin) && Objects.equals(destination, s.destination))
                    && (Objects.equals(name, s.name) && Objects.equals(length, s.length));
    }
}
