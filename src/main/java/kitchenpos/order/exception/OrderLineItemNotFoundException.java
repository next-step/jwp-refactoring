package kitchenpos.order.exception;

public class OrderLineItemNotFoundException extends RuntimeException {
    private static final String message = "주문 항목 정보가 존재하지 않습니다.";

    public OrderLineItemNotFoundException() {
        super(message);
    }
}
