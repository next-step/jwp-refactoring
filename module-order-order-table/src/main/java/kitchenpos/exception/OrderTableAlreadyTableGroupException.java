package kitchenpos.exception;

public class OrderTableAlreadyTableGroupException extends RuntimeException {
    public static final OrderTableAlreadyTableGroupException ORDER_TABLE_ALREADY_TABLE_GROUP_EXCEPTION = new OrderTableAlreadyTableGroupException(
            "이미 단체지정인 테이블 입니다.");

    public OrderTableAlreadyTableGroupException(String message) {
        super(message);
    }

}
