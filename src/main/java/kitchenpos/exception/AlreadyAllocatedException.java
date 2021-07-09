package kitchenpos.exception;

public class AlreadyAllocatedException extends RuntimeException {
    public AlreadyAllocatedException(String message) {
        super(message);
    }
}
