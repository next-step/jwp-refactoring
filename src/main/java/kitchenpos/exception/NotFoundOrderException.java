package kitchenpos.exception;

public class NotFoundOrderException extends RuntimeException {
    private static final String NOT_FOUND_ORDER_MESSAGE = "해당하는 주문을 찾을 수 없습니다. (id = %s)";

    public NotFoundOrderException(Long id) {
        super(String.format(NOT_FOUND_ORDER_MESSAGE, id));
    }
}
