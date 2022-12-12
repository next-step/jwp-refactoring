package kitchenpos;

public enum ExceptionMessage {
    PRODUCT_PRICE_LOWER_THAN_MINIMUM("상품가격이 최소 가격보다 작습니다.");

    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
