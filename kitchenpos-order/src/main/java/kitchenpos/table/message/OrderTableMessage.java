package kitchenpos.table.message;

public enum OrderTableMessage {
    CHANGE_EMPTY_ERROR_TABLE_GROUP_MUST_BE_NOT_ENROLLED("테이블 그룹에 속해져있으므로 이용 유무를 변경할 수 없습니다."),
    CHANGE_EMPTY_ERROR_INVALID_ORDER_STATE("주문의 상태가 요리중이거나 식사중인경우 이용 유무를 변경할 수 없습니다."),
    CHANGE_GUESTS_ERROR_TABLE_MUST_BE_NOT_EMPTY_STATE("테이블이 이용가능한 상태이므로 손님수를 변경할 수 없습니다."),
    UN_GROUP_ERROR_INVALID_ORDER_STATE("주문의 상태가 요리중이거나 식사중인경우 그룹 해지를 할 수 없습니다."),
    UN_GROUP_ERROR_OTHER_TABLE_GROUP_MUST_BE_NOT_ENROLLED("다른 테이블 그룹에 속한 테이블이 있으므로 그룹해지를 할 수 없습니다."),
    GROUP_ERROR_ORDER_TABLE_IS_NOT_EMPTY("주문 테이블은 이용 가능한 상태이어야 합니다."),
    GROUP_ERROR_MORE_THAN_TWO_ORDER_TABLES("주문 테이블은 최소 2개 이상 주어져야 합니다."),
    GROUP_ERROR_OTHER_TABLE_GROUP_MUST_BE_NOT_ENROLLED("다른 테이블 그룹에 속한 테이블이 있으므로 생성할 수 없습니다."),
    ;

    private final String message;

    OrderTableMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
