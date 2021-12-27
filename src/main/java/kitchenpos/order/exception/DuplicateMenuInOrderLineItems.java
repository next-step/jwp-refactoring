package kitchenpos.order.exception;

public class DuplicateMenuInOrderLineItems extends RuntimeException {

    private static final String ERROR_MESSAGE_DUPLICATE_MENU = "주문항목들 중에 중복된 메뉴가 존재합니다.";

    public DuplicateMenuInOrderLineItems() {
        super(ERROR_MESSAGE_DUPLICATE_MENU);
    }
}
