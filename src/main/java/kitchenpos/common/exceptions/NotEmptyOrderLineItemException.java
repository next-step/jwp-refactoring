package kitchenpos.common.exceptions;

public class NotEmptyOrderLineItemException extends CustomException {
    public static final String NOT_EMPTY_ORDER_LINE_ITEM_EXCEPTION = "주문 항목이 있어야 합니다.";

    public NotEmptyOrderLineItemException() {
        super(NOT_EMPTY_ORDER_LINE_ITEM_EXCEPTION);
    }
}
