package kitchenpos.domain.tablegroup.exception;

public class CanNotGroupException extends IllegalArgumentException {
    public CanNotGroupException(String message) {
        super(message);
    }
}
