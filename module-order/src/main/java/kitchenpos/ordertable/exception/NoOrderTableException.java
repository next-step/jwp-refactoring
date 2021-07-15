package kitchenpos.ordertable.exception;

public class NoOrderTableException extends RuntimeException {
    public NoOrderTableException() {
        super("포함된 주문 테이블이 하나도 없습니다.");
    }
}
