package Model;
import java.util.Objects;

import java.util.Objects;

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
        Segment s3 = new Segment(a, c, "AC", 15);
        Segment s4 = new Segment(b, d, "BD", 20);
        Segment s5 = new Segment(a, d, "AD", 20);
        Segment s6 = new Segment(b, c, "BC", 10);
        Segment s7 = new Segment(a, b, "AB", 30);
        System.out.println("test d'égalité de s1 et s2");
        s1.equals(s2);
        System.out.println("test d'égalité de s1 et s3");
        s1.equals(s3);
        System.out.println("test d'égalité de s1 et s7");
        s1.equals(s7);
        System.out.println("test d'égalité de s2 et s3");
        s2.equals(s3);
        System.out.println("test d'égalité de s2 et s6");
        s2.equals(s6);
    }
}
