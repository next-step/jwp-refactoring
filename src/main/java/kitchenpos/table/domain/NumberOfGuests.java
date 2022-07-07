package kitchenpos.table.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    @Column(name = "number_of_guests")
    private final int value;

    protected NumberOfGuests() {
        this.value = 0;
    }

    public NumberOfGuests(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static void validate(int value) {
        if (value < 0) {
            throw new BadRequestException(ErrorCode.NEGATIVE_NUMBER_OF_GUESTS);
        }
    }
}
