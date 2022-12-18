package kitchenpos.table.exception;

import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.ErrorMessage;

public class GroupTableRequestException extends BadRequestException {

    public GroupTableRequestException() {
        super(ErrorMessage.GROUP_TABLE_REQUEST_ERROR);
    }
}
