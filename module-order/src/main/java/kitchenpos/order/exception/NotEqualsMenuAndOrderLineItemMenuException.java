package kitchenpos.order.exception;

public class NotEqualsMenuAndOrderLineItemMenuException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE = "메뉴와 주문한 주문항목의 메뉴가 일치해야 합니다. (menuCount = %s, orderLineItemMenuSize = %s)";

    public NotEqualsMenuAndOrderLineItemMenuException(long menuCount, int orderLineItemMenuSize) {
        super(String.format(EXCEPTION_MESSAGE, menuCount, orderLineItemMenuSize));
    }
}
