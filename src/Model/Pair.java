package Model;

import Model.Intersection;
import Model.Map;
import Model.Segment;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }
    //If we have bugs we need to dig deeper into hashCode and hash
    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    public static void main(String[] args){
        Intersection i = new Intersection(1, 20, 20);
        Intersection ii = new Intersection(2, 20, 20);
        Segment s = new Segment(i, ii, "lol", 30);
        Pair<Intersection, Segment> dd= new Pair(i, s);
        System.out.println("lolllljdfklasjdl;fk");

    }
}
