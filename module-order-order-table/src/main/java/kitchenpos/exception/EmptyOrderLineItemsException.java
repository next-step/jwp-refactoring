package kitchenpos.exception;

public class EmptyOrderLineItemsException extends RuntimeException {
    public static final EmptyOrderLineItemsException EMPTY_ORDER_LINE_ITEMS_EXCEPTION = new EmptyOrderLineItemsException(
            "주문 항목 목록이 비어있습니다.");

    public EmptyOrderLineItemsException(String message) {
        super(message);
    }
}
