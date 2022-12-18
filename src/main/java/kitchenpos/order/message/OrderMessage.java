package kitchenpos.order.message;

public enum OrderMessage {

    CREATE_ERROR_ORDER_TABLE_IS_EMPTY("주문 테이블이 이용가능한 상태이므로 생성 할 수 없습니다."),
    CREATE_ERROR_ORDER_LINE_ITEMS_IS_EMPTY("주문 상품이 주어져야 합니다."),
    CHANGE_ERROR_STATE_IS_COMPLETION("주문 완료 상태이므로 상태를 변경 할 수 없습니다."),
    ;

    private final String message;

    OrderMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
