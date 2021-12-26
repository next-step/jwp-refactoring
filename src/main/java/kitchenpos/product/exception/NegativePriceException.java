package kitchenpos.product.exception;

public class NegativePriceException extends RuntimeException {
    public NegativePriceException() {
        super("가격이 0보다 작거나 같을 수 없습니다");
    }
}
