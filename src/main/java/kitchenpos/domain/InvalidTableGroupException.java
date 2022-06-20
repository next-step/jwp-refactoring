package kitchenpos.domain;

public class InvalidTableGroupException extends RuntimeException {

    public InvalidTableGroupException(String message) {
        super(message);
    }
}
