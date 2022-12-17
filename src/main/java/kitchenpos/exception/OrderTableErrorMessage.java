package kitchenpos.exception;

public enum OrderTableErrorMessage {
    NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP("비어 있지 않은 순서 테이블은 테이블 그룹에 포함될 수 없습니다."),
    ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP("이미 다른 테이블 그룹에 포함된 주문 테이블입니다."),
    NOT_FOUND_BY_ID("ID로 주문 테이블을 찾을 수 없습니다."),
    INVALID_NUMBER_OF_GUESTS("게스트 수는 음수일 수 없습니다."),
    NUMBER_OF_GUESTS_CANNOT_BE_CHANGED("빈 주문 테이블의 게스트 수를 변경할 수 없습니다.");

    private static final String TITLE = "[ERROR] ";

    private final String message;

    OrderTableErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}
