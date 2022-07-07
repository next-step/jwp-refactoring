package kitchenpos.exception;

public class CannotCreateOrderTableException extends RuntimeException {
    public static final CannotCreateOrderTableException CANNOT_CREATE_ORDER_TABLE = new CannotCreateOrderTableException(
            "주문 테이블을 생성할 수 없습니다.");

    public CannotCreateOrderTableException(String message) {
        super(message);
    }
}
