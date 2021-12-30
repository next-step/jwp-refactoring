package kitchenpos.common.exceptions;

public class NotEmptyOrderTableGroupException extends CustomException {
    public static final String NOT_EMPTY_ORDER_TABLE_GROUP_MESSAGE = "테이블 그룹에 속해 있지 않아야 합니다";

    public NotEmptyOrderTableGroupException() {
        super(NOT_EMPTY_ORDER_TABLE_GROUP_MESSAGE);
    }
}
