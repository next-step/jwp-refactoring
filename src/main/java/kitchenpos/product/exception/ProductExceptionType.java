package kitchenpos.product.exception;

public enum ProductExceptionType {
    MIN_PRICE("가격은 0보다 커야 합니다.");

    private final String message;

    ProductExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
