package kitchenposNew.order.exception;

public class EmptyOrderLineItemsException extends RuntimeException {
    public EmptyOrderLineItemsException() {
        super("주문 메뉴가 없습니다.");
    }
}
