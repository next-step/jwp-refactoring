package kitchenpos.exception;

public class OrderLineItemError {
    private static final String PREFIX = "[ERROR] ";

    public static final String CANNOT_EMPTY = PREFIX + "주문 항목이 비어있습니다.";
    public static final String REQUIRED_ORDER = PREFIX + "주문이 필수로 지정되어야 합니다.";
    public static final String REQUIRED_MENU = PREFIX + "메뉴가 필수로 지정되어야 합니다.";
    public static final String INVALID_QUANTITY = PREFIX + "수량이 0 이상으로 지정되어야 합니다.";

    private OrderLineItemError() {
        
    }
}
