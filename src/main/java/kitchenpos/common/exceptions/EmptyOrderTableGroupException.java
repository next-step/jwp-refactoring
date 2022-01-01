package kitchenpos.common.exceptions;

public class EmptyOrderTableGroupException extends CustomException {
    public static final String EMPTY_ORDER_TABLE_GROUP_MESSAGE = "주문 테이블 그룹은 필수로 존재해야 합니다.";

    public EmptyOrderTableGroupException() {
        super(EMPTY_ORDER_TABLE_GROUP_MESSAGE);
    }
}
