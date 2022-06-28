package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.EXIST_GROUP_TABLE;

public class ExistGroupTableException extends IllegalArgumentException {
    public ExistGroupTableException() {
        super(EXIST_GROUP_TABLE);
    }

    public ExistGroupTableException(String s) {
        super(s);
    }
}
