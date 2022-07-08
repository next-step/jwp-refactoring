package kitchenpos.ordertable.exception;

public class NoSuchOrderTableException extends KitchenPosArgumentException {
    private static final String ERROR_MESSAGE = "OrderTable이 존재하지 않습니다 (id: %d)";

    public NoSuchOrderTableException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
