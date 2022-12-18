package kitchenpos.product.message;

public enum PriceMessage {
    CREATE_ERROR_PRICE_MUST_BE_NOT_NULL("가격이 주어지지 않았습니다."),
    CREATE_ERROR_PRICE_MUST_BE_GREATER_THAN_ZERO("가격은 0원 이상이어야 합니다."),
    ;

    private final String message;

    PriceMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
