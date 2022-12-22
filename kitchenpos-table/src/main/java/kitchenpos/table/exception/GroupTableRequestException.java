package kitchenpos.table.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class GroupTableRequestException extends BadRequestException {

    public GroupTableRequestException() {
        super(GROUP_TABLE_REQUEST_ERROR);
    }
}
