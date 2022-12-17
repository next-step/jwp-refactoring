package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class InvalidNumberOfGuestsException extends BadRequestException {

    public InvalidNumberOfGuestsException() {
        super(INVALID_NUMBER_OF_GUESTS);
    }
}
