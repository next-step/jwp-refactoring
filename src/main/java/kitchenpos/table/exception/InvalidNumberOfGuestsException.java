package kitchenpos.table.exception;

import static kitchenpos.exception.ErrorMessage.*;

import kitchenpos.exception.BadRequestException;

public class InvalidNumberOfGuestsException extends BadRequestException {

    public InvalidNumberOfGuestsException() {
        super(INVALID_NUMBER_OF_GUESTS);
    }
}
