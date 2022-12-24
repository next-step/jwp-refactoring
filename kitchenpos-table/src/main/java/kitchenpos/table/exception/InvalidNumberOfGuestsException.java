package kitchenpos.table.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class InvalidNumberOfGuestsException extends BadRequestException {

    public InvalidNumberOfGuestsException() {
        super(INVALID_NUMBER_OF_GUESTS);
    }
}
