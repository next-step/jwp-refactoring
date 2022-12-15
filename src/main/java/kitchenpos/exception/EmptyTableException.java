package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class EmptyTableException extends BadRequestException {

    public EmptyTableException() {
        super(EMPTY_TABLE);
    }
}
