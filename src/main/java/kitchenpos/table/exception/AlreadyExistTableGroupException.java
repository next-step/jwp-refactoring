package kitchenpos.table.exception;

public class AlreadyExistTableGroupException extends RuntimeException {

    public AlreadyExistTableGroupException() {
    }

    public AlreadyExistTableGroupException(String message) {
        super(message);
    }
}
