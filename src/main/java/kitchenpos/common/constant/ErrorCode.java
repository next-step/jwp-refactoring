package kitchenpos.common.constant;

public enum ErrorCode {

    NAME_NOT_EMPTY("이름은 빈 값을 허용하지 않습니다."),
    NUMBER_OF_GUESTS_LESS_THAN_ZERO("방문한 손님은 0명보다 작을 수 없습니다."),
    PRICE_NOT_EMPTY("가격은 빈 값을 허용하지 않습니다."),
    PRICE_LESS_THAN_ZERO("가격은 0원보다 작을 수 없습니다."),
    QUANTITY_LESS_THAN_ZERO("수량은 0단위보다 작을 수 없습니다.");

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
