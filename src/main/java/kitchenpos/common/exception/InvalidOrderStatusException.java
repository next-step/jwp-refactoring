package kitchenpos.common.exception;

public class InvalidOrderStatusException extends RuntimeException{

    public InvalidOrderStatusException() {
        super(String.format("orderStatus invalid."));
    }
}
