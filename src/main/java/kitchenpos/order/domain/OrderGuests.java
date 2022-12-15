package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.criteria.Order;

@Embeddable
public class OrderGuests {
    @Column
    private int numberOfGuests;

    protected OrderGuests() {}

    public OrderGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static OrderGuests of(int numberOfGuests) {
        return new OrderGuests(numberOfGuests);
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 2) {
            throw new IllegalArgumentException();
        }
    }

    public OrderGuests changeNumberOfGuests(int numberOfGuests, boolean empty) {
        if(empty) {
            throw new IllegalArgumentException();
        }

        return new OrderGuests(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
