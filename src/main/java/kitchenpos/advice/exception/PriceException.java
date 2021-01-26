package kitchenpos.advice.exception;

public class PriceException extends RuntimeException {
    public PriceException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
