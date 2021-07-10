package kitchenpos.exception;

public class NoProductException extends IllegalArgumentException {

    private static final Long serialVersionUID = 540236956800849912L;
    private static final String NO_PRODUCT = "상품이 존재하지 않습니다.";

    public NoProductException() {
        super(NO_PRODUCT);
    }

    public NoProductException(String message) {
        super(message);
    }
}
