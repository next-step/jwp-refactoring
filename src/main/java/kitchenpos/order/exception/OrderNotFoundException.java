package kitchenpos.order.exception;

public class OrderNotFoundException extends RuntimeException {
    private static final String message = "주문 정보가 존재하지 않습니다.";

    public OrderNotFoundException() {
        super(message);
    }
}
