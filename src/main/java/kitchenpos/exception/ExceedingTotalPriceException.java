package kitchenpos.exception;

public class ExceedingTotalPriceException extends RuntimeException {
    public ExceedingTotalPriceException(String message) {
        super(message);
    }
}
