package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Guests {
    @Column(name = "number_of_guests", nullable = false)
    private int number;

    protected Guests() {
    }

    public Guests(int number) {
        this.number = number;
    }

    public static Guests none() {
        return new Guests(0);
    }

    private void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Guests guests = (Guests) o;
        return number == guests.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
