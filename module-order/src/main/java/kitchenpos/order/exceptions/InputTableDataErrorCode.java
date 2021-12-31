package kitchenpos.order.exceptions;

public enum InputTableDataErrorCode {

    THE_STATUS_IS_ALEADY_FULL("[ERROR] 이 테이블에는 이미 손님이 있습니다."),
    THE_STATUS_IS_ALEADY_EMPTY("[ERROR] 이 테이블에는 이미 비어 있습니다."),
    THE_STATUS_IS_WRONG_DATA("[ERROR] 잘못된 테이블 상태입니다."),
    THE_TABLE_CAN_NOT_REGISTER_GROUP_BECAUSE_OF_EMPTY_STATUS("[ERROR] 이 테이블은 비어있기 때문에 그룹으로 묶을 수 없습니다."),
    THE_TABLE_CAN_NOT_FIND("[ERROR] 테이블을 찾을 수 없습니다."),
    THE_NUMBER_OF_GUESTS_IS_NOT_LESS_THAN_ZERO("[ERROR] 테이블 멤버 수는 0 미만일 수 없습니다."),
    THE_TABLE_GROUP_CAN_NOT_FIND("[ERROR] 테이블 그룹을 찾을 수 없습니다."),
    THE_TABLE_HAS_GROUP("그룹지정이 되어있어 상태를 변경할 수 없습니다.");

    private String errorMessage;

    InputTableDataErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String errorMessage() {
        return this.errorMessage;
    }
}
