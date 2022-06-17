package kitchenpos.domain;

public class InvalidOrderTablesException extends RuntimeException {

    public InvalidOrderTablesException(String message) {
        super(message);
    }
}
