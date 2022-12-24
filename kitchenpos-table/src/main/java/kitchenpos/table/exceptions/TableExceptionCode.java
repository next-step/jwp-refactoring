package kitchenpos.table.exceptions;

public enum TableExceptionCode {
    GUEST_NOT_NULL_AND_ZERO("손님은 0명 미만이거나 비어 있을 수 없습니다"),
    COOKING_MEAL_NOT_UNGROUP("조리나 식사 상태면 상태를 변경 할 수 없습니다"),
    ORDER_TABLE_NOT_FOUND("주문 테이블을 찾을 수 없습니다"),
    NOT_MATCH_ORDER_TABLE("매치 되는 주문테이블이 없습니다"),
    ORDER_TABLES_SIZE_IS_EMPTY_AND_MIN_SIZE("주문 테이블은 비어있거나, 한개 이하이면 안됩니다."),
    MATCH_GROUP_PRESENT("단체지정석이 존재합니다")
    ;
    private final String message;

    TableExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
