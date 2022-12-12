package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.exception.OrderTableExceptionCode;
import kitchenpos.utils.NumberUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TableGuests {
    @Column
    private int numberOfGuests;

    protected TableGuests() {}

    public TableGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (NumberUtil.isNotPositiveNumber(numberOfGuests)) {
            throw new IllegalArgumentException(OrderTableExceptionCode.INVALID_NUMBER_OF_GUESTS.getMessage());
        }
    }

    public TableGuests changeNumberOfGuests(int numberOfGuests, boolean empty) {
        if (empty) {
            throw new IllegalArgumentException(OrderTableExceptionCode.NUMBER_OF_GUESTS_CANNOT_BE_CHANGED.getMessage());
        }

        return new TableGuests(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
