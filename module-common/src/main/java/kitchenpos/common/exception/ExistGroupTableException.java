package kitchenpos.common.exception;


import static kitchenpos.common.message.ErrorMessage.*;

public class ExistGroupTableException extends IllegalArgumentException {
    public ExistGroupTableException() {
        super(EXIST_GROUP_TABLE.message());
    }

    public ExistGroupTableException(String s) {
        super(s);
    }
}
