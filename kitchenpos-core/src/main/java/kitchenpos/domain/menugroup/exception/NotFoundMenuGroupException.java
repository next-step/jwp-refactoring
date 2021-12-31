package kitchenpos.domain.menugroup.exception;

public class NotFoundMenuGroupException extends IllegalArgumentException {
    public NotFoundMenuGroupException(String message) {
        super(message);
    }
}
