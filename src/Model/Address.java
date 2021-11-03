package Model;

import java.util.Objects;

public class Address extends Intersection{
    private int addressDuration;

    public Address(long id, double latitude, double longitude, int addressDuration) {
        super(id, latitude, longitude);
        this.addressDuration = addressDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Intersection)) return false;
        if (!super.equals(o)) return false;
        //Address address = (Address) o;
        return true;//addressDuration == address.addressDuration;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public int getAddressDuration() {
        return addressDuration;
    }

    public void setAddressDuration(int addressDuration) {
        this.addressDuration = addressDuration;
    }
}
