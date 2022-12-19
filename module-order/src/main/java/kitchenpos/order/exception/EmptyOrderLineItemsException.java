package kitchenpos.order.exception;

public class EmptyOrderLineItemsException extends RuntimeException {

    public EmptyOrderLineItemsException() {
        super("주문 항목이 비었습니다.");
    }
}
