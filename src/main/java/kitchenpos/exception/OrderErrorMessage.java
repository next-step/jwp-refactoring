package kitchenpos.exception;

public enum OrderErrorMessage {
    REQUIRED_ORDER_TABLE("주문 테이블은 필수 필드입니다."),
    CANNOT_BE_CHANGED("요리 중이거나 식사 중인 테이블 상태를 변경할 수 없습니다."),
    NOT_FOUND_BY_ID("ID로 주문 테이블을 찾을 수 없습니다."),
    CANNOT_CHANGE_COMPLETION_ORDER("완료된 주문은 변경할 수 없습니다."),
    ORDER_TABLE_CANNOT_BE_EMPTY("빈 주문 테이블에서 주문할 수 없습니다."),
    ORDER_LINE_ITEMS_CANNOT_BE_EMPTY("주문 생성 시 빈 주문 제품을 요청할 수 없습니다.");

    private static final String TITLE = "[ERROR] ";

    private final String message;

    OrderErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}
