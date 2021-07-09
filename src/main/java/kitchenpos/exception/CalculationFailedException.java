package kitchenpos.exception;

public class CalculationFailedException extends RuntimeException {
    public CalculationFailedException(String message) {
        super(message);
    }
}
