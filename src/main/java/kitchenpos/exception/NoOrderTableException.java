package kitchenpos.exception;

public class NoOrderTableException extends RuntimeException {
    public NoOrderTableException() {
        super("주문 테이블이 없습니다");
    }
}
