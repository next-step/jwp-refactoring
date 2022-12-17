package kitchenpos.exception;

public enum TableGroupErrorMessage {
    ORDER_TABLES_CANNOT_BE_EMPTY("주문 테이블은 빈 컬렉션일 수 없습니다."),
    MUST_BE_GREATER_THAN_MINIMUM_SIZE("주문 테이블 크기는 최소 크기보다 작을 수 없습니다."),
    NOT_FOUND_BY_ID("ID로 테이블 그룹을 찾을 수 없습니다.");

    private static final String TITLE = "[ERROR] ";

    private final String message;

    TableGroupErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}
