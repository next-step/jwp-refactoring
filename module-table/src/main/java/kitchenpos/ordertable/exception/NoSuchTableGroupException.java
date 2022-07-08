package kitchenpos.ordertable.exception;

public class NoSuchTableGroupException extends KitchenPosTableException {
    private static final String ERROR_MESSAGE = "TableGroup이 존재하지 않습니다 (id: %d)";

    public NoSuchTableGroupException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
