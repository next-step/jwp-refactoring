package kitchenpos.advice.exception;

public class TableGroupException extends RuntimeException {

    public TableGroupException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
