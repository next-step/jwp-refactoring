package kitchenpos.order.exception;

public class EmptyOrderLineItemsException extends RuntimeException {
    public static final String DONT_EMPTY_IS_ORDER_LINE_ITEMS = "주문 항목(orderLineItem) 가 존재하지 않습니다.";

    public EmptyOrderLineItemsException() {
        super(DONT_EMPTY_IS_ORDER_LINE_ITEMS);
    }
}
