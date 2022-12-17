package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class GroupTableException extends BadRequestException {

    public GroupTableException() {
        super(GROUP_TABLE_ERROR);
    }
}
