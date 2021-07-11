package kitchenpos.orderlineitem.exception;

public class EmptyOrderLineItemsException extends RuntimeException{
    public EmptyOrderLineItemsException() {
        super("주문항목이 비어있습니다");
    }
}
