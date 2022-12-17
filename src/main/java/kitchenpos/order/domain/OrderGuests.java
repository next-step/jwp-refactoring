package kitchenpos.order.domain;

import kitchenpos.exception.OrderTableErrorMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class OrderGuests {
    private static final int COMPARE_NUM = 0;

    @Column
    private int numberOfGuests;

    protected OrderGuests() {}

    public OrderGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < COMPARE_NUM) {
            throw new IllegalArgumentException(OrderTableErrorMessage.INVALID_NUMBER_OF_GUESTS.getMessage());
        }
    }

    public OrderGuests changeNumberOfGuests(int numberOfGuests, boolean empty) {
        if(empty) {
            throw new IllegalArgumentException(OrderTableErrorMessage.NUMBER_OF_GUESTS_CANNOT_BE_CHANGED.getMessage());
        }
        return new OrderGuests(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderGuests that = (OrderGuests) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}
