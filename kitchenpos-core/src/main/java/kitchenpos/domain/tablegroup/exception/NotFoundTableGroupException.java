package kitchenpos.domain.tablegroup.exception;

public class NotFoundTableGroupException extends IllegalArgumentException {
    public NotFoundTableGroupException(String message) {
        super(message);
    }
}
