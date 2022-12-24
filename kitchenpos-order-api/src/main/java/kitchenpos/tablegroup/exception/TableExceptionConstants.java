package kitchenpos.tablegroup.exception;

public enum TableExceptionConstants {

    NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP("[ERROR] 해당 id에 해당하는 엔티티는 없습니다. "),
    ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP("[ERROR] 이미 해당 주문테이블은 테이블그룹에 속해 있습니다."),
    INVALID_NUMBER_OF_GUESTS("[ERROR] 손님 숫자는 음수가 될 수 없습니다."),
    CANNOT_CHANGE_NUMBER_OF_GUESTS("[ERROR] 비어있는 테이블은 손님의 숫자를 변경할 수 없습니다."),
    ORDER_TABLES_CANNOT_BE_EMPTY("[ERROR] 주문 테이블은 비어있을 수 없습니다."),
    MUST_BE_GREATER_THAN_MINIMUM_SIZE("[ERROR] 주문 테이블은 최소 2개 이상이어야 합니다."),
    INVALID_NUMBER_OF_ORDER_TABLES("[ERROR] 테이블 그룹핑을 위한 주문테이블은 최소 2개 이상이 되어야 합니다."),
    MUST_BE_GREATER_THAN_MINIMUM_ORDER_MENU_SIZE("[ERROR] 주문 메뉴는 최소 2개 이상이어야 합니다."),
    ;

    private final String errorMessage;

    TableExceptionConstants(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
