package kitchenpos.table.exception;

import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.ErrorMessage;

public class ChangeEmptyGroupException extends BadRequestException {

    public ChangeEmptyGroupException() {
        super(ErrorMessage.NOT_ALLOWED_CHANGE_EMPTY_WHEN_GROUP);
    }
}
