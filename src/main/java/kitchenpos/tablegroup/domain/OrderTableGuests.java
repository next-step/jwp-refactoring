package kitchenpos.tablegroup.domain;

import kitchenpos.common.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderTableGuests {
    private static final long ZERO = 0;

    @Column
    private int numberOfGuests;

    protected OrderTableGuests() {}

    public OrderTableGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (isNotPositiveNumber(numberOfGuests)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_NUMBER_OF_GUESTS.getErrorMessage());
        }
    }

    public OrderTableGuests changeNumberOfGuests(int numberOfGuests, boolean empty) {
        if(empty) {
            throw new IllegalArgumentException(ErrorCode.CANNOT_CHANGE_NUMBER_OF_GUESTS.getErrorMessage());
        }

        return new OrderTableGuests(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    private boolean isNotPositiveNumber(int numberOfGuests) {
        return numberOfGuests < ZERO;
    }
}
