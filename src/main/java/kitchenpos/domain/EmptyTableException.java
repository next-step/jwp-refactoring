package kitchenpos.domain;

import static kitchenpos.exception.ErrorMessage.*;

import kitchenpos.exception.BadRequestException;

public class EmptyTableException extends BadRequestException {

    public EmptyTableException() {
        super(EMPTY_TABLE);
    }
}
