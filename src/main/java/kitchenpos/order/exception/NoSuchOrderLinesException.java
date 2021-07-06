package kitchenpos.order.exception;

public class NoSuchOrderLinesException extends IllegalArgumentException {
    private static final Long serialVersionUID = 540236956800849912L;
    private static final String NO_SUCH_ORDER_LINES = "유효한 주문 정보가 아닙니다. 주문 정보를 확인하세요";

    public NoSuchOrderLinesException() {
        super(NO_SUCH_ORDER_LINES);
    }

    public NoSuchOrderLinesException(String message) {
        super(message);
    }
}
