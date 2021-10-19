package Model;

public class Pair<T1, T2>{
    private T1 key;
    private T2 value;

    public Pair(T1 newKey, T2 newValue){
        this.key = newKey;
        this.value = newValue;
    }

    public T1 getKey() {
        return key;
    }

    public T2 getValue() {
        return value;
    }

  /*  @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }*/

    @Override
    public boolean equals(Pair o) {
        if (this.key == o.key && this.value == o.value) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

   /* public static void main(String[] args){
        Intersection i = new Intersection(1, 20, 20);
        Intersection ii = new Intersection(2, 20, 20);
        Segment s = new Segment(i, ii, "lol", 30);
        Pair<Intersection, Segment> dd= new Pair(i, s);
        System.out.println("lolllljdfklasjdl;fk");

    }*/
   public static void main(String[] args){
       Intersection a = new Intersection(1, 20, 20);
       Intersection b = new Intersection(2, 25, 20);
       Segment s1 = new Segment(a, b, "lilo", 30);
       Pair<Intersection, Segment> p1= new Pair(a, s1);
       Pair<Intersection, Segment> p2= new Pair(a, s1);
       Intersection c = new Intersection(3, 25, 30);
       Intersection d = new Intersection(4, 15, 25);
       Segment s2 = new Segment(c, d, "stech", 30);
       Pair<Intersection, Segment> p3= new Pair(c, s2);
       Segment s3 = new Segment(a, c, "lilo1", 30);
       Pair<Intersection, Segment> p4= new Pair(a, s3);
       System.out.println("test d'égalité de p1 et p2");
       p1.equals(p2);
       System.out.println("test d'égalité de p1 et p3");
       p1.equals(p3);
       System.out.println("test d'égalité de p2 et p4");
       p1.equals(p4);
       System.out.println("test d'égalité de p3 et p4");
       p3.equals(p4);
   }
}
