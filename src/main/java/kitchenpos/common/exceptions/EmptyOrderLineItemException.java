package kitchenpos.common.exceptions;

public class EmptyOrderLineItemException extends CustomException {
    public static final String EMPTY_ORDER_LINE_ITEM_MESSAGE = "주문 상품 정보가 존재하지 않습니다.";

    public EmptyOrderLineItemException() {
        super(EMPTY_ORDER_LINE_ITEM_MESSAGE);
    }
}
