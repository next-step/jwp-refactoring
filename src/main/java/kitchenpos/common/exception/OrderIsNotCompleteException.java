package kitchenpos.common.exception;

public class OrderIsNotCompleteException extends RuntimeException {

    private static final String ERROR_MESSAGE_ORDER_NOT_FINISH = "주문 상태가 조리 혹은 식사인 주문이 존재합니다.";

    public OrderIsNotCompleteException() {
        super(ERROR_MESSAGE_ORDER_NOT_FINISH);
    }
}
