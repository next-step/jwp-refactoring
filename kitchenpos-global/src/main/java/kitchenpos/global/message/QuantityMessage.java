package kitchenpos.global.message;

public enum QuantityMessage {
    CREATE_ERROR_QTY_MUST_BE_NOT_NULL("수량이 주어지지 않았습니다."),
    CREATE_ERROR_QTY_MUST_BE_GREATER_THAN_ZERO("수량은 0개 이상이어야 합니다."),
    ;

    private final String message;

    QuantityMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
