package kitchenpos.table.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class ChangeEmptyGroupException extends BadRequestException {

    public ChangeEmptyGroupException() {
        super(NOT_ALLOWED_CHANGE_EMPTY_WHEN_GROUP);
    }
}
