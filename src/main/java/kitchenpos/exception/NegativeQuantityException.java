package kitchenpos.exception;

public class NegativeQuantityException extends RuntimeException {
    private static final String NEGATIVE_QUANTITY = "Quantity 는 0 이상의 숫자로 생성할 수 있습니다.";

    public NegativeQuantityException() {
        super(NEGATIVE_QUANTITY);
    }
}
