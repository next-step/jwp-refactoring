package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderTableExceptionCode;
import kitchenpos.utils.NumberUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderGuests {
    @Column
    private int numberOfGuests;

    protected OrderGuests() {}

    public OrderGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (NumberUtil.isNotPositiveNumber(numberOfGuests)) {
            throw new IllegalArgumentException(OrderTableExceptionCode.INVALID_NUMBER_OF_GUESTS.getMessage());
        }
    }

    public OrderGuests changeNumberOfGuests(int numberOfGuests, boolean empty) {
        if(empty) {
            throw new IllegalArgumentException(OrderTableExceptionCode.NUMBER_OF_GUESTS_CANNOT_BE_CHANGED.getMessage());
        }

        return new OrderGuests(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
