package kitchenpos.exception;

public enum ProductExceptionType {
    MIN_PRICE("상품 금액은 0보다 커야 합니다."),
    PRODUCT_NOT_FOUND("상품이 존재 하지 않습니다.");

    private final String message;

    ProductExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
