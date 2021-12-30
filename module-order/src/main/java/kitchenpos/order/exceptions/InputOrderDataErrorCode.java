package kitchenpos.order.exceptions;

public enum InputOrderDataErrorCode {
    THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER("[ERROR] 완료인 상태에서 다른 상태로 변경할 수 없습니다."),
    THE_ORDER_LINE_IS_EMPTY("[ERROR] 주문 항목이 비어있습니다."),
    THE_ORDER_CAN_NOT_SEARCH("[ERROR] 주문을 찾을 수 없습니다."),
    THE_ORDER_IS_COOKING_OR_IS_EATING("[ERROR] 주문이 조리중 이거나 식사 중입니다.");

    private String errorMessage;

    InputOrderDataErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String errorMessage(){
        return this.errorMessage;
    }
}
