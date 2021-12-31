package kitchenpos.domain.product.exception;

public class IllegalProductNameException extends IllegalArgumentException {
    public IllegalProductNameException(String message) {
        super(message);
    }
}
