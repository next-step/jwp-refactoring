package kitchenpos.table.exception;

import static kitchenpos.exception.ErrorMessage.*;

import kitchenpos.exception.BadRequestException;

public class GroupTableException extends BadRequestException {

    public GroupTableException() {
        super(GROUP_TABLE_ERROR);
    }
}
