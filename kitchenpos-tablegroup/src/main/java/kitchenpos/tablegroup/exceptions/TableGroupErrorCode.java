package kitchenpos.tablegroup.exceptions;

public enum TableGroupErrorCode {
    ORDER_TABLE_MIN_SIZE_ERROR("주문 테이블은 최소 2개 이상이여야 합니다"),
    NOT_FOUND_ORDER_TABLE("주문테이블에 주문이 없습니다"),
    TABLE_GROUP_NOT_FOUND("단체 지정을 찾을 수 없습니다.");

    private final String message;

    TableGroupErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
