package kitchenpos.exception;

public enum OrderLineItemErrorMessage {
    REQUIRED_ORDER("주문은 필수 필드입니다."),
    REQUIRED_MENU("메뉴는 필수 필드입니다."),
    INVALID_QUANTITY("수량은 0보다 작을 수 없습니다.");

    private static final String TITLE = "[ERROR] ";

    private final String message;

    OrderLineItemErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}
