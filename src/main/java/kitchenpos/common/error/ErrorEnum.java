package kitchenpos.common.error;

public enum ErrorEnum {
    PRICE_IS_NOT_NULL("가격은 비어있을 수 없습니다."),
    PRICE_UNDER_ZERO("가격은 0원 이하일 수 없습니다.."),
    DEFAULT_ERROR("오류가 발생하였습니다");

    private final String message;

    public String message() {
        return message;
    }

    ErrorEnum(String message) {
        this.message  = message;
    }
}
