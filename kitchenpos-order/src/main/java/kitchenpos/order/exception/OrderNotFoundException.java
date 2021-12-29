package kitchenpos.order.exception;

public class OrderNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE_NOT_EXIST_ORDER = "존재하지 않는 주문입니다.";

    public OrderNotFoundException() {
        super(ERROR_MESSAGE_NOT_EXIST_ORDER);
    }
}
