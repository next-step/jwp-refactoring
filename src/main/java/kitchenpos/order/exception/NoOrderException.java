package kitchenpos.order.exception;

public class NoOrderException extends IllegalArgumentException {
    private static final Long serialVersionUID = 540236956800849912L;
    private static final String NO_ORDER = "주문이 없습니다. 유효한 주문인지 확인하세요";

    public NoOrderException() {
        super(NO_ORDER);
    }

    public NoOrderException(String message) {
        super(message);
    }
}
