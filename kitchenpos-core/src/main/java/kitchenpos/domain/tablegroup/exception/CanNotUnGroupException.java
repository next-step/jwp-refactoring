package kitchenpos.domain.tablegroup.exception;

public class CanNotUnGroupException extends IllegalArgumentException {
    public CanNotUnGroupException(String message) {
        super(message);
    }
}
