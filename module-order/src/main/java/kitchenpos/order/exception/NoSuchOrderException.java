package kitchenpos.order.exception;

public class NoSuchOrderException extends KitchenPosArgumentException {
    private static final String ERROR_MESSAGE = "Order가 존재하지 않습니다 (id: %d)";

    public NoSuchOrderException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
