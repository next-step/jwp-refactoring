package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    @Column(name = "number_of_guests", nullable = false, columnDefinition = "int(11)")
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int value() {
        return numberOfGuests;
    }
}
