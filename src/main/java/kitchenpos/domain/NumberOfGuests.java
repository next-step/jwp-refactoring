package kitchenpos.domain;


import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    @Column
    private int guests;

    public NumberOfGuests() {
    }

    public NumberOfGuests(int guests) {
        this.guests = guests;
    }

    private void validate(int guests) {
        if (guests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getGuests() {
        return guests;
    }

    public void changeNumberOfGuests(int guests) {
        validate(guests);
        this.guests = guests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NumberOfGuests)) {
            return false;
        }
        NumberOfGuests that = (NumberOfGuests) o;
        return guests == that.guests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guests);
    }
}
