package kitchenpos.orderlineitem.exception;

public class MenuAndOrderLineItemSizeNotMatchException extends RuntimeException{
    public MenuAndOrderLineItemSizeNotMatchException() {
        super("메뉴와 주문항목의 수가 일치하지 않습니다");
    }
}
