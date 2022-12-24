package kitchenpos.product.exceptions;

public enum ProductErrorCode {

    PRICE_NOT_NULL_AND_ZERO("상품 가격은 0원일 수 없습니다"),
    NOT_FOUND_PRODUCT("상품을 찾을 수 없습니다");

    private final String message;

    ProductErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
