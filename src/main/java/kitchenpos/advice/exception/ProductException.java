package kitchenpos.advice.exception;

public class ProductException extends RuntimeException {

    public ProductException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
