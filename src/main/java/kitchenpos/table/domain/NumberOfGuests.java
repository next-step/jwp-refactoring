package kitchenpos.table.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;

public class NumberOfGuests {
    public static void validate(int value) {
        if (value < 0) {
            throw new BadRequestException(ErrorCode.NEGATIVE_NUMBER_OF_GUESTS);
        }
    }
}
