package kitchenpos.exception;

public enum KitchenposErrorCode {
    INVALID_PRICE("0 이상의 가격만 입력 가능합니다.");

    private final String message;

    KitchenposErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
