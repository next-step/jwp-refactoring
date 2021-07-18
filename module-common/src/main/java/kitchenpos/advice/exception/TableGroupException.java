package kitchenpos.advice.exception;

public class TableGroupException extends BusinessException {

    public TableGroupException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
