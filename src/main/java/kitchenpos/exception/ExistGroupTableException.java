package kitchenpos.exception;


import static kitchenpos.common.ErrorMessage.EXIST_GROUP_TABLE;

public class ExistGroupTableException extends IllegalArgumentException {
    public ExistGroupTableException() {
        super(EXIST_GROUP_TABLE.message());
    }

    public ExistGroupTableException(String s) {
        super(s);
    }
}
