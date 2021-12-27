package kitchenpos.common.exception;

public enum InputDataErrorCode {
    THE_NAME_CAN_NOT_EMPTY("[ERROR] 이름은 필수 입력입니다."),
    THE_PRICE_MUST_INPUT("[ERROR]가격은 필수 입력입니다."),
    THE_PRICE_CAN_NOT_INPUT_LESS_THAN_ZERO("[ERROR] 가격은 0 미만은 입력할 수 없습니다."),
    THE_NUMBER_OF_GUESTS_IS_LESS_THAN_ZERO("[ERROR] 손님의 숫자는 음수가 될 수 없습니다.");

    private String errorMessage;

    InputDataErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String errorMessage() {
        return this.errorMessage;
    }
}
