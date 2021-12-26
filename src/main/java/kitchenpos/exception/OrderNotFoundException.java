package kitchenpos.exception;

public class OrderNotFoundException extends RuntimeException {
    public static final String ORDER_NOT_FOUND = "주문을 찾을 수 없습니다.";

    public OrderNotFoundException() {
        super(ORDER_NOT_FOUND);
    }
}
