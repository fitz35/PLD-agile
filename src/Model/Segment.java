package Model;
import java.util.Objects;

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
     * @param origin
     * @param destination
     * @param name
     * @param length
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


    public static void main(String[] args) {
        Intersection a = new Intersection(1, 20, 20);
        Intersection b = new Intersection(2, 25, 20);
        Intersection c = new Intersection(3, 25, 30);
        Intersection d = new Intersection(4, 15, 25);
        Segment s1 = new Segment(a, b, "AB", 30);
        Segment s2 = new Segment(c, d, "CD", 25);
        System.out.println("test d'égalité de s1 et s2");
        s1.equals(s2);
    }
}
