package kitchenpos.table.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class GroupTableException extends BadRequestException {

    public GroupTableException() {
        super(GROUP_TABLE_ERROR);
    }
}
