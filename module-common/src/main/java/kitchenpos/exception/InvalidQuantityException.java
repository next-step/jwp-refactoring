package kitchenpos.exception;

public class InvalidQuantityException extends RuntimeException {
    public static final InvalidQuantityException INVALID_QUANTITY_EXCEPTION = new InvalidQuantityException(
            "메뉴 상품의 수량은 1 이상이어야 합니다.");

    public InvalidQuantityException(String message) {
        super(message);
    }
}
