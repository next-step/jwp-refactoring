package kitchenpos.exception;

public class NotExistOrderLineItemsException extends RuntimeException{
    public static final String NOT_EXIST_ORDER_LINE_ITEMS_EXCEPTION_MESSAGE = "주문항목이 존재하지 않습니다.";

    public NotExistOrderLineItemsException() {
        super(NOT_EXIST_ORDER_LINE_ITEMS_EXCEPTION_MESSAGE);
    }
}
