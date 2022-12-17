package kitchenpos.table.message;

public enum OrderTableMessage {
    CHANGE_EMPTY_ERROR_TABLE_GROUP_MUST_BE_NOT_ENROLLED("테이블 그룹에 속해져있으므로 이용 유무를 변경할 수 없습니다."),
    CHANGE_EMPTY_ERROR_INVALID_ORDER_STATE("주문의 상태가 요리중이거나 식사중인경우 이용 유무를 변경할 수 없습니다."),
    CHANGE_GUESTS_ERROR_TABLE_MUST_BE_NOT_EMPTY_STATE("테이블이 이용가능한 상태이므로 손님수를 변경할 수 없습니다.");

    private final String message;

    OrderTableMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
