package kitchenpos.exception;

public class NoProductException extends RuntimeException {
    public NoProductException() {
        super("해당하는 상품이 없습니다");
    }
}
