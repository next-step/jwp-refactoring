package kitchenpos.order.exception;

public class CreateOrderLineItemException extends RuntimeException {

    private static final String MENU_IS_NOT_NULL = "주문항목 생성 시 Menu정보는 필수입니다.";

    public CreateOrderLineItemException() {
        super(MENU_IS_NOT_NULL);
    }
}
