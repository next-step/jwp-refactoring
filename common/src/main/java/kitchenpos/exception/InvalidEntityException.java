package kitchenpos.exception;

public class InvalidEntityException extends IllegalArgumentException {

    public InvalidEntityException(String s) {
        super(s);
    }

    public InvalidEntityException(Throwable cause) {
        super(cause);
    }

    public InvalidEntityException(Long id) {
        super("Not found Entity" + id);
    }
}
