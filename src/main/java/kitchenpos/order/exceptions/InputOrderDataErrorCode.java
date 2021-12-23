package kitchenpos.order.exceptions;

public enum InputOrderDataErrorCode {
    THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER("[ERROR] 완료인 상태에서 다른 상태로 변경할 수 없습니다.");

    private String errorMessage;

    InputOrderDataErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String errorMessage(){
        return this.errorMessage;
    }
}
